/*
 * Created by JFormDesigner on Sat May 30 15:28:38 CEST 2020
 */

package app.table.view;

import app.model.ReportType;
import app.tree.model.Entity;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;


public class ChooseReportTypeDialog extends ChooseTypeDialog<ReportType> {
    private static final long serialVersionUID = -6523327154701167514L;
    private final Entity entity;

    public ChooseReportTypeDialog(final Window owner, final Entity entity) {
        super(owner);
        this.entity = entity;
        this.setTitle("Choose report type");

        final DefaultListModel<ReportType> sortTypeDefaultListModel = new DefaultListModel<>();
        sortTypeDefaultListModel.addElement(ReportType.COUNT);
        sortTypeDefaultListModel.addElement(ReportType.AVERAGE);
        this.typesList.setModel(sortTypeDefaultListModel);
        this.typesList.setSelectedValue(sortTypeDefaultListModel.get(0), true);

        this.okButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -4131772858321997981L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                ChooseReportTypeDialog.this.dispose();
                final GenerateReportDialog generateReportDialog = new GenerateReportDialog(owner, entity, ChooseReportTypeDialog.this.typesList.getSelectedValue());
                generateReportDialog.setVisible(true);
            }
        });
    }
}