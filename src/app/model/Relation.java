package app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Relation {
    private String foreignKeyTableName;
    private String foreignKeyColumnName;
    private String primaryKeyTableName;
    private String primaryKeyColumnName;
}