package app.database.repository;

import app.model.*;
import app.tree.model.Resource;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface Repository {

    void initializeConnection();

    void closeConnection();

    Resource loadSchema() throws SQLException;

    List<Row> query(String tableName);

    List<Row> queryWithCondition(String tableName, Map<String, Object> conditionColumns);

    List<Row> queryFilterAndSort(String tableName, List<String> filterColumns, List<Sort> sortColumns);

    Row insert(String tableName, List<Field> fields);

    Row update(String tableName, List<Field> fields, List<Field> conditionColumns);

    boolean delete(String tableName, List<Field> conditionColumns);

    List<Row> groupByQuery(String tableName, String aggregateColumn, ReportType aggregateType, String aggregateColumnName, List<String> groupingColumns);

    List<Row> search(String tableName, List<SearchCondition> searchConditions);
}