package app.controller;

import app.ApplicationSingleton;
import app.events.EntityEvent;
import app.model.Field;
import app.model.Row;
import app.tree.model.Attribute;
import app.tree.model.Constraint;
import app.tree.model.ConstraintType;
import app.tree.model.Entity;
import app.view.ButtonsContainer;
import app.view.TableView;

import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class DeleteRowAction extends AbstractActionWithParent {
    private static final long serialVersionUID = -9017498427724228133L;

    public DeleteRowAction(final String name, final ButtonsContainer parent) {
        super(name, parent);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final Entity entity = this.parent.getCorrespondingTabsContainer().getSelectedEntity();

        if (entity == null) {
            return;
        }

        final TableView tableView = this.parent.getCorrespondingTabsContainer().getTabs().get(entity.getTitle());
        final Row selectedRow = tableView.getTable().getCurrentlySelectedRow();
        final int selectedRowIndex = tableView.getTable().getSelectedRow();

        if (selectedRow == null) {
            return;
        }

        final List<Field> primaryKeyFields = new ArrayList<>();
        final Constraint primaryKeyConstraint = new Constraint(ConstraintType.PRIMARY_KEY);
        for (final Field field : selectedRow.getFields()) {
            final Attribute attribute = entity.retrieveColumnByName(field.getName());
            if (attribute.getConstraints().contains(primaryKeyConstraint)) {
                primaryKeyFields.add(field);
            }
        }

        if (ApplicationSingleton.getInstance().getDataSource().deleteData(entity.getTitle(), primaryKeyFields)) {
            entity.getRows().remove(selectedRowIndex);
            entity.notifyObservers(EntityEvent.DELETE_ROW);
        }
    }
}