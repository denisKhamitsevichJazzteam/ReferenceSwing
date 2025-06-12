package org.jazzteam.util;


import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.jazzteam.model.Priority;
import org.jazzteam.model.Todo;

@Slf4j
public class HibernateUtil {
    @Getter
    private static final SessionFactory sessionFactory = buildSessionFactory();

    private static SessionFactory buildSessionFactory() {
        try {
            Configuration configuration = new Configuration();

            // For our current purposes, it's sufficient to define these properties directly here
            // instead of using an external file.
            configuration.addProperties(
                    new java.util.Properties() {{
                        put("hibernate.connection.driver_class", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        put("hibernate.connection.url", "jdbc:sqlserver://localhost:1433;database=ReferenceSwingDb");
                        put("hibernate.connection.username", "admin");
                        put("hibernate.connection.password", "admin");
                        put("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
                        put("hibernate.hbm2ddl.auto", "update");
                        put("hibernate.show_sql", "true");
                    }}
            );

            configuration.addAnnotatedClass(Todo.class);
            configuration.addAnnotatedClass(Priority.class);

            return configuration.buildSessionFactory();
        } catch (Exception ex) {
            log.error("Initial SessionFactory creation failed.{}", String.valueOf(ex));
            throw new ExceptionInInitializerError(ex);
        }
    }

}

