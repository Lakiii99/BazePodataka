package app.table.view;

import app.ApplicationSingleton;
import app.events.EntityEvent;
import app.model.Row;
import app.model.Sort;
import app.model.SortType;
import app.tree.model.Attribute;
import app.tree.model.Entity;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collections;

public class FilterAndSortDialog extends JDialog {
    private static final long serialVersionUID = -8642849710785546137L;
    private JList<Attribute> allFilterColumnsList;
    private JButton moveToFilterColumnsListButton;
    private JButton moveBackToAllFilterColumnsListButton;
    private JList<Attribute> filterColumnsList;
    private JList<Attribute> allSortColumnsList;
    private JButton moveToSortColumnsListButton;
    private JButton moveBackToAllSortColumnsListButton;
    private JList<Sort> sortColumnsList;
    private final DefaultListModel<Attribute> allFilterColumnsListModel;
    private final DefaultListModel<Attribute> filterColumnsListModel;
    private final DefaultListModel<Attribute> allSortColumnsListModel;
    private final DefaultListModel<Sort> sortColumnsListModel;
    private final Entity entity;

    public FilterAndSortDialog(final Window owner, final Entity entity) {
        super(owner);
        this.entity = entity;
        this.initComponents();
        this.setTitle("Filter & Sort");
        this.setModal(true);

        this.allFilterColumnsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.filterColumnsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.allSortColumnsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.sortColumnsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.allFilterColumnsListModel = new DefaultListModel<>();
        entity.getAttributes().forEach(attribute -> this.allFilterColumnsListModel.addElement(attribute));
        this.allFilterColumnsList.setModel(this.allFilterColumnsListModel);

        this.filterColumnsListModel = new DefaultListModel<>();
        this.filterColumnsList.setModel(this.filterColumnsListModel);

        this.allSortColumnsListModel = new DefaultListModel<>();
        entity.getAttributes().forEach(attribute -> this.allSortColumnsListModel.addElement(attribute));
        this.allSortColumnsList.setModel(this.allSortColumnsListModel);

        this.sortColumnsListModel = new DefaultListModel<>();
        this.sortColumnsList.setModel(this.sortColumnsListModel);

        this.moveToFilterColumnsListButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 3839354537243733019L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                final Attribute selectedAttribute = FilterAndSortDialog.this.allFilterColumnsList.getSelectedValue();
                if (selectedAttribute != null) {
                    FilterAndSortDialog.this.allFilterColumnsListModel.removeElement(selectedAttribute);
                    FilterAndSortDialog.this.filterColumnsListModel.addElement(selectedAttribute);
                }
            }
        });

        this.moveBackToAllFilterColumnsListButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 3839354537243733019L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                final Attribute selectedAttribute = FilterAndSortDialog.this.filterColumnsList.getSelectedValue();
                if (selectedAttribute != null) {
                    FilterAndSortDialog.this.filterColumnsListModel.removeElement(selectedAttribute);
                    FilterAndSortDialog.this.allFilterColumnsListModel.addElement(selectedAttribute);
                }
            }
        });

        this.moveToSortColumnsListButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 3839354537243733019L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                final ChooseSortTypeDialog chooseSortTypeDialog = new ChooseSortTypeDialog(FilterAndSortDialog.this);
                chooseSortTypeDialog.setVisible(true);
            }
        });

        this.moveBackToAllSortColumnsListButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = 3839354537243733019L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                final Sort selectedSortAttribute = FilterAndSortDialog.this.sortColumnsList.getSelectedValue();
                if (selectedSortAttribute != null) {
                    FilterAndSortDialog.this.sortColumnsListModel.removeElement(selectedSortAttribute);
                    FilterAndSortDialog.this.allSortColumnsListModel.addElement(selectedSortAttribute.getSortByAttribute());
                }
            }
        });
    }

    public void sortTypeChosen(final SortType sortType) {
        final Attribute selectedAttribute = FilterAndSortDialog.this.allSortColumnsList.getSelectedValue();
        if (selectedAttribute != null) {
            this.allSortColumnsListModel.removeElement(selectedAttribute);
            this.sortColumnsListModel.addElement(new Sort(selectedAttribute, sortType));
        }
    }

    private void initComponents() {
        final JPanel dialogPane = new JPanel();
        final JPanel contentPanel = new JPanel();
        final JScrollPane allFilterColumnsScrollPane = new JScrollPane();
        this.allFilterColumnsList = new JList<>();
        final JPanel filterColumnsListsPanel = new JPanel();
        this.moveToFilterColumnsListButton = new JButton();
        this.moveBackToAllFilterColumnsListButton = new JButton();
        final JScrollPane filterColumnsScrollPane = new JScrollPane();
        this.filterColumnsList = new JList<>();
        final JScrollPane allSortColumnsScrollPane = new JScrollPane();
        this.allSortColumnsList = new JList<>();
        final JPanel sortColumnsListsPanel = new JPanel();
        this.moveToSortColumnsListButton = new JButton();
        this.moveBackToAllSortColumnsListButton = new JButton();
        final JScrollPane sortColumnsScrollPane = new JScrollPane();
        this.sortColumnsList = new JList<>();
        final JPanel buttonBar = new JPanel();
        final JButton confirmButton = new JButton();
        final JButton cancelButton = new JButton();
        final JLabel allFilterColumnsListLabel = new JLabel();
        final JLabel filterColumnsListLabel = new JLabel();
        final JLabel allSortColumnsListLabel = new JLabel();
        final JLabel sortColumnsListLabel = new JLabel();

        final Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        dialogPane.setLayout(new BorderLayout());

        contentPanel.setLayout(new MigLayout(
                "fill,insets 10 15 10 15,hidemode 3,align center center,gap 20 20",
                "[fill]" +
                        "[fill]" +
                        "[fill]" +
                        "[center]" +
                        "[fill]" +
                        "[fill]" +
                        "[fill]",
                "[]" +
                        "[]" +
                        "[]" +
                        "[]"));

        allFilterColumnsListLabel.setText("Columns available for filtering");
        allFilterColumnsListLabel.setLabelFor(allFilterColumnsScrollPane);
        contentPanel.add(allFilterColumnsListLabel, "cell 0 0 3 1,align center center,grow 0 0");

        allFilterColumnsScrollPane.setViewportView(this.allFilterColumnsList);

        contentPanel.add(allFilterColumnsScrollPane, "cell 0 1 3 1");

        filterColumnsListsPanel.setLayout(new MigLayout(
                "fill,insets 5,hidemode 3,align center center",

                "[fill]",

                "[]" +
                        "[]"));

        this.moveToFilterColumnsListButton.setText(">");
        filterColumnsListsPanel.add(this.moveToFilterColumnsListButton, "cell 0 0");

        this.moveBackToAllFilterColumnsListButton.setText("<");
        filterColumnsListsPanel.add(this.moveBackToAllFilterColumnsListButton, "cell 0 1");

        contentPanel.add(filterColumnsListsPanel, "cell 3 1");

        filterColumnsListLabel.setText("Filter columns");
        filterColumnsListLabel.setLabelFor(filterColumnsScrollPane);
        contentPanel.add(filterColumnsListLabel, "cell 4 0 3 1,align center center,grow 0 0");

        filterColumnsScrollPane.setViewportView(this.filterColumnsList);

        contentPanel.add(filterColumnsScrollPane, "cell 4 1 3 1");

        allSortColumnsListLabel.setText("Columns available for sorting");
        allSortColumnsListLabel.setLabelFor(allSortColumnsScrollPane);
        contentPanel.add(allSortColumnsListLabel, "cell 0 2 3 1,align center center,grow 0 0");

        allSortColumnsScrollPane.setViewportView(this.allSortColumnsList);

        contentPanel.add(allSortColumnsScrollPane, "cell 0 3 3 1");

        sortColumnsListsPanel.setLayout(new MigLayout(
                "fill,insets 5,hidemode 3,align center center",
                "[fill]",
                "[]" +
                        "[]"));

        this.moveToSortColumnsListButton.setText(">");
        sortColumnsListsPanel.add(this.moveToSortColumnsListButton, "cell 0 0");

        this.moveBackToAllSortColumnsListButton.setText("<");
        sortColumnsListsPanel.add(this.moveBackToAllSortColumnsListButton, "cell 0 1");

        contentPanel.add(sortColumnsListsPanel, "cell 3 3");

        sortColumnsListLabel.setText("Sort columns");
        sortColumnsListLabel.setLabelFor(sortColumnsScrollPane);
        contentPanel.add(sortColumnsListLabel, "cell 4 2 3 1,align center center,grow 0 0");

        sortColumnsScrollPane.setViewportView(this.sortColumnsList);

        contentPanel.add(sortColumnsScrollPane, "cell 4 3 3 1");

        dialogPane.add(contentPanel, BorderLayout.CENTER);

        buttonBar.setLayout(new MigLayout(
                "insets dialog,alignx right",
                "[button,fill]" +
                        "[button,fill]",
                null));

        confirmButton.setText("CONFIRM");
        confirmButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -8420023668656380302L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                final java.util.List<Attribute> filterColumnsAttributes = Collections.list(FilterAndSortDialog.this.filterColumnsListModel.elements());
                final java.util.List<String> filterColumns = new ArrayList<>();
                for (final Attribute attribute : filterColumnsAttributes) {
                    filterColumns.add(attribute.getName());
                }

                final java.util.List<Sort> sortColumnsAttributes = Collections.list(FilterAndSortDialog.this.sortColumnsListModel.elements());
                final java.util.List<Row> filteredAndSortedRows = ApplicationSingleton.getInstance()
                        .getDataSource()
                        .getFilteredAndSortedData(FilterAndSortDialog.this.entity.getTitle(), filterColumns, sortColumnsAttributes);

                FilterAndSortDialog.this.entity.setRows(filteredAndSortedRows);
                FilterAndSortDialog.this.entity.notifyObservers(EntityEvent.UPDATE_DATA);

                FilterAndSortDialog.this.dispose();
            }
        });
        buttonBar.add(confirmButton, "cell 0 0");

        cancelButton.setText("CANCEL");
        cancelButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -8420023668656380302L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                FilterAndSortDialog.this.dispose();
            }
        });
        buttonBar.add(cancelButton, "cell 1 0");
        dialogPane.add(buttonBar, BorderLayout.SOUTH);
        contentPane.add(dialogPane, BorderLayout.CENTER);
        this.pack();
        this.setLocationRelativeTo(this.getOwner());
    }
}