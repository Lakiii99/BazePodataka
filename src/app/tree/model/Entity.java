package app.tree.model;

import app.model.Relation;
import app.model.Row;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class Entity extends Observable {
    private String title;
    private final List<Attribute> attributes = new ArrayList<>();
    private List<Row> rows = new ArrayList<>();
    private final List<Relation> parentRelations = new ArrayList<>();

    public Entity(final String title) {
        this.title = title;
    }

    public Entity(final Entity entity) {
        this.title = entity.getTitle();
        this.getAttributes().addAll(entity.getAttributes());
        this.getParentRelations().addAll(entity.getParentRelations());
    }

    @Override
    public String toString() {
        return this.title;
    }

    @Override
    public void notifyObservers(final Object arg) {
        this.setChanged();
        super.notifyObservers(arg);
    }

    public Attribute retrieveColumnByName(final String columnName) {
        for (final Attribute attribute : this.attributes) {
            if (attribute.getName().equals(columnName)) {
                return attribute;
            }
        }
        return null;
    }
}