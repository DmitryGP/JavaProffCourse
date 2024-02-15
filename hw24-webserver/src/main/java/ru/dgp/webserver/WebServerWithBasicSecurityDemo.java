package ru.dgp.webserver;

import java.net.URI;
import org.eclipse.jetty.security.HashLoginService;
import org.eclipse.jetty.security.LoginService;
import org.eclipse.jetty.util.resource.PathResourceFactory;
import org.eclipse.jetty.util.resource.Resource;
import org.hibernate.cfg.Configuration;
import ru.dgp.clientservice.core.repository.DataTemplateHibernate;
import ru.dgp.clientservice.core.repository.HibernateUtils;
import ru.dgp.clientservice.core.sessionmanager.TransactionManagerHibernate;
import ru.dgp.clientservice.crm.dbmigrations.MigrationsExecutorFlyway;
import ru.dgp.clientservice.crm.model.Address;
import ru.dgp.clientservice.crm.model.Client;
import ru.dgp.clientservice.crm.model.Phone;
import ru.dgp.clientservice.crm.service.DbServiceClientImpl;
import ru.dgp.webserver.helpers.FileSystemHelper;
import ru.dgp.webserver.server.ClientsWebServer;
import ru.dgp.webserver.server.ClientsWebServerWithBasicSecurity;
import ru.dgp.webserver.services.TemplateProcessor;
import ru.dgp.webserver.services.TemplateProcessorImpl;

/*
    Ссылки

    // Стартовая страница
    http://localhost:8080

    // Страница пользователей
    http://localhost:8080/clients

*/
public class WebServerWithBasicSecurityDemo {

    public static final String HIBERNATE_CFG_FILE = "hibernate.cfg.xml";
    private static final int WEB_SERVER_PORT = 8080;
    private static final String TEMPLATES_DIR = "/templates/";
    private static final String HASH_LOGIN_SERVICE_CONFIG_NAME = "realm.properties";
    private static final String REALM_NAME = "AnyRealm";

    public static void main(String[] args) throws Exception {
        var configuration = new Configuration().configure(HIBERNATE_CFG_FILE);

        var dbUrl = configuration.getProperty("hibernate.connection.url");
        var dbUserName = configuration.getProperty("hibernate.connection.username");
        var dbPassword = configuration.getProperty("hibernate.connection.password");

        new MigrationsExecutorFlyway(dbUrl, dbUserName, dbPassword).executeMigrations();

        var sessionFactory =
                HibernateUtils.buildSessionFactory(configuration, Client.class, Address.class, Phone.class);

        var transactionManager = new TransactionManagerHibernate(sessionFactory);
        ///
        var clientTemplate = new DataTemplateHibernate<>(Client.class);
        ///
        var dbServiceClient = new DbServiceClientImpl(transactionManager, clientTemplate);

        TemplateProcessor templateProcessor = new TemplateProcessorImpl(TEMPLATES_DIR);

        String hashLoginServiceConfigPath =
                FileSystemHelper.localFileNameOrResourceNameToFullPath(HASH_LOGIN_SERVICE_CONFIG_NAME);
        PathResourceFactory pathResourceFactory = new PathResourceFactory();
        Resource configResource = pathResourceFactory.newResource(URI.create(hashLoginServiceConfigPath));

        LoginService loginService = new HashLoginService(REALM_NAME, configResource);

        ClientsWebServer usersWebServer = new ClientsWebServerWithBasicSecurity(
                WEB_SERVER_PORT, loginService, dbServiceClient, templateProcessor);

        usersWebServer.start();
        usersWebServer.join();
    }
}
