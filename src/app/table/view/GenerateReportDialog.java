/*
 * Created by JFormDesigner on Mon Jun 01 18:50:25 CEST 2020
 */

package app.table.view;

import app.ApplicationSingleton;
import app.events.EntityEvent;
import app.model.ReportType;
import app.model.Row;
import app.tree.model.Attribute;
import app.tree.model.Entity;
import app.util.Util;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GenerateReportDialog extends JDialog {
    private static final long serialVersionUID = 2794805303024187034L;
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JLabel reportNameLabel;
    private JTextField reportNameTextField;
    private JLabel reportColumnListLabel;
    private JLabel allColumnsListLabel;
    private JLabel groupByColumnsListLabel;
    private JScrollPane reportColumnListContainer;
    private JList<Attribute> reportColumnList;
    private JScrollPane allColumnsListContainer;
    private JList<Attribute> allColumnsList;
    private JPanel moveButtonsPanel;
    private JButton moveColumnToGroupByListButton;
    private JButton moveColumnToAllColumnsListButton;
    private JScrollPane groupByColumnsListContainer;
    private JList<Attribute> groupByColumnsList;
    private JPanel buttonBar;
    private JButton okButton;
    private JButton cancelButton;
    private final DefaultListModel<Attribute> reportColumnListModel;
    private final DefaultListModel<Attribute> allColumnsListModel;
    private final DefaultListModel<Attribute> groupByColumnsListModel;
    private final Entity entity;
    private final ReportType reportType;

    public GenerateReportDialog(final Window owner, final Entity entity, final ReportType reportType) {
        super(owner);
        this.entity = entity;
        this.reportType = reportType;
        this.initComponents();
        this.setTitle("Generate Report");

        this.reportColumnList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.allColumnsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.groupByColumnsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.reportColumnListModel = new DefaultListModel<>();

        if (reportType == ReportType.AVERAGE) {
            entity.getAttributes()
                    .stream()
                    .filter(attribute -> Util.checkIfAttributeTypeIsNumeric(attribute.getAttributeType()))
                    .forEach(attribute -> this.reportColumnListModel.addElement(attribute));
        } else {
            entity.getAttributes().forEach(attribute -> this.reportColumnListModel.addElement(attribute));
        }

        this.reportColumnList.setModel(this.reportColumnListModel);

        this.allColumnsListModel = new DefaultListModel<>();
        entity.getAttributes().forEach(attribute -> this.allColumnsListModel.addElement(attribute));
        this.allColumnsList.setModel(this.allColumnsListModel);

        this.groupByColumnsListModel = new DefaultListModel<>();
        this.groupByColumnsList.setModel(this.groupByColumnsListModel);

        this.moveColumnToGroupByListButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 3839354537243733019L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                final Attribute selectedAttribute = GenerateReportDialog.this.allColumnsList.getSelectedValue();
                if (selectedAttribute != null) {
                    GenerateReportDialog.this.allColumnsListModel.removeElement(selectedAttribute);
                    GenerateReportDialog.this.groupByColumnsListModel.addElement(selectedAttribute);
                }
            }
        });

        this.moveColumnToAllColumnsListButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 3839354537243733019L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                final Attribute selectedAttribute = GenerateReportDialog.this.groupByColumnsList.getSelectedValue();
                if (selectedAttribute != null) {
                    GenerateReportDialog.this.groupByColumnsListModel.removeElement(selectedAttribute);
                    GenerateReportDialog.this.allColumnsListModel.addElement(selectedAttribute);
                }
            }
        });
    }

    private void initComponents() {
        this.dialogPane = new JPanel();
        this.contentPanel = new JPanel();
        this.reportNameLabel = new JLabel();
        this.reportNameTextField = new JTextField();
        this.reportColumnListLabel = new JLabel();
        this.allColumnsListLabel = new JLabel();
        this.groupByColumnsListLabel = new JLabel();
        this.reportColumnListContainer = new JScrollPane();
        this.reportColumnList = new JList<>();
        this.allColumnsListContainer = new JScrollPane();
        this.allColumnsList = new JList<>();
        this.moveButtonsPanel = new JPanel();
        this.moveColumnToGroupByListButton = new JButton();
        this.moveColumnToAllColumnsListButton = new JButton();
        this.groupByColumnsListContainer = new JScrollPane();
        this.groupByColumnsList = new JList<>();
        this.buttonBar = new JPanel();
        this.okButton = new JButton();
        this.cancelButton = new JButton();

        final Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        this.dialogPane.setLayout(new BorderLayout());

        this.contentPanel.setLayout(new MigLayout(
                "fill,insets 10 15 10 15,hidemode 3,align center center,gap 15 15",
                "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]",
                "[]" +
                        "[]" +
                        "[]"));

        this.reportNameLabel.setText("Report name");
        this.reportNameLabel.setLabelFor(this.reportNameTextField);
        this.contentPanel.add(this.reportNameLabel, "cell 0 0 3 1,align center center,grow 0 0");
        this.contentPanel.add(this.reportNameTextField, "cell 3 0 7 1");

        this.reportColumnListLabel.setText("Column for report");
        this.reportColumnListLabel.setLabelFor(this.reportColumnList);
        this.contentPanel.add(this.reportColumnListLabel, "cell 0 1 3 1,align center center,grow 0 0");

        this.allColumnsListLabel.setText("All columns");
        this.allColumnsListLabel.setLabelFor(this.allColumnsList);
        this.contentPanel.add(this.allColumnsListLabel, "cell 3 1 3 1,align center center,grow 0 0");

        this.groupByColumnsListLabel.setText("Group by columns");
        this.groupByColumnsListLabel.setLabelFor(this.groupByColumnsList);
        this.contentPanel.add(this.groupByColumnsListLabel, "cell 7 1 3 1,align center center,grow 0 0");

        this.reportColumnListContainer.setViewportView(this.reportColumnList);

        this.contentPanel.add(this.reportColumnListContainer, "cell 0 2 3 1");

        this.allColumnsListContainer.setViewportView(this.allColumnsList);

        this.contentPanel.add(this.allColumnsListContainer, "cell 3 2 3 1");

        this.moveButtonsPanel.setLayout(new MigLayout(
                "fill,insets 5,hidemode 3,align center center,gap 10 10",
                "[fill]",
                "[]" +
                        "[]"));

        this.moveColumnToGroupByListButton.setText(">");
        this.moveButtonsPanel.add(this.moveColumnToGroupByListButton, "cell 0 0");

        this.moveColumnToAllColumnsListButton.setText("<");
        this.moveButtonsPanel.add(this.moveColumnToAllColumnsListButton, "cell 0 1");

        this.contentPanel.add(this.moveButtonsPanel, "cell 6 2");

        this.groupByColumnsListContainer.setViewportView(this.groupByColumnsList);

        this.contentPanel.add(this.groupByColumnsListContainer, "cell 7 2 3 1");

        this.dialogPane.add(this.contentPanel, BorderLayout.CENTER);

        this.buttonBar.setLayout(new MigLayout(
                "insets dialog,alignx right",
                "[button,fill]" +
                        "[button,fill]",
                null));

        this.okButton.setText("GENERATE");
        this.okButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 6742356121118580574L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (GenerateReportDialog.this.reportNameTextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Report name has to be set!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (GenerateReportDialog.this.reportColumnList.getSelectedValue() == null) {
                    JOptionPane.showMessageDialog(null, "Report column has to be chosen!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                } else if (GenerateReportDialog.this.groupByColumnsListModel.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "Please select one or more group by columns!", "Error!", JOptionPane.ERROR_MESSAGE);
                    return;
                }

                final List<String> groupByColumns = Collections.list(GenerateReportDialog.this.groupByColumnsListModel.elements()).stream()
                        .map(attribute -> attribute.getName())
                        .collect(Collectors.toList());

                final List<Row> rows = ApplicationSingleton.getInstance().getDataSource().generateReport(GenerateReportDialog.this.entity.getTitle(),
                        GenerateReportDialog.this.reportColumnList.getSelectedValue().getName(),
                        GenerateReportDialog.this.reportType,
                        GenerateReportDialog.this.reportNameTextField.getText(),
                        groupByColumns);

                GenerateReportDialog.this.entity.setRows(rows);
                GenerateReportDialog.this.entity.notifyObservers(EntityEvent.UPDATE_DATA);

                GenerateReportDialog.this.dispose();
            }
        });
        this.buttonBar.add(this.okButton, "cell 0 0");

        this.cancelButton.setText("CANCEL");
        this.cancelButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 6742356121118580574L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                GenerateReportDialog.this.dispose();
            }
        });
        this.buttonBar.add(this.cancelButton, "cell 1 0");

        this.dialogPane.add(this.buttonBar, BorderLayout.SOUTH);

        contentPane.add(this.dialogPane, BorderLayout.CENTER);
        this.pack();
        this.setLocationRelativeTo(this.getOwner());
    }
}