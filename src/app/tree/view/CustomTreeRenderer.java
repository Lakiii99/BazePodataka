package app.tree.view;

import app.tree.model.Attribute;
import app.tree.model.Constraint;
import app.tree.model.Entity;
import app.tree.model.Resource;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import java.awt.*;

public class CustomTreeRenderer extends DefaultTreeCellRenderer {

    private static final long serialVersionUID = -9111421262581360361L;
    private static final ImageIcon RESOURCE_ICON = new ImageIcon("resources/images/resource_icon.png");
    private static final ImageIcon TABLE_ICON = new ImageIcon("resources/images/table_icon.png");
    private static final ImageIcon COLUMN_ICON = new ImageIcon("resources/images/column_icon.png");
    private static final ImageIcon CONSTRAINT_ICON = new ImageIcon("resources/images/constraint_icon.png");

    public CustomTreeRenderer() {
        this.setFont(new Font("Arial", Font.PLAIN, 15));
    }

    @Override
    public Component getTreeCellRendererComponent(final JTree tree, final Object value, final boolean sel, final boolean expanded, final boolean leaf, final int row, final boolean hasFocus) {
        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);

        final DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;
        final Object object = node.getUserObject();

        if (object instanceof Resource) {
            this.setIcon(RESOURCE_ICON);
        } else if (object instanceof Entity) {
            this.setIcon(TABLE_ICON);
        } else if (object instanceof Attribute) {
            this.setIcon(COLUMN_ICON);
        } else if (object instanceof Constraint) {
            this.setIcon(CONSTRAINT_ICON);
        }

        return this;
    }
}