package app.tree.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attribute {
    private String name;
    private AttributeType attributeType;
    private int length;
    private final List<Constraint> constraints = new ArrayList<>();

    @Override
    public String toString() {
        return this.name;
    }
}