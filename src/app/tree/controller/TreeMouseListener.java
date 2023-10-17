package app.tree.controller;

import app.ApplicationSingleton;
import app.model.Row;
import app.tree.model.Entity;
import app.tree.view.ApplicationTree;
import app.view.TabsContainer;

import javax.swing.tree.DefaultMutableTreeNode;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class TreeMouseListener extends MouseAdapter {

    private final ApplicationTree tree;

    public TreeMouseListener(final ApplicationTree tree) {
        this.tree = tree;
    }

    @Override
    public void mouseClicked(final MouseEvent e) {
        final int row = this.tree.getRowForLocation(e.getX(), e.getY());
        if (row == -1) {
            return;
        }
        //Reaguje na dvoklik tako sto otvara tabove sa tabelama(entitetima)
        final DefaultMutableTreeNode selectedNode = (DefaultMutableTreeNode) this.tree.getLastSelectedPathComponent();
        if (e.getClickCount() == 2 && selectedNode.getUserObject() instanceof Entity) {
            final Entity entity = (Entity) selectedNode.getUserObject();
            final List<Row> rows = ApplicationSingleton.getInstance().getDataSource().getData(entity.getTitle());
            entity.setRows(rows);
            final TabsContainer topTabsContainer = ApplicationSingleton.getInstance().getAppMainFrame().getMainPanel().getTopContainer().getTabsContainer();
            topTabsContainer.addTab(entity, true);
        }
    }
}