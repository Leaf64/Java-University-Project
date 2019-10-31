package com.leaf.surveys;

import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;

public class SessionFactoryController {

    private static SessionFactory sessionFactory;


    private static SessionFactory buildSessionFactory() {

        Configuration configuration = new Configuration();
        configuration.configure("hibernate.cfg.xml");
        configuration.addAnnotatedClass(Users.class);
        configuration.addAnnotatedClass(Survey.class);
        configuration.addAnnotatedClass(Answer.class);
        sessionFactory = configuration.buildSessionFactory();

        return sessionFactory;
    }

    public static SessionFactory getSessionFactory() {
        if(sessionFactory == null) sessionFactory = buildSessionFactory();
        return sessionFactory;
    }

}
