package app.controller;

import app.view.ButtonsContainer;

import javax.swing.*;

public abstract class AbstractActionWithParent extends AbstractAction {
    private static final long serialVersionUID = 1226661375180977685L;
    protected ButtonsContainer parent;

    public AbstractActionWithParent(final String name, final ButtonsContainer parent) {
        this.putValue(Action.NAME, name);
        this.parent = parent;
    }
}
