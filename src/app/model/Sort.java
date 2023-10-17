package app.model;

import app.tree.model.Attribute;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Sort {

    private Attribute sortByAttribute;
    private SortType sortType;

    @Override
    public String toString() {
        return this.sortByAttribute.getName() + " " + this.sortType;
    }
}