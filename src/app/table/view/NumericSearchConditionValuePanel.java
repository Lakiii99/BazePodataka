/*
 * Created by JFormDesigner on Tue Jun 02 22:42:48 CEST 2020
 */

package app.table.view;

import app.model.RelationalOperatorType;
import lombok.Getter;
import net.miginfocom.swing.MigLayout;

import javax.swing.*;
import java.awt.*;

public class NumericSearchConditionValuePanel extends JPanel {

    private static final long serialVersionUID = -7224869774486140051L;

    @Getter
    private JList<RelationalOperatorType> operatorTypeList;

    @Getter
    private JTextField searchConditionValue;

    public NumericSearchConditionValuePanel() {
        this.initComponents();

        final DefaultListModel<RelationalOperatorType> relationalOperatorTypeDefaultListModel = new DefaultListModel<>();
        relationalOperatorTypeDefaultListModel.addElement(RelationalOperatorType.LESS);
        relationalOperatorTypeDefaultListModel.addElement(RelationalOperatorType.GREATER);
        relationalOperatorTypeDefaultListModel.addElement(RelationalOperatorType.EQUAL);
        this.operatorTypeList.setModel(relationalOperatorTypeDefaultListModel);
        this.operatorTypeList.setSelectedValue(relationalOperatorTypeDefaultListModel.get(0), true);
    }

    private void initComponents() {
        final JScrollPane operatorTypeListContainer = new JScrollPane();
        this.operatorTypeList = new JList<>();
        this.searchConditionValue = new JTextField();

        operatorTypeListContainer.setMinimumSize(new Dimension(70, 70));
        operatorTypeListContainer.setPreferredSize(new Dimension(70, 70));
        operatorTypeListContainer.setMaximumSize(new Dimension(70, 70));

        this.setLayout(new MigLayout(
                "fill,hidemode 3,align center center,gap 5 5",
                "[fill]" +
                        "[fill]",
                "[]"));

        operatorTypeListContainer.setViewportView(this.operatorTypeList);
        this.add(operatorTypeListContainer, "cell 0 0");
        this.add(this.searchConditionValue, "cell 1 0,aligny center,growy 0");
    }
}
