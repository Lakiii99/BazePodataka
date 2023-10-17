package app;

import app.database.DataSource;
import app.tree.model.Resource;
import app.view.AppMainFrame;
import lombok.Data;

@Data
public class ApplicationSingleton {

    private static final ApplicationSingleton instance = new ApplicationSingleton();
    private AppMainFrame appMainFrame;
    private DataSource dataSource;
    private Resource resource;

    private ApplicationSingleton() {
    }

    public static ApplicationSingleton getInstance() {
        return instance;
    }
}