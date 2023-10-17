package app.controller;

import app.ApplicationSingleton;
import app.table.view.AddUpdateDialog;
import app.tree.model.Entity;
import app.view.AppMainFrame;
import app.view.ButtonsContainer;

import java.awt.event.ActionEvent;

public class OpenAddDialogAction extends AbstractActionWithParent {
    private static final long serialVersionUID = -9017498427724228133L;

    public OpenAddDialogAction(final String name, final ButtonsContainer parent) {
        super(name, parent);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final AppMainFrame appMainFrame = ApplicationSingleton.getInstance().getAppMainFrame();
        final Entity entity = this.parent.getCorrespondingTabsContainer().getSelectedEntity();

        if (entity == null) {
            return;
        }

        final AddUpdateDialog addUpdateDialog = new AddUpdateDialog(appMainFrame, entity);
        addUpdateDialog.setVisible(true);
    }
}