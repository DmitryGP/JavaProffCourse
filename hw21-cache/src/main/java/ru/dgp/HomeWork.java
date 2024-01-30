package ru.dgp;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.dgp.cachehw.HwListener;
import ru.dgp.cachehw.MyCache;
import ru.dgp.core.repository.executor.DbExecutorImpl;
import ru.dgp.core.sessionmanager.TransactionRunnerJdbc;
import ru.dgp.crm.datasource.DriverManagerDataSource;
import ru.dgp.crm.model.Client;
import ru.dgp.crm.model.Manager;
import ru.dgp.crm.service.DbServiceClientImpl;
import ru.dgp.crm.service.DbServiceManagerImpl;
import ru.dgp.jdbc.mapper.DataTemplateJdbc;
import ru.dgp.jdbc.mapper.EntityClassMetaData;
import ru.dgp.jdbc.mapper.EntityClassMetaDataImpl;
import ru.dgp.jdbc.mapper.EntitySQLMetaData;
import ru.dgp.jdbc.mapper.EntitySQLMetaDataImpl;

public class HomeWork {
    private static final String URL = "jdbc:postgresql://localhost:5430/demoDB";
    private static final String USER = "usr";
    private static final String PASSWORD = "pwd";

    private static final Logger log = LoggerFactory.getLogger(HomeWork.class);

    public static void main(String[] args) {
        // Общая часть
        var dataSource = new DriverManagerDataSource(URL, USER, PASSWORD);
        flywayMigrations(dataSource);
        var transactionRunner = new TransactionRunnerJdbc(dataSource);
        var dbExecutor = new DbExecutorImpl();

        EntityClassMetaData<Client> entityClassMetaDataClient = new EntityClassMetaDataImpl<>(Client.class);
        EntitySQLMetaData entitySQLMetaDataClient = new EntitySQLMetaDataImpl(entityClassMetaDataClient);
        MyCache<Long, Client> clientCache = createCache(10);
        var dataTemplateClient =
                new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataClient, entityClassMetaDataClient, clientCache);

        var dbServiceClient = new DbServiceClientImpl(transactionRunner, dataTemplateClient);
        dbServiceClient.saveClient(new Client("dbServiceFirst"));

        var clientSecond = dbServiceClient.saveClient(new Client("dbServiceSecond"));
        var clientSecondSelected = dbServiceClient
                .getClient(clientSecond.getId())
                .orElseThrow(() -> new RuntimeException("Client not found, id:" + clientSecond.getId()));
        log.info("clientSecondSelected:{}", clientSecondSelected);

        EntityClassMetaData<Manager> entityClassMetaDataManager = new EntityClassMetaDataImpl<>(Manager.class);
        EntitySQLMetaData entitySQLMetaDataManager = new EntitySQLMetaDataImpl(entityClassMetaDataManager);
        MyCache<Long, Manager> managerCache = createCache(10);
        var dataTemplateManager =
                new DataTemplateJdbc<>(dbExecutor, entitySQLMetaDataManager, entityClassMetaDataManager, managerCache);

        var dbServiceManager = new DbServiceManagerImpl(transactionRunner, dataTemplateManager);
        dbServiceManager.saveManager(new Manager("ManagerFirst"));

        var managerSecond = dbServiceManager.saveManager(new Manager("ManagerSecond"));
        var managerSecondSelected = dbServiceManager
                .getManager(managerSecond.getNo())
                .orElseThrow(() -> new RuntimeException("Manager not found, id:" + managerSecond.getNo()));
        log.info("managerSecondSelected:{}", managerSecondSelected);
    }

    @SuppressWarnings("java:S1604")
    private static <T> MyCache<Long, T> createCache(int capacity) {
        MyCache<Long, T> cache = new MyCache<>(capacity);

        var listener = new HwListener<Long, T>() {
            @Override
            public void notify(Long key, T value, String action) {
                log.atInfo()
                        .setMessage("Cache: [%s] key = %s value = %s.")
                        .addArgument(action)
                        .addArgument(key)
                        .addArgument(value)
                        .log();
            }
        };

        cache.addListener(listener);

        return cache;
    }

    private static void flywayMigrations(DataSource dataSource) {
        log.info("db migration started...");
        var flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations("classpath:/db/migration")
                .load();
        flyway.migrate();
        log.info("db migration finished.");
        log.info("***");
    }
}
