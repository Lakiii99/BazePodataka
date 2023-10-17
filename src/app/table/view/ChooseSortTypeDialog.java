/*
 * Created by JFormDesigner on Sat May 30 15:28:38 CEST 2020
 */

package app.table.view;

import app.model.SortType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ChooseSortTypeDialog extends ChooseTypeDialog<SortType> {
    private static final long serialVersionUID = 6871164518921184772L;

    public ChooseSortTypeDialog(final Window owner) {
        super(owner);
        this.setTitle("Choose sort type");

        final DefaultListModel<SortType> sortTypeDefaultListModel = new DefaultListModel<>();
        sortTypeDefaultListModel.addElement(SortType.ASCENDING);
        sortTypeDefaultListModel.addElement(SortType.DESCENDING);
        this.typesList.setModel(sortTypeDefaultListModel);
        this.typesList.setSelectedValue(sortTypeDefaultListModel.get(0), true);

        this.okButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -4131772858321997981L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                ((FilterAndSortDialog) ChooseSortTypeDialog.this.getOwner()).sortTypeChosen(ChooseSortTypeDialog.this.typesList.getSelectedValue());
                ChooseSortTypeDialog.this.dispose();
            }
        });
    }
}
