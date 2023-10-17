package app.view;

import javax.swing.*;
import java.awt.*;

public class ContentContainer extends JPanel {
    private static final long serialVersionUID = -2879987797128466807L;
    private final TabsContainer tabsContainer;
    private final ButtonsContainer buttonsContainer;

    public ContentContainer() {
        super(new BorderLayout(10, 10));

        this.tabsContainer = new TabsContainer();
        this.buttonsContainer = new ButtonsContainer(this.getTabsContainer());

        this.add(this.tabsContainer, BorderLayout.CENTER);
        this.add(this.buttonsContainer, BorderLayout.SOUTH);
    }

    public TabsContainer getTabsContainer() {
        return this.tabsContainer;
    }

    public ButtonsContainer getButtonsContainer() {
        return this.buttonsContainer;
    }
}
