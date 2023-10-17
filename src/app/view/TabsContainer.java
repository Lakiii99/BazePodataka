package app.view;

import app.ApplicationSingleton;
import app.table.view.Table;
import app.tree.model.Entity;
import lombok.Getter;

import javax.swing.*;
import java.util.HashMap;
import java.util.Map;

@Getter
public class TabsContainer extends JTabbedPane {

    private static final long serialVersionUID = 1984497511125983392L;
    private static final ImageIcon CLOSE_ICON = new ImageIcon("resources/images/close_icon.png");
    private final Map<String, TableView> tabs;

    public TabsContainer() {
        this.tabs = new HashMap<>();
    }

    public void addSelectionChangeListener() {
        this.addChangeListener(e -> {
            final TableView tableView = (TableView) TabsContainer.this.getSelectedComponent();
            if (tableView != null) {
                ApplicationSingleton.getInstance().getAppMainFrame().getMainPanel().updateBottomContainer(tableView.getTable().getEntity());
            } else {
                ApplicationSingleton.getInstance().getAppMainFrame().getMainPanel().getBottomContainer().getTabsContainer().removeAllTabs();
            }
        });
    }

    public void addTab(final Entity entity, final boolean parent) {
        if (!this.tabs.containsKey(entity.getTitle())) {
            final TableView tableView = new TableView(entity, parent);
            this.tabs.put(entity.getTitle(), tableView);
            this.addTab(entity.getTitle(), tableView);
            this.setTabComponentAt(this.getTabCount() - 1, this.createTab(entity.getTitle()));

            //Add table as observer of his entity object
            final Table table = tableView.getTable();
            table.getEntity().addObserver(table);
        }

        final TableView tableView = this.tabs.get(entity.getTitle());
        if (this.indexOfComponent(tableView) != -1) {
            this.setSelectedComponent(tableView);
        }
    }

    public void removeTab(final String title) {
        if (this.tabs.containsKey(title)) {
            final int indexOfTab = this.indexOfTab(title);
            this.removeTabAt(indexOfTab);

            final TableView tableView = this.tabs.get(title);
            final Table table = tableView.getTable();
            table.getEntity().deleteObserver(table);

            this.tabs.remove(title);
        }
    }

    public void removeAllTabs() {
        while (this.getTabCount() > 0) {
            this.removeTabAt(0);
        }

        for (final Map.Entry<String, TableView> entry : this.tabs.entrySet()) {
            final TableView tableView = entry.getValue();
            final Table table = tableView.getTable();
            table.getEntity().deleteObserver(table);
        }

        this.tabs.clear();
    }

    public Entity getSelectedEntity() {
        final TableView tableView = (TableView) this.getSelectedComponent();

        if (tableView != null) {
            return tableView.getTable().getEntity();
        }

        return null;
    }

    public JPanel createTab(final String title) {
        final JPanel customTab = new JPanel();
        customTab.setLayout(new BoxLayout(customTab, BoxLayout.X_AXIS));
        customTab.setOpaque(false);

        final JLabel lblTitle = new JLabel(title);
        customTab.add(lblTitle);

        final JButton btnClose = new JButton(CLOSE_ICON);
        btnClose.setBorderPainted(false);
        btnClose.setContentAreaFilled(false);
        btnClose.addActionListener(e -> TabsContainer.this.removeTab(title));

        customTab.add(btnClose);
        return customTab;
    }
}