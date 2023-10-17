/*
 * Created by JFormDesigner on Tue Jun 02 21:35:01 CEST 2020
 */

package app.table.view;

import app.ApplicationSingleton;
import app.events.EntityEvent;
import app.model.OperatorType;
import app.model.RelationalOperatorType;
import app.model.Row;
import app.model.SearchCondition;
import app.tree.model.Attribute;
import app.tree.model.AttributeType;
import app.tree.model.Entity;
import app.util.SearchConditionStringFilter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

public class SearchDialog extends JDialog {

    private static final long serialVersionUID = 877314529182702088L;
    private JScrollPane scrollPane;
    private JPanel scrollPaneContentPanel;
    private JLabel columnsListLabel;
    private JButton addSearchConditionButton;
    private JScrollPane columnsListContainer;
    private MigLayout scrollPanelContentPanelLayout;
    private JList<Attribute> columnsList;
    private final DefaultListModel<Attribute> columnsListModel;
    private final Entity entity;
    private boolean firstColumnAdded;
    private int rowsNumber = 3;
    private final List<SearchCondition> searchConditions = new ArrayList<>();
    private final List<JComponent> valueComponents = new ArrayList<>();

    public SearchDialog(final Window owner, final Entity entity) {
        super(owner);
        this.entity = entity;
        this.initComponents();
        this.setModal(true);

        this.columnsList.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        this.columnsListModel = new DefaultListModel<>();
        entity.getAttributes()
                .stream()
                .filter(attribute -> attribute.getAttributeType() == AttributeType.VARCHAR ||
                        attribute.getAttributeType() == AttributeType.CHAR ||
                        attribute.getAttributeType() == AttributeType.NUMERIC ||
                        attribute.getAttributeType() == AttributeType.INT
                )
                .forEach(attribute -> this.columnsListModel.addElement(attribute));
        this.columnsList.setModel(this.columnsListModel);

        this.addSearchConditionButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -2277791351071982469L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                if (SearchDialog.this.columnsList.getSelectedValue() == null) {
                    return;
                }
                if (!SearchDialog.this.firstColumnAdded) {
                    SearchDialog.this.newConditionAdded(null);
                    SearchDialog.this.firstColumnAdded = true;
                } else {
                    final ChooseOperatorTypeDialog chooseOperatorTypeDialog = new ChooseOperatorTypeDialog(SearchDialog.this);
                    chooseOperatorTypeDialog.setVisible(true);
                }
            }
        });
    }

    public void newConditionAdded(final OperatorType operatorType) {

        String rowConstraints = (String) this.scrollPanelContentPanelLayout.getRowConstraints();

        if (operatorType != null) {
            rowConstraints += "[]";
            this.scrollPaneContentPanel.add(new JLabel(operatorType.toString()), "cell 0 " + this.rowsNumber + " 3 1,align center center,grow 0 0");
            this.rowsNumber++;
        }

        final Attribute attribute = this.columnsList.getSelectedValue();

        rowConstraints += "[]";
        this.scrollPaneContentPanel.add(new JLabel(attribute.getName()), "cell 0 " + this.rowsNumber + ",align center center,grow 0 0");

        final JComponent component;
        if (attribute.getAttributeType() == AttributeType.VARCHAR || attribute.getAttributeType() == AttributeType.CHAR) {
            component = new JTextField();
            ((AbstractDocument) ((JTextField) component).getDocument()).setDocumentFilter(new SearchConditionStringFilter());
        } else {
            component = new NumericSearchConditionValuePanel();
        }

        this.scrollPaneContentPanel.add(component, "cell 1 " + this.rowsNumber + " 2 1,align center center");
        this.valueComponents.add(component);

        this.rowsNumber++;
        this.scrollPanelContentPanelLayout.setRowConstraints(rowConstraints);


        final SearchCondition searchCondition = new SearchCondition();
        searchCondition.setColumnName(attribute.getName());

        if (!this.searchConditions.isEmpty()) {
            this.searchConditions.get(this.searchConditions.size() - 1).setOperatorType(operatorType);
        }

        this.searchConditions.add(searchCondition);

        SwingUtilities.updateComponentTreeUI(this);

        final JScrollBar vertical = this.scrollPane.getVerticalScrollBar();
        vertical.setValue(vertical.getMaximum());
    }

    private void initComponents() {
        final JPanel dialogPane = new JPanel();
        final JPanel contentPanel = new JPanel();
        this.scrollPane = new JScrollPane();
        this.scrollPaneContentPanel = new JPanel();
        this.columnsListLabel = new JLabel();
        this.addSearchConditionButton = new JButton();
        this.columnsListContainer = new JScrollPane();
        this.columnsList = new JList<>();
        final JPanel buttonBar = new JPanel();
        final JButton searchButton = new JButton();
        final JButton cancelButton = new JButton();

        this.scrollPane.getVerticalScrollBar().setUnitIncrement(20);

        this.setTitle("Search dialog");
        final Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        contentPanel.setLayout(new BorderLayout(15, 10));

        this.scrollPanelContentPanelLayout = new MigLayout(
                "fill,hidemode 3,align center center,gap 10 10",
                "[fill]" +
                        "[fill]" +
                        "[fill]",
                "[]" +
                        "[]" +
                        "[]");
        this.scrollPaneContentPanel.setLayout(this.scrollPanelContentPanelLayout);

        this.columnsListLabel.setText("Columns list");
        this.columnsListLabel.setLabelFor(this.columnsList);
        this.scrollPaneContentPanel.add(this.columnsListLabel, "cell 0 0 3 1,align center bottom,grow 0 0");

        this.addSearchConditionButton.setText("ADD SEARCH CONDITION");
        this.scrollPaneContentPanel.add(this.addSearchConditionButton, "cell 0 2 3 1,align center top,grow 0 0");

        this.columnsListContainer.setViewportView(this.columnsList);

        this.scrollPaneContentPanel.add(this.columnsListContainer, "cell 0 1 3 1");

        this.scrollPane.setMinimumSize(new Dimension(400, 300));
        this.scrollPane.setPreferredSize(new Dimension(400, 300));
        this.scrollPane.setMaximumSize(new Dimension(400, 300));
        this.scrollPane.setViewportView(this.scrollPaneContentPanel);

        contentPanel.add(this.scrollPane, BorderLayout.CENTER);

        dialogPane.add(contentPanel, BorderLayout.CENTER);

        buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
        buttonBar.setLayout(new GridBagLayout());
        ((GridBagLayout) buttonBar.getLayout()).columnWidths = new int[]{0, 85, 80};
        ((GridBagLayout) buttonBar.getLayout()).columnWeights = new double[]{1.0, 0.0, 0.0};

        searchButton.setText("SEARCH");
        searchButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -2277791351071982469L;

            @Override
            public void actionPerformed(final ActionEvent e) {

                for (int i = 0; i < SearchDialog.this.searchConditions.size(); i++) {
                    final SearchCondition searchCondition = SearchDialog.this.searchConditions.get(i);
                    final JComponent component = SearchDialog.this.valueComponents.get(i);
                    if (component instanceof JTextField) {
                        final String text = ((JTextField) component).getText();
                        searchCondition.setValue(text);
                    } else {
                        final NumericSearchConditionValuePanel numericSearchConditionValuePanel = (NumericSearchConditionValuePanel) component;
                        final RelationalOperatorType selectedValue = numericSearchConditionValuePanel.getOperatorTypeList().getSelectedValue();
                        final String text = numericSearchConditionValuePanel.getSearchConditionValue().getText();
                        searchCondition.setValue(text);
                        searchCondition.setRelationalOperatorType(selectedValue);
                    }
                }

                final List<Row> rows = ApplicationSingleton.getInstance().getDataSource().searchData(SearchDialog.this.entity.getTitle(), SearchDialog.this.searchConditions);
                SearchDialog.this.entity.setRows(rows);
                SearchDialog.this.entity.notifyObservers(EntityEvent.UPDATE_DATA);

                SearchDialog.this.dispose();
            }
        });
        buttonBar.add(searchButton, new GridBagConstraints(1, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 10, 10), 0, 0));

        cancelButton.setText("CANCEL");
        cancelButton.addActionListener(new AbstractAction() {
            private static final long serialVersionUID = -2277791351071982469L;

            @Override
            public void actionPerformed(final ActionEvent e) {
                SearchDialog.this.dispose();
            }
        });
        buttonBar.add(cancelButton, new GridBagConstraints(2, 1, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 10, 10), 0, 0));

        contentPane.add(buttonBar, BorderLayout.SOUTH);

        contentPane.add(dialogPane, BorderLayout.CENTER);
        this.pack();
        this.setLocationRelativeTo(this.getOwner());
    }
}
