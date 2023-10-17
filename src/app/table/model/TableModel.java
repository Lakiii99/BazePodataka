package app.table.model;

import app.model.Field;
import app.model.Row;

import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;

public class TableModel extends DefaultTableModel {
    private static final long serialVersionUID = 1698286037792189174L;
    private List<Row> rows;

    public TableModel(final List<Row> rows) {
        this.setRows(rows);
    }

    @Override
    public Class<?> getColumnClass(final int arg0) {
        if (this.rows == null) this.rows = new ArrayList<>();
        for (final Row row : this.rows) {
            if (row.getFields().get(arg0) != null) {
                return row.getFields().get(arg0).getType();
            }
        }
        return null;
    }

    @Override
    public boolean isCellEditable(final int row, final int column) {
        return false;
    }

    @Override
    public int getRowCount() {
        if (this.rows == null) this.rows = new ArrayList<>();
        return this.rows.size();
    }

    @Override
    public int getColumnCount() {
        if (this.rows == null) this.rows = new ArrayList<>();
        if (this.rows.size() == 0) return 0;
        return this.rows.get(0).getFields().size();
    }

    @Override
    public Object getValueAt(final int row, final int column) {
        if (this.rows == null) {
            this.rows = new ArrayList<>();
            return null;
        }

        final Field field = this.rows.get(row).getFields().get(column);
        if (field == null || field.getValue() == null) {
            return null;
        }

        return field.getValue();
    }

    public Row getSelectedRow(final int selectedRowIndex) {
        if (this.rows == null) this.rows = new ArrayList<>();
        if (selectedRowIndex == -1 || this.rows.isEmpty()) {
            return null;
        }

        return this.rows.get(selectedRowIndex);
    }

    public Object getValueAtByColumnName(final int row, final String columnName) {
        if (this.rows == null) {
            this.rows = new ArrayList<>();
            return null;
        }

        final List<Field> fields = this.rows.get(row).getFields();
        for (final Field field : fields) {
            if (field.getName().equals(columnName)) {
                return field.getValue();
            }
        }

        return null;
    }

    public void setRows(final List<Row> rows) {
        this.rows = new ArrayList<>(rows);

        if (!rows.isEmpty()) {
            final List<Field> fields = rows.get(0).getFields();
            final List<Object> columnIdentifiers = new ArrayList<>();
            for (final Field field : fields) {
                columnIdentifiers.add(field.getName());
            }
            this.setColumnIdentifiers(columnIdentifiers.toArray());
        }

        this.fireTableDataChanged();
    }
}