package com.leaf.surveys;

        import org.hibernate.Session;
        import org.hibernate.SessionFactory;
        import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
        import org.hibernate.cfg.Configuration;
        import org.hibernate.service.ServiceRegistry;
        import org.springframework.boot.SpringApplication;
        import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SurveysApplication {




    public static void main(String[] args)
    {


        SpringApplication.run(SurveysApplication.class, args);
        UsersHandler usersHandler = new UsersHandler();
        SurveyHandler surveysHandler = new SurveyHandler();
        AnswerHandler answer = new AnswerHandler();
    }





}
