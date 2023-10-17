package app.tree.view;

import app.events.ResourceEvent;
import app.tree.controller.TreeMouseListener;
import app.tree.model.Attribute;
import app.tree.model.Constraint;
import app.tree.model.Entity;
import app.tree.model.Resource;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeSelectionModel;
import java.util.Observable;
import java.util.Observer;

public class ApplicationTree extends JTree implements Observer {
    private static final long serialVersionUID = 2629630196432604484L;
    private final DefaultTreeModel treeModel;

    public ApplicationTree() {
        this.treeModel = new DefaultTreeModel(null);
        this.setModel(this.treeModel);
        this.setEditable(false);
        this.setCellRenderer(new CustomTreeRenderer());
        this.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
        this.addMouseListener(new TreeMouseListener(this));
    }

    public DefaultTreeModel getTreeModel() {
        return this.treeModel;
    }

    private void expandAllNodes() {
        for (int i = 0; i < this.getRowCount(); i++) {
            this.expandRow(i);
        }
    }

    @Override
    public void update(final Observable o, final Object arg) {
        if (arg instanceof ResourceEvent) {
            final ResourceEvent resourceEvent = (ResourceEvent) arg;
            if (resourceEvent == ResourceEvent.LOAD_RESOURCE && o instanceof Resource) {
                final Resource resource = (Resource) o;
                this.initTreeData(resource);
                this.expandAllNodes();
            }
        }
    }

    private void initTreeData(final Resource resource) {

        //Setujemo root stabla
        final DefaultMutableTreeNode root = new DefaultMutableTreeNode(resource);
        this.treeModel.setRoot(root);

        //Setujemo entitete kao decu root-a
        for (final Entity entity : resource.getEntities()) {
            final DefaultMutableTreeNode entityTreeNode = new DefaultMutableTreeNode(entity);
            this.treeModel.insertNodeInto(entityTreeNode, root, root.getChildCount());

            //Setujemo atribute kao decu svakog  entiteta
            for (final Attribute attribute : entity.getAttributes()) {
                final DefaultMutableTreeNode attributeTreeNode = new DefaultMutableTreeNode(attribute);
                this.treeModel.insertNodeInto(attributeTreeNode, entityTreeNode, entityTreeNode.getChildCount());

                //Setujemo ogranicenja kao decu svakog atributa
                for (final Constraint constraint : attribute.getConstraints()) {
                    final DefaultMutableTreeNode constraintTreeNode = new DefaultMutableTreeNode(constraint);
                    this.treeModel.insertNodeInto(constraintTreeNode, attributeTreeNode, attributeTreeNode.getChildCount());
                }
            }
        }
    }
}
