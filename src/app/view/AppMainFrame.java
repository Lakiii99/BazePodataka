package app.view;

import app.tree.view.ApplicationTree;
import app.tree.view.ApplicationTreeContainer;

import javax.swing.*;
import java.awt.*;

public class AppMainFrame extends JFrame {
    private static final long serialVersionUID = -4669561551652043197L;
    public static final int SCREEN_WIDTH = Toolkit.getDefaultToolkit().getScreenSize().width;
    public static final int SCREEN_HEIGHT = Toolkit.getDefaultToolkit().getScreenSize().height;
    private final ApplicationTreeContainer treeContainer;
    private final ApplicationTree tree;
    private final AppMainPanel mainPanel;

    public AppMainFrame() {
        this.setTitle("Database Management tool");
        this.setSize(new Dimension(4 * SCREEN_WIDTH / 5, 3 * SCREEN_HEIGHT / 4));
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLocationRelativeTo(null);

        this.tree = new ApplicationTree();
        this.treeContainer = new ApplicationTreeContainer(this.tree);

        final JSplitPane splitPane = new JSplitPane();
        splitPane.setOneTouchExpandable(true);
        splitPane.setLeftComponent(this.treeContainer);

        this.mainPanel = new AppMainPanel();
        splitPane.setRightComponent(this.mainPanel);

        this.add(splitPane, BorderLayout.CENTER);
    }

    public ApplicationTreeContainer getTreeContainer() {
        return this.treeContainer;
    }

    public ApplicationTree getTree() {
        return this.tree;
    }

    public AppMainPanel getMainPanel() {
        return this.mainPanel;
    }
}