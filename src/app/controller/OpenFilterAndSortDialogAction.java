package app.controller;

import app.ApplicationSingleton;
import app.table.view.FilterAndSortDialog;
import app.tree.model.Entity;
import app.view.AppMainFrame;
import app.view.ButtonsContainer;

import java.awt.event.ActionEvent;

public class OpenFilterAndSortDialogAction extends AbstractActionWithParent {
    private static final long serialVersionUID = -253805446483734708L;

    public OpenFilterAndSortDialogAction(final String name, final ButtonsContainer parent) {
        super(name, parent);
    }

    @Override
    public void actionPerformed(final ActionEvent e) {
        final AppMainFrame appMainFrame = ApplicationSingleton.getInstance().getAppMainFrame();
        final Entity entity = this.parent.getCorrespondingTabsContainer().getSelectedEntity();

        if (entity == null) {
            return;
        }

        final FilterAndSortDialog filterAndSortDialog = new FilterAndSortDialog(appMainFrame, entity);
        filterAndSortDialog.setVisible(true);
    }
}