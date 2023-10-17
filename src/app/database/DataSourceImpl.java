package app.database;

import app.database.repository.Repository;
import app.model.*;
import app.tree.model.Resource;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DataSourceImpl implements DataSource {

    private final Repository repository;

    public DataSourceImpl(final Repository repository) {
        this.repository = repository;
    }

    @Override
    public void initializeConnection() {
        this.repository.initializeConnection();
    }

    @Override
    public void closeConnection() {
        this.repository.closeConnection();
    }

    @Override
    public Resource loadResource() throws SQLException {
        return this.repository.loadSchema();
    }

    @Override
    public List<Row> getData(final String dataContainer) {
        return this.repository.query(dataContainer);
    }

    @Override
    public List<Row> getDataByRelation(final String dataContainer, final Map<String, Object> conditions) {
        return this.repository.queryWithCondition(dataContainer, conditions);
    }

    @Override
    public List<Row> getFilteredAndSortedData(final String dataContainer, final List<String> filterColumns, final List<Sort> sortColumns) {
        return this.repository.queryFilterAndSort(dataContainer, filterColumns, sortColumns);
    }

    @Override
    public Row insertData(final String dataContainer, final List<Field> data) {
        return this.repository.insert(dataContainer, data);
    }

    @Override
    public Row updateData(final String dataContainer, final List<Field> data, final List<Field> conditions) {
        return this.repository.update(dataContainer, data, conditions);
    }

    @Override
    public boolean deleteData(final String dataContainer, final List<Field> conditions) {
        return this.repository.delete(dataContainer, conditions);
    }

    @Override
    public List<Row> generateReport(final String dataContainer, final String reportUnit, final ReportType reportType, final String reportName, final List<String> reportGroupingConditions) {
        return this.repository.groupByQuery(dataContainer, reportUnit, reportType, reportName, reportGroupingConditions);
    }

    @Override
    public List<Row> searchData(final String dataContainer, final List<SearchCondition> conditions) {
        return this.repository.search(dataContainer, conditions);
    }
}
