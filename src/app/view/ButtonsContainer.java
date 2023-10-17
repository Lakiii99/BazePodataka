package app.view;

import app.controller.*;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class ButtonsContainer extends JPanel {
    private static final long serialVersionUID = 7219870954250731170L;
    private final TabsContainer correspondingTabsContainer;

    public ButtonsContainer(final TabsContainer correspondingTabsContainer) {
        super(new FlowLayout(FlowLayout.RIGHT, 10, 10));

        this.correspondingTabsContainer = correspondingTabsContainer;

        final OpenAddDialogAction openAddDialogAction = new OpenAddDialogAction("ADD", this);
        final JButton addButton = new JButton(openAddDialogAction);

        final OpenUpdateDialogAction openUpdateDialogAction = new OpenUpdateDialogAction("UPDATE", this);
        final JButton updateButton = new JButton(openUpdateDialogAction);

        final DeleteRowAction deleteRowAction = new DeleteRowAction("DELETE", this);
        final JButton deleteButton = new JButton(deleteRowAction);

        final OpenFilterAndSortDialogAction openFilterAndSortDialogAction = new OpenFilterAndSortDialogAction("FILTER & SORT", this);
        final JButton filterAndSortButton = new JButton(openFilterAndSortDialogAction);

        final OpenChooseReportTypeDialogAction openChooseReportTypeDialogAction = new OpenChooseReportTypeDialogAction("GENERATE REPORT", this);
        final JButton generateReportButton = new JButton(openChooseReportTypeDialogAction);

        final OpenSearchDialogAction openSearchDialogAction = new OpenSearchDialogAction("SEARCH", this);
        final JButton searchButton = new JButton(openSearchDialogAction);

        this.add(addButton);
        this.add(updateButton);
        this.add(deleteButton);
        this.add(filterAndSortButton);
        this.add(generateReportButton);
        this.add(searchButton);
    }
}