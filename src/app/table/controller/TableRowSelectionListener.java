package app.table.controller;

import app.ApplicationSingleton;
import app.events.EntityEvent;
import app.model.Relation;
import app.model.Row;
import app.table.view.Table;
import app.tree.model.Entity;
import app.view.TableView;
import lombok.AllArgsConstructor;

import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@AllArgsConstructor
public class TableRowSelectionListener implements ListSelectionListener {

    private final Table table;

    @Override
    public void valueChanged(final ListSelectionEvent e) {
        if (e.getValueIsAdjusting() || !this.table.isParent()) {
            return;
        }
        final int selectedRow = this.table.getSelectedRow();
        final Map<String, Map<String, Object>> relationsMapChild = new HashMap<>();
        for (final Relation relation : this.table.getEntity().getParentRelations()) {
            final String foreignKeyTableName = relation.getForeignKeyTableName();
            if (!relationsMapChild.containsKey(foreignKeyTableName)) {
                final Map<String, Object> foreignKeyFields = new HashMap<>();
                relationsMapChild.put(foreignKeyTableName, foreignKeyFields);
            }
            final String foreignKeyColumnName = relation.getForeignKeyColumnName();

            if (selectedRow != -1) {
                final String primaryKeyColumnName = relation.getPrimaryKeyColumnName();
                final Object value = this.table.getTableModel().getValueAtByColumnName(selectedRow, primaryKeyColumnName);
                relationsMapChild.get(foreignKeyTableName).put(foreignKeyColumnName, value);
            } else {
                relationsMapChild.get(foreignKeyTableName).put(foreignKeyColumnName, null);
            }
        }

        relationsMapChild.forEach((key, value) -> {
            final TableView tableView = ApplicationSingleton.getInstance().getAppMainFrame().getMainPanel().getBottomContainer().getTabsContainer().getTabs().get(key);
            final List<Row> rows;
            if (selectedRow != -1) {
                rows = ApplicationSingleton.getInstance().getDataSource().getDataByRelation(key, value);
            } else {
                rows = ApplicationSingleton.getInstance().getDataSource().getData(key);
            }
            final Table table = tableView.getTable();
            final Entity entity = table.getEntity();
            entity.setRows(rows);
            entity.notifyObservers(EntityEvent.UPDATE_DATA);
        });
    }
}
