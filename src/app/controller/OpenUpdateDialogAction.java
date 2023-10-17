package app.controller;

import app.ApplicationSingleton;
import app.model.Row;
import app.table.view.AddUpdateDialog;
import app.tree.model.Entity;
import app.view.AppMainFrame;
import app.view.ButtonsContainer;
import app.view.TableView;

import java.awt.event.ActionEvent;

public class OpenUpdateDialogAction extends AbstractActionWithParent {
    private static final long serialVersionUID = -9017498427724228133L;

    public OpenUpdateDialogAction(final String name, final ButtonsContainer parent) {
        super(name, parent);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final AppMainFrame appMainFrame = ApplicationSingleton.getInstance().getAppMainFrame();
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

        final AddUpdateDialog addUpdateDialog = new AddUpdateDialog(appMainFrame, entity, selectedRow, selectedRowIndex);
        addUpdateDialog.setVisible(true);
    }
}