package app.tree.model;

import app.ApplicationSingleton;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

@Data
@EqualsAndHashCode(callSuper = true)
public class Resource extends Observable {

    private String title;
    private final List<Entity> entities = new ArrayList<>();

    public Resource() {
        this.initObservers();
    }

    public Resource(final String title) {
        this.title = title;
        this.initObservers();
    }

    private void initObservers() {
        if (ApplicationSingleton.getInstance().getAppMainFrame() != null) {
            this.addObserver(ApplicationSingleton.getInstance().getAppMainFrame().getTree());
        }
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

    public Entity retrieveTableByName(final String tableName) {
        for (final Entity entity : this.entities) {
            if (entity.getTitle().equals(tableName)) {
                return entity;
            }
        }
        return null;
    }
}