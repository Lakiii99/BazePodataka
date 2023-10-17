package app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchCondition {
    private String columnName;
    private OperatorType operatorType;
    private Object value;
    private RelationalOperatorType relationalOperatorType;
}