package tech.chernega.backend.database;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfigurator {

    private static final String JDBC_URL_VAR = "jdbc.url";
    private static final String JDBC_USER_VAR = "jdbc.user";
    private static final String JDBC_PASSWORD_VAR = "jdbc.password";

    @Bean
    public DataSource getDataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        if (isEnvironmentConfigurationProvided()) {
            dataSource.setUrl(System.getenv(JDBC_URL_VAR));
            dataSource.setUsername(System.getenv(JDBC_USER_VAR));
            dataSource.setPassword(System.getenv(JDBC_PASSWORD_VAR));
        } else {
            dataSource.setUrl("jdbc:h2:file:./backend-test;DB_CLOSE_DELAY=-1");
        }
        return dataSource;
    }

    private boolean isEnvironmentConfigurationProvided() {
        return System.getenv(JDBC_URL_VAR) != null;
    }

}

