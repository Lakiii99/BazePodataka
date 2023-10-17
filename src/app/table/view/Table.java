package app.table.view;

import app.events.EntityEvent;
import app.model.Row;
import app.table.controller.TableRowSelectionListener;
import app.table.model.TableModel;
import app.tree.model.Entity;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

@Getter
public class Table extends JTable implements Observer {
    private static final long serialVersionUID = -315227941095963800L;
    private final TableModel tableModel;
    private Entity entity;
    private final boolean parent;

    public Table(final Entity entity, final boolean parent) {
        this.entity = entity;
        this.parent = parent;
        this.tableModel = new TableModel(entity.getRows());
        this.setModel(this.tableModel);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.getSelectionModel().addListSelectionListener(new TableRowSelectionListener(this));
        this.getTableHeader().setReorderingAllowed(false);
    }

    public Row getCurrentlySelectedRow() {
        return this.getTableModel().getSelectedRow(this.getSelectedRow());
    }

    @Override
    public void update(final Observable o, final Object arg) {
        if (o instanceof Entity && arg instanceof EntityEvent) {
            final EntityEvent entityEvent = (EntityEvent) arg;
            if (entityEvent == EntityEvent.UPDATE_DATA) {
                this.entity = (Entity) o;
                this.getTableModel().setRows(this.entity.getRows());
            } else if (entityEvent == EntityEvent.ADD_ROW) {
                this.entity = (Entity) o;
                this.getTableModel().setRows(this.entity.getRows());

                final int rowIndex = this.tableModel.getRowCount() - 1;
                this.getSelectionModel().setSelectionInterval(rowIndex, rowIndex);
                this.scrollRectToVisible(new Rectangle(this.getCellRect(rowIndex, 0, true)));
            } else if (entityEvent == EntityEvent.UPDATE_ROW) {
                this.entity = (Entity) o;

                final int lastSelectedRowIndex = this.getSelectedRow();

                this.getTableModel().setRows(this.entity.getRows());

                this.getSelectionModel().setSelectionInterval(lastSelectedRowIndex, lastSelectedRowIndex);
                this.scrollRectToVisible(new Rectangle(this.getCellRect(lastSelectedRowIndex, 0, true)));
            } else if (entityEvent == EntityEvent.DELETE_ROW) {
                this.entity = (Entity) o;
                this.getTableModel().setRows(this.entity.getRows());
                JOptionPane.showMessageDialog(null, "You have deleted selected row successfully!", "Row deleted", JOptionPane.INFORMATION_MESSAGE);
            }
        }
    }
}
