package app;

import app.database.DataSource;
import app.database.DataSourceImpl;
import app.database.repository.MicrosoftSqlServerRepository;
import app.events.ResourceEvent;
import app.tree.model.Resource;
import app.view.AppMainFrame;
import com.formdev.flatlaf.FlatDarkLaf;

import javax.swing.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.SQLException;

public class Application {

    public static void main(final String[] args) {

        FlatDarkLaf.install();

        final AppMainFrame appMainFrame = new AppMainFrame();
        ApplicationSingleton.getInstance().setAppMainFrame(appMainFrame);
        appMainFrame.setVisible(true);

        final DataSource dataSource = new DataSourceImpl(new MicrosoftSqlServerRepository());
        ApplicationSingleton.getInstance().setDataSource(dataSource);
        dataSource.initializeConnection();

        try {
            final Resource resource = dataSource.loadResource();
            ApplicationSingleton.getInstance().setResource(resource);
            resource.notifyObservers(ResourceEvent.LOAD_RESOURCE);
        } catch (final SQLException e) {
            JOptionPane.showMessageDialog(null, "Couldn't retrieve data from database! Please, try again later.");
        }

        ApplicationSingleton.getInstance().getAppMainFrame().addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosed(final WindowEvent e) {
                ApplicationSingleton.getInstance().getDataSource().closeConnection();
            }
        });
    }
}