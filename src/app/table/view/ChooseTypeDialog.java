/*
 * Created by JFormDesigner on Sat May 30 15:28:38 CEST 2020
 */

package app.table.view;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ChooseTypeDialog<T> extends JDialog {
    private static final long serialVersionUID = -3219426890315236681L;
    private JPanel dialogPane;
    private JPanel contentPanel;
    private JPanel buttonBar;
    private JScrollPane typesListContainer;
    protected JList<T> typesList;
    protected JButton okButton;

    public ChooseTypeDialog(final Window owner) {
        super(owner);
        this.initComponents();
        this.setModal(true);
    }

    private void initComponents() {
        this.dialogPane = new JPanel();
        this.contentPanel = new JPanel();
        this.typesListContainer = new JScrollPane();
        this.typesList = new JList<>();
        this.buttonBar = new JPanel();
        this.okButton = new JButton();

        final Container contentPane = this.getContentPane();
        contentPane.setLayout(new BorderLayout());

        this.dialogPane.setLayout(new BorderLayout());

        this.contentPanel.setLayout(new BorderLayout(20, 20));

        this.typesListContainer.setViewportView(this.typesList);

        this.contentPanel.add(this.typesListContainer, BorderLayout.CENTER);

        this.dialogPane.add(this.contentPanel, BorderLayout.CENTER);

        this.buttonBar.setBorder(new EmptyBorder(12, 0, 0, 0));
        this.buttonBar.setLayout(new GridBagLayout());
        ((GridBagLayout) this.buttonBar.getLayout()).columnWidths = new int[]{0, 80};
        ((GridBagLayout) this.buttonBar.getLayout()).columnWeights = new double[]{1.0, 0.0};

        this.okButton.setText("OK");
        this.buttonBar.add(this.okButton, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0,
                GridBagConstraints.CENTER, GridBagConstraints.BOTH,
                new Insets(0, 0, 0, 0), 0, 0));

        this.dialogPane.add(this.buttonBar, BorderLayout.SOUTH);

        contentPane.add(this.dialogPane, BorderLayout.CENTER);
        this.pack();
        this.setLocationRelativeTo(this.getOwner());
    }
}
