package com.leaf.surveys;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public class SurveyHandler {

    private static SessionFactory sessionFactory;
    private static Session session;


    SurveyHandler() {
        try {
            sessionFactory = SessionFactoryController.getSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }

    }

    public static Integer createSurvey(Integer user_id, String title, String question) {
        session = sessionFactory.openSession();
        Transaction tx = null;
        Integer SurveyID = null;
        Survey survey = null;
        try {
            tx = session.beginTransaction();
            Users user = UsersHandler.getUser(user_id);
            survey = new Survey(user, title, question);
            System.err.println(survey.toJson());
            SurveyID = (Integer) session.save(survey);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();

        }
        return SurveyID;
    }

    public static Survey getSurvey(Integer SurveyID) {
        session = sessionFactory.openSession();
        Transaction tx = null;
        Survey survey = null;
        try {
            tx = session.beginTransaction();
            survey = (Survey) session.get(Survey.class, SurveyID);
        } catch (HibernateException e) {
            System.err.println("Rollback on getSurvey");
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return survey;
    }


    public static List<Survey> getSurveysByUserId(Integer user_id) {
        session = sessionFactory.openSession();


        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Survey> criteria = builder.createQuery(Survey.class);
        criteria.from(Survey.class);
        List<Survey> surveys = session.createQuery(criteria).getResultList();
        Iterator<Survey> it = surveys.iterator();
        List<Survey> results = new ArrayList<>();


        while (it.hasNext()) {
            Survey surv = (Survey) it.next();
            if (surv.getUser().getUser_id() == user_id) {
                results.add(surv);
            }
        }
        return results;
    }

    public static List<Survey> getAllSurveys() {
        session = sessionFactory.openSession();


        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Survey> criteria = builder.createQuery(Survey.class);
        criteria.from(Survey.class);
        List<Survey> results = session.createQuery(criteria).getResultList();

        return results;
    }




    public static boolean deleteSurvey(Integer SurveyID) {


        session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            Survey surv = session.get(Survey.class, SurveyID);

            if (surv != null) {
                //for(Answer answer : AnswerHandler.getAnswerBySurveyId(SurveyID)){
               //     session.remove(answer);
                //}

                session.remove(surv);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                System.err.println("Rollback on delete survey");
                transaction.rollback();
            }
            e.printStackTrace();
            return false;
        } finally {
            if (session != null) {
                session.close();
            }
        }
        return true;
    }


    public static List<Answer> getAllAnswers(Integer SurveyID){

        List<Answer> result = new ArrayList<>();

        session = sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();

            Survey surv = session.get(Survey.class, SurveyID);

            if (surv != null) {
                for(Answer answer : AnswerHandler.getAnswerBySurveyId(SurveyID)){
                     result.add(answer);
                }


                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                System.err.println("Rollback on delete survey");
                transaction.rollback();
            }
            e.printStackTrace();

        } finally {
            if (session != null) {
                session.close();
            }
        }
       return result;
    }
}
