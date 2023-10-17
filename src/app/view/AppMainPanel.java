package app.view;

import app.ApplicationSingleton;
import app.model.Relation;
import app.model.Row;
import app.tree.model.Entity;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AppMainPanel extends JPanel {
    private static final long serialVersionUID = 476572460491240812L;
    private final ContentContainer topContainer;
    private final ContentContainer bottomContainer;

    public AppMainPanel() {
        this.setBackground(Color.WHITE);
        this.setMinimumSize(new Dimension(AppMainFrame.SCREEN_WIDTH / 2, 0));
        this.setLayout(new GridLayout(2, 1));

        this.topContainer = new ContentContainer();
        this.bottomContainer = new ContentContainer();

        this.topContainer.getTabsContainer().addSelectionChangeListener();

        this.add(this.topContainer);
        this.add(this.bottomContainer);
    }

    public ContentContainer getTopContainer() {
        return this.topContainer;
    }

    public ContentContainer getBottomContainer() {
        return this.bottomContainer;
    }

    public void updateBottomContainer(final Entity entity) {
        this.bottomContainer.getTabsContainer().removeAllTabs();
        for (final Relation relation : entity.getParentRelations()) {
            final String foreignKeyTableName = relation.getForeignKeyTableName();
            final Entity childTable = new Entity(ApplicationSingleton.getInstance().getResource().retrieveTableByName(foreignKeyTableName));
            final List<Row> rows = ApplicationSingleton.getInstance().getDataSource().getData(childTable.getTitle());
            childTable.setRows(rows);
            this.bottomContainer.getTabsContainer().addTab(childTable, false);
        }
    }
}