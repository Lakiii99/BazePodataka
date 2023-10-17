package app.view;

import app.table.view.Table;
import app.tree.model.Entity;

import javax.swing.*;

public class TableView extends JScrollPane {
    private static final long serialVersionUID = 2294593865036326407L;
    private final String title;
    private final Table table;

    public TableView(final Entity entity, final boolean parent) {
        this.title = entity.getTitle();
        this.table = new Table(entity, parent);
        this.setViewportView(this.table);
    }

    public String getTitle() {
        return this.title;
    }

    public Table getTable() {
        return this.table;
    }
}