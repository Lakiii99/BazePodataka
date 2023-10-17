package app.database.repository;

import app.model.*;
import app.tree.model.*;
import app.util.Util;
import lombok.Getter;

import javax.swing.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.stream.Collectors;

@Getter
public class MicrosoftSqlServerRepository implements Repository {

    private static final String[] TABLE_TYPES = new String[]{"TABLE"};
    private static final String SCHEMA_PATTERN = "dbo";
    private static final String TABLE_NAME = "TABLE_NAME";
    private static final String COLUMN_NAME = "COLUMN_NAME";
    private static final String FKTABLE_NAME = "FKTABLE_NAME";
    private static final String FKCOLUMN_NAME = "FKCOLUMN_NAME";
    private static final String PKCOLUMN_NAME = "PKCOLUMN_NAME";
    private static final String NOT_NULLABLE = "NO";
    private static final String IS_NULLABLE = "IS_NULLABLE";
    private static final String COLUMN_DEF = "COLUMN_DEF";
    private static final String TYPE_NAME = "TYPE_NAME";
    private static final String COLUMN_SIZE = "COLUMN_SIZE";
    private Connection connection;
    private String catalogName;


    //uspostavljanje konekcije sa bazom
    @Override
    public void initializeConnection() {
        final String url;
        final String user;
        final String password;
        try (final InputStream input = new FileInputStream("resources/config.properties")) {

            final Properties properties = new Properties();
            properties.load(input);
            url = properties.getProperty("db.url");
            user = properties.getProperty("db.user");
            password = properties.getProperty("db.password");

        } catch (final IOException ex) {
            JOptionPane.showMessageDialog(null, "Couldn't load database credentials!");
            return;
        }

        try {
            Class.forName("net.sourceforge.jtds.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
            this.catalogName = this.connection.getCatalog();
        } catch (final SQLException | ClassNotFoundException e) {
            JOptionPane.showMessageDialog(null, "Couldn't connect to database! Please, try again later.");
        }
    }

    //zatvaranje konekcije
    @Override
    public void closeConnection() {
        if (this.connection != null) {
            try {
                this.connection.close();
            } catch (final SQLException e) {
                JOptionPane.showMessageDialog(null, "Conneciton couldn't be closed!");
            }
        }
    }

    //ucitavanje podataka
    @Override
    public Resource loadSchema() throws SQLException {
        if (this.connection == null) {
            this.initializeConnection();
        }

        final DatabaseMetaData databaseMetaData = this.connection.getMetaData();
        final ResultSet resultSet = databaseMetaData.getTables(this.catalogName, SCHEMA_PATTERN, null, TABLE_TYPES);

        final Resource resource = new Resource(this.catalogName);
        final List<Entity> entities = new ArrayList<>();

        // Uzimamo tabele iz resultSet-a i pravimo entitete
        while (resultSet.next()) {
            final String tableName = resultSet.getString(TABLE_NAME);
            final Entity entity = new Entity(tableName);

            final ResultSet columnsResultSet = databaseMetaData.getColumns(this.catalogName, SCHEMA_PATTERN, tableName, null);
            final List<Attribute> attributes = new ArrayList<>();

            //Uzimamo kolone od trenutne tabele
            while (columnsResultSet.next()) {
                final String columnName = columnsResultSet.getString(COLUMN_NAME);
                final String columnType = columnsResultSet.getString(TYPE_NAME);
                final int columnSize = columnsResultSet.getInt(COLUMN_SIZE);
                final Attribute attribute = new Attribute(columnName, AttributeType.valueOf(columnType.toUpperCase()), columnSize);
                attributes.add(attribute);
                final String isNullable = columnsResultSet.getString(IS_NULLABLE);

                //Proveravamo null ogranicenje
                if (isNullable.equals(NOT_NULLABLE)) {
                    final Constraint constraint = new Constraint(ConstraintType.NOT_NULL);
                    attribute.getConstraints().add(constraint);
                }

                final String columnDef = columnsResultSet.getString(COLUMN_DEF);
                if (columnDef != null) {
                    final Constraint constraint = new Constraint(ConstraintType.DEFAULT_VALUE);
                    attribute.getConstraints().add(constraint);
                }
            }

            columnsResultSet.close();

            entity.getAttributes().addAll(attributes);

            final ResultSet primaryKeys = databaseMetaData.getPrimaryKeys(this.catalogName, SCHEMA_PATTERN, tableName);

            //Uzimamo primarne kljuceve tabela i stavljamo ogranicenje primarnog kljuca odgovarajucoj koloni
            while (primaryKeys.next()) {
                final String primaryKeyColumnName = primaryKeys.getString(COLUMN_NAME);
                final Attribute attribute = entity.retrieveColumnByName(primaryKeyColumnName);
                final Constraint constraint = new Constraint(ConstraintType.PRIMARY_KEY);
                attribute.getConstraints().add(constraint);
            }

            primaryKeys.close();

            final ResultSet relations = databaseMetaData.getExportedKeys(this.catalogName, SCHEMA_PATTERN, tableName);
            final List<Relation> parentRelations = new ArrayList<>();

            //Uzimamo sve tabele u kojima se nalazi priamrni kljuc trenutne tabele
            while (relations.next()) {
                final String foreignKeyTableName = relations.getString(FKTABLE_NAME);
                final String foreignKeyColumnName = relations.getString(FKCOLUMN_NAME);
                final String primaryKeyColumnName = relations.getString(PKCOLUMN_NAME);
                final Relation relation = new Relation(foreignKeyTableName, foreignKeyColumnName, tableName, primaryKeyColumnName);
                parentRelations.add(relation);
            }

            relations.close();

            final ResultSet foreignKeys = databaseMetaData.getImportedKeys(this.catalogName, SCHEMA_PATTERN, tableName);

            //Uzimamo sve strane kljuceve za trenutnu tabelu i setujemo ogranicenje stranog kljuca odgovarajucoj koloni
            while (foreignKeys.next()) {
                final String foreignKeyColumnName = foreignKeys.getString(FKCOLUMN_NAME);
                final Attribute attribute = entity.retrieveColumnByName(foreignKeyColumnName);
                final Constraint constraint = new Constraint(ConstraintType.FOREIGN_KEY);
                attribute.getConstraints().add(constraint);
            }

            foreignKeys.close();

            entity.getParentRelations().addAll(parentRelations);
            entities.add(entity);
        }

        resultSet.close();

        resource.getEntities().addAll(entities);

        return resource;
    }

    //Daje nam sve kolone iz tabela
    @Override
    public List<Row> query(final String tableName) {
        if (this.connection == null) {
            this.initializeConnection();
        }

        try (final Statement statement = this.connection.createStatement()) {
            final String query = new StringBuilder("SELECT * FROM ").append(tableName).append(";").toString();
            final ResultSet resultSet = statement.executeQuery(query);
            return this.getRowsFromResultSet(resultSet);
        } catch (final SQLException e) {
            JOptionPane.showMessageDialog(null, "Couldn't retrieve data for table " + tableName + "!");
        }
        return new ArrayList<>();
    }

    @Override
    public List<Row> queryWithCondition(final String tableName, final Map<String, Object> conditionColumns) {
        if (this.connection == null) {
            this.initializeConnection();
        }

        if (conditionColumns == null || conditionColumns.isEmpty()) {
            return this.query(tableName);
        }

        final StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ").append(tableName).append(" WHERE ");

        int size = conditionColumns.size();
        for (final String key : conditionColumns.keySet()) {
            queryBuilder.append(key);
            queryBuilder.append(" = ?");

            if (--size > 0) {
                queryBuilder.append(" AND ");
            }
        }
        queryBuilder.append(";");

        try (final PreparedStatement preparedStatement = this.connection.prepareStatement(queryBuilder.toString())) {

            int parametersCounter = 1;
            for (final Map.Entry<String, Object> entry : conditionColumns.entrySet()) {
                preparedStatement.setObject(parametersCounter++, entry.getValue());
            }

            final ResultSet resultSet = preparedStatement.executeQuery();
            return this.getRowsFromResultSet(resultSet);
        } catch (final SQLException e) {
            JOptionPane.showMessageDialog(null, "Couldn't retrieve data for table " + tableName + "!");
        }

        return new ArrayList<>();
    }

    @Override
    public List<Row> queryFilterAndSort(final String tableName, final List<String> filterColumns, final List<Sort> sortColumns) {
        if (this.connection == null) {
            this.initializeConnection();
        }

        try (final Statement statement = this.connection.createStatement()) {
            final StringBuilder queryBuilder = new StringBuilder("SELECT ");

            if (filterColumns.isEmpty()) {
                queryBuilder.append("*");
            } else {
                int size = filterColumns.size();
                for (final String column : filterColumns) {
                    queryBuilder.append(column);
                    if (--size > 0) {
                        queryBuilder.append(", ");
                    }
                }
            }

            queryBuilder.append(" FROM ").append(tableName);

            if (!sortColumns.isEmpty()) {
                queryBuilder.append(" ORDER BY ");
                int size = sortColumns.size();
                for (final Sort sortColumn : sortColumns) {
                    queryBuilder.append(sortColumn.getSortByAttribute().getName());
                    queryBuilder.append(" ");
                    queryBuilder.append(sortColumn.getSortType() == SortType.ASCENDING ? "ASC" : "DESC");
                    if (--size > 0) {
                        queryBuilder.append(", ");
                    }
                }
            }

            queryBuilder.append(";");

            final ResultSet resultSet = statement.executeQuery(queryBuilder.toString());
            return this.getRowsFromResultSet(resultSet);
        } catch (final SQLException e) {
            JOptionPane.showMessageDialog(null, "Couldn't retrieve data for table " + tableName + "!");
        }
        return new ArrayList<>();
    }

    @Override
    public Row insert(final String tableName, final List<Field> fields) {
        if (this.connection == null) {
            this.initializeConnection();
        }

        if (fields == null || fields.isEmpty()) {
            return null;
        }

        final StringBuilder queryBuilder = new StringBuilder("INSERT INTO ").append(tableName).append(" (");

        int size = fields.size();
        final StringBuilder valuesBuilder = new StringBuilder();
        for (final Field field : fields) {
            queryBuilder.append(field.getName());
            valuesBuilder.append("?");

            if (--size > 0) {
                queryBuilder.append(", ");
                valuesBuilder.append(", ");
            }
        }

        queryBuilder.append(") VALUES (");
        queryBuilder.append(valuesBuilder.toString());
        queryBuilder.append(");");

        try (final PreparedStatement preparedStatement = this.connection.prepareStatement(queryBuilder.toString())) {

            int parametersCounter = 1;
            for (final Field field : fields) {
                preparedStatement.setObject(parametersCounter++, field.getValue());
            }

            return this.executeUpdate(tableName, fields, preparedStatement);
        } catch (final SQLException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    private Row executeUpdate(final String tableName, final List<Field> fields, final PreparedStatement preparedStatement) throws SQLException {
        preparedStatement.executeUpdate();

        final Map<String, Object> fieldsMap = fields.stream().collect(Collectors.toMap(field -> field.getName(), field -> field.getValue()));
        final List<Row> rows = this.queryWithCondition(tableName, fieldsMap);
        return rows.get(0);
    }

    @Override
    public Row update(final String tableName, final List<Field> fields, final List<Field> conditionColumns) {
        if (this.connection == null) {
            this.initializeConnection();
        }

        if (fields == null || fields.isEmpty()) {
            return null;
        }

        final StringBuilder queryBuilder = new StringBuilder("UPDATE ").append(tableName).append(" SET ");

        int size = fields.size();
        for (final Field field : fields) {
            queryBuilder.append(field.getName());
            queryBuilder.append(" = ?");

            if (--size > 0) {
                queryBuilder.append(", ");
            }
        }

        queryBuilder.append(" WHERE ");

        size = conditionColumns.size();
        for (final Field field : conditionColumns) {
            queryBuilder.append(field.getName());
            queryBuilder.append(" = ?");

            if (--size > 0) {
                queryBuilder.append(" AND ");
            }
        }

        queryBuilder.append(";");

        try (final PreparedStatement preparedStatement = this.connection.prepareStatement(queryBuilder.toString())) {

            int parametersCounter = 1;
            for (final Field field : fields) {
                preparedStatement.setObject(parametersCounter++, field.getValue());
            }

            for (final Field field : conditionColumns) {
                preparedStatement.setObject(parametersCounter++, field.getValue());
            }

            return this.executeUpdate(tableName, fields, preparedStatement);
        } catch (final SQLException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
        }

        return null;
    }

    @Override
    public boolean delete(final String tableName, final List<Field> conditionColumns) {
        if (this.connection == null) {
            this.initializeConnection();
        }

        if (conditionColumns == null || conditionColumns.isEmpty()) {
            return false;
        }

        final StringBuilder queryBuilder = new StringBuilder("DELETE FROM ").append(tableName).append(" WHERE ");

        int size = conditionColumns.size();
        for (final Field field : conditionColumns) {
            queryBuilder.append(field.getName());
            queryBuilder.append(" = ?");

            if (--size > 0) {
                queryBuilder.append(" AND ");
            }
        }

        queryBuilder.append(";");

        try (final PreparedStatement preparedStatement = this.connection.prepareStatement(queryBuilder.toString())) {

            int parametersCounter = 1;
            for (final Field field : conditionColumns) {
                preparedStatement.setObject(parametersCounter++, field.getValue());
            }
            preparedStatement.executeUpdate();
            return true;
        } catch (final SQLException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    @Override
    public List<Row> groupByQuery(final String tableName, final String aggregateColumn,
                                  final ReportType aggregateType, final String aggregateColumnName, final List<String> groupingColumns) {

        if (this.connection == null) {
            this.initializeConnection();
        }

        if (aggregateColumn == null || groupingColumns == null || groupingColumns.isEmpty() || aggregateColumnName == null) {
            return this.query(tableName);
        }

        try (final Statement statement = this.connection.createStatement()) {
            final StringBuilder queryBuilder = new StringBuilder("SELECT ");

            if (aggregateType == ReportType.AVERAGE) {
                queryBuilder.append("AVG(");
            } else {
                queryBuilder.append("COUNT(");
            }

            queryBuilder.append(aggregateColumn).append(") '").append(aggregateColumnName).append("'");

            for (final String groupingColumn : groupingColumns) {
                queryBuilder.append(", ").append(groupingColumn);
            }

            queryBuilder.append(" FROM ").append(tableName).append(" GROUP BY ");

            int size = groupingColumns.size();
            for (final String groupingColumn : groupingColumns) {
                queryBuilder.append(groupingColumn);

                if (--size > 0) {
                    queryBuilder.append(", ");
                }
            }

            queryBuilder.append(";");

            final ResultSet resultSet = statement.executeQuery(queryBuilder.toString());
            return this.getRowsFromResultSet(resultSet);
        } catch (final SQLException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
        }
        return new ArrayList<>();
    }

    @Override
    public List<Row> search(final String tableName, final List<SearchCondition> searchConditions) {
        if (this.connection == null) {
            this.initializeConnection();
        }

        if (searchConditions == null || searchConditions.isEmpty()) {
            return this.query(tableName);
        }

        final StringBuilder queryBuilder = new StringBuilder("SELECT * FROM ").append(tableName).append(" WHERE ");

        int size = searchConditions.size();
        for (final SearchCondition searchCondition : searchConditions) {
            queryBuilder.append(searchCondition.getColumnName());

            final RelationalOperatorType relationalOperatorType = searchCondition.getRelationalOperatorType();
            if (relationalOperatorType == null) {
                queryBuilder.append(" LIKE ?");
            } else {
                if (relationalOperatorType == RelationalOperatorType.LESS) {
                    queryBuilder.append(" < ?");
                } else if (relationalOperatorType == RelationalOperatorType.GREATER) {
                    queryBuilder.append(" > ?");
                } else {
                    queryBuilder.append(" = ?");
                }
            }

            if (--size > 0) {
                final OperatorType operatorType = searchCondition.getOperatorType();
                if (operatorType == OperatorType.AND) {
                    queryBuilder.append(" AND ");
                } else {
                    queryBuilder.append(" OR ");
                }
            }
        }
        queryBuilder.append(";");

        try (final PreparedStatement preparedStatement = this.connection.prepareStatement(queryBuilder.toString())) {

            int parametersCounter = 1;
            for (final SearchCondition searchCondition : searchConditions) {
                preparedStatement.setObject(parametersCounter++, searchCondition.getValue());
            }

            final ResultSet resultSet = preparedStatement.executeQuery();
            return this.getRowsFromResultSet(resultSet);
        } catch (final SQLException e) {
            JOptionPane.showMessageDialog(null, e.getLocalizedMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
        }

        return new ArrayList<>();
    }

    private List<Row> getRowsFromResultSet(final ResultSet resultSet) throws SQLException {
        final List<Row> rows = new ArrayList<>();
        final ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        while (resultSet.next()) {
            final Row row = new Row();
            for (int i = 1; i <= resultSetMetaData.getColumnCount(); i++) {
                final String columnTypeName = resultSetMetaData.getColumnTypeName(i);
                final AttributeType attributeType = AttributeType.valueOf(columnTypeName.toUpperCase());
                final Class classForAttributeType = Util.getClassForAttributeType(attributeType);
                final Field field = new Field(resultSetMetaData.getColumnName(i), classForAttributeType, classForAttributeType.cast(resultSet.getObject(i)));
                row.getFields().add(field);
            }
            rows.add(row);
        }

        return rows;
    }
}
