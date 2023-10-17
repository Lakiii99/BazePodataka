/*
 * Created by JFormDesigner on Sat May 30 15:28:38 CEST 2020
 */

package app.table.view;

import app.model.OperatorType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class ChooseOperatorTypeDialog extends ChooseTypeDialog<OperatorType> {
    private static final long serialVersionUID = 6871164518921184772L;

    public ChooseOperatorTypeDialog(final Window owner) {
        super(owner);
        this.setTitle("Choose operator type");

        final DefaultListModel<OperatorType> operatorTypeDefaultListModel = new DefaultListModel<>();
        operatorTypeDefaultListModel.addElement(OperatorType.AND);
        operatorTypeDefaultListModel.addElement(OperatorType.OR);
        this.typesList.setModel(operatorTypeDefaultListModel);
        this.typesList.setSelectedValue(operatorTypeDefaultListModel.get(0), true);

        this.okButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -4131772858321997981L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                ((SearchDialog) ChooseOperatorTypeDialog.this.getOwner()).newConditionAdded(ChooseOperatorTypeDialog.this.typesList.getSelectedValue());
                ChooseOperatorTypeDialog.this.dispose();
            }
        });
    }
}
