package com.leaf.surveys;

import com.fasterxml.classmate.AnnotationConfiguration;
import com.google.gson.JsonObject;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Service;

import java.util.List;


public class UsersHandler {

    private static SessionFactory sessionFactory;
    private static Session session;


    UsersHandler() {

        try {
            sessionFactory = SessionFactoryController.getSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }


    }

    public static Integer createUser(String username) {
        session = sessionFactory.openSession();
        Transaction tx = null;
        Integer UserID = null;
        Users user = null;
        try {
            tx = session.beginTransaction();
            user = new Users(username);

            UserID = (Integer) session.save(user);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            System.err.println(UserID);
            session.close();

        }
        return UserID;
    }

    public static Users getUser(Integer UserID) {
        session = sessionFactory.openSession();
        Transaction tx = null;
        Users user = null;
        try {
            tx = session.beginTransaction();
            user = (Users) session.get(Users.class, UserID);
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return user;
    }


    public static String getUserStats(Integer userID) {

        JsonObject jsonObject = new JsonObject();
        List<Survey> surveys = SurveyHandler.getSurveysByUserId(userID);
        int surveysAmount = surveys.size();
        jsonObject.addProperty("Amount of surveys for user", surveysAmount);

        int sumAllAnswers=0;
        for(Survey survey : surveys){
            List<Answer> answers = SurveyHandler.getAllAnswers(survey.getSurvey_id());
            for(Answer answer : answers){
                sumAllAnswers += answer.getRating();
            }
        }

        float averageOfAnswers = (float)((float)sumAllAnswers/(float)surveysAmount);
        jsonObject.addProperty("Average amount of answers for survey", averageOfAnswers);


        return jsonObject.toString();

    }
}


