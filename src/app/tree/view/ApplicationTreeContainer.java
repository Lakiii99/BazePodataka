package app.tree.view;

import app.view.AppMainFrame;

import javax.swing.*;
import java.awt.*;

public class ApplicationTreeContainer extends JScrollPane {
    private static final long serialVersionUID = -4328794641885151338L;

    public ApplicationTreeContainer(final JTree tree) {
        this.setViewportView(tree);
        this.setPreferredSize(new Dimension(AppMainFrame.SCREEN_WIDTH / 6, 0));
        this.setMinimumSize(new Dimension(AppMainFrame.SCREEN_WIDTH / 8, 0));
    }
}