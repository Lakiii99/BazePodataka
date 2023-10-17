package app.database;

import app.model.*;
import app.tree.model.Resource;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface DataSource {

    void initializeConnection();

    void closeConnection();

    Resource loadResource() throws SQLException;

    List<Row> getData(String dataContainer);

    List<Row> getDataByRelation(String dataContainer, Map<String, Object> conditions);

    List<Row> getFilteredAndSortedData(String dataContainer, List<String> filterColumns, List<Sort> sortColumns);

    Row insertData(String dataContainer, List<Field> data);

    Row updateData(String dataContainer, List<Field> data, List<Field> conditions);

    boolean deleteData(String dataContainer, List<Field> conditions);

    List<Row> generateReport(String dataContainer, String reportUnit, ReportType reportType, String reportName, List<String> reportGroupingConditions);

    List<Row> searchData(String dataContainer, List<SearchCondition> conditions);
}