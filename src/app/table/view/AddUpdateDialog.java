/*
 * Created by JFormDesigner on Sun May 31 18:32:30 CEST 2020
 */

package app.table.view;

import app.ApplicationSingleton;
import app.events.EntityEvent;
import app.model.Field;
import app.model.Row;
import app.tree.model.Attribute;
import app.tree.model.Constraint;
import app.tree.model.ConstraintType;
import app.tree.model.Entity;
import app.util.Util;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddUpdateDialog extends JDialog {
    private static final long serialVersionUID = 8477224892350375799L;
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    private Map<Attribute, JComponent> attributeComponentsMap;
    private Entity entity;
    private List<Field> primaryKeyFields;
    private boolean update;
    private int selectedRowIndex;

    public AddUpdateDialog(final Window owner, final Entity entity) {
        super(owner);
        this.initDialog(entity);
        this.setTitle("Add row - " + this.entity.getTitle());
        this.okButton.setText("ADD");
    }

    public AddUpdateDialog(final Window owner, final Entity entity, final Row row, final int selectedRowIndex) {
        super(owner);
        this.initDialog(entity);
        this.setTitle("Update row - " + this.entity.getTitle());
        this.update = true;
        this.selectedRowIndex = selectedRowIndex;
        this.okButton.setText("UPDATE");

        this.primaryKeyFields = new ArrayList<>();
        final Constraint primaryKeyConstraint = new Constraint(ConstraintType.PRIMARY_KEY);
        for (final Field field : row.getFields()) {
            final Attribute attribute = entity.retrieveColumnByName(field.getName());
            final JComponent component = this.attributeComponentsMap.get(attribute);
            if (field.getType().equals(Boolean.class)) {
                ((JCheckBox) component).setSelected((Boolean) field.getValue());
            } else {
                ((JTextField) component).setText(field.getValue().toString());
            }

            if (attribute.getConstraints().contains(primaryKeyConstraint)) {
                this.primaryKeyFields.add(field);
            }
        }
    }

    private void initDialog(final Entity entity) {
        this.entity = entity;
        this.attributeComponentsMap = new HashMap<>();
        this.initComponents();
        this.setModal(true);
    }

    private void initComponents() {
        this.dialogPane = new JPanel();
        this.contentPanel = new JPanel();
        this.buttonBar = new JPanel();
        this.okButton = new JButton();
        this.cancelButton = new JButton();

        final Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        this.dialogPane.setLayout(new BorderLayout());

        final StringBuilder rows = new StringBuilder();
        for (final Attribute ignored : this.entity.getAttributes()) {
            rows.append("[grow]");
        }

        this.contentPanel.setLayout(new MigLayout(
                "fill,insets 20,hidemode 3,align center center,gap 15 15",
                "[fill][fill][fill][fill][fill][fill][fill]",
                rows.toString()));

        int row = 0;
        for (final Attribute attribute : this.entity.getAttributes()) {
            final JLabel attributeLabel = new JLabel(attribute.getName());

            final Class classForAttributeType = Util.getClassForAttributeType(attribute.getAttributeType());
            final JComponent attributeValueComponent;
            if (classForAttributeType.equals(Boolean.class)) {
                attributeValueComponent = new JCheckBox();
            } else {
                attributeValueComponent = new JTextField();
            }
            this.attributeComponentsMap.put(attribute, attributeValueComponent);

            attributeLabel.setLabelFor(attributeValueComponent);

            this.contentPanel.add(attributeLabel, "cell " + 0 + " " + row);
            this.contentPanel.add(attributeValueComponent, "cell " + 1 + " " + row + " 6 1");
            row++;
        }

        this.dialogPane.add(this.contentPanel, BorderLayout.CENTER);

        this.buttonBar.setLayout(new MigLayout(
                "insets dialog,alignx right",
                "[button,fill]" +
                        "[button,fill]",
                null));

        this.buttonBar.add(this.okButton, "cell 0 0");

        this.okButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 3161985302771648180L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                final List<Field> fields = new ArrayList<>();
                for (final Map.Entry<Attribute, JComponent> entry : AddUpdateDialog.this.attributeComponentsMap.entrySet()) {
                    final Field field = new Field();

                    final Attribute attribute = entry.getKey();
                    field.setName(attribute.getName());
                    field.setType(Util.getClassForAttributeType(attribute.getAttributeType()));

                    final JComponent component = entry.getValue();
                    if (component instanceof JTextField) {
                        field.setValue(((JTextField) component).getText());
                    } else if (component instanceof JCheckBox) {
                        field.setValue(((JCheckBox) component).isSelected());
                    }
                    fields.add(field);
                }

                final Row row;
                final EntityEvent entityEvent;
                if (AddUpdateDialog.this.update) {
                    row = ApplicationSingleton.getInstance().getDataSource().updateData(AddUpdateDialog.this.entity.getTitle(), fields, AddUpdateDialog.this.primaryKeyFields);
                    AddUpdateDialog.this.entity.getRows().set(AddUpdateDialog.this.selectedRowIndex, row);
                    entityEvent = EntityEvent.UPDATE_ROW;
                } else {
                    row = ApplicationSingleton.getInstance().getDataSource().insertData(AddUpdateDialog.this.entity.getTitle(), fields);
                    AddUpdateDialog.this.entity.getRows().add(row);
                    entityEvent = EntityEvent.ADD_ROW;
                }
                if (row != null) {
                    AddUpdateDialog.this.entity.notifyObservers(entityEvent);
                    AddUpdateDialog.this.dispose();
                }
            }
        });

        this.cancelButton.setText("Cancel");
        this.cancelButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 3161985302771648180L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                AddUpdateDialog.this.dispose();
            }
        });
        this.buttonBar.add(this.cancelButton, "cell 1 0");

        this.dialogPane.add(this.buttonBar, BorderLayout.SOUTH);

        contentPane.add(this.dialogPane, BorderLayout.CENTER);
        this.pack();
        this.setLocationRelativeTo(this.getOwner());
    }
}
