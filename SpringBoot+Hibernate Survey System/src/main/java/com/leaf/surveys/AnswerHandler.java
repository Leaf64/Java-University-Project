package com.leaf.surveys;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class AnswerHandler {

    private static SessionFactory sessionFactory;
    private static Session session;


    AnswerHandler() {
        try {
            sessionFactory = SessionFactoryController.getSessionFactory();
        } catch (Throwable ex) {
            System.err.println("Failed to create sessionFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Integer createAnswer(Integer user_id, Integer survey_id, int rating) {
        session = sessionFactory.openSession();
        Transaction tx = null;
        Integer AnswerID = null;
        Answer answer = null;
        try {
            tx = session.beginTransaction();
            Users user = UsersHandler.getUser(user_id);
            Survey survey = SurveyHandler.getSurvey(survey_id);
            System.err.println("OTO SURVEY KURWA: ");
            System.err.println(survey.toJson());
            answer = new Answer(survey,user,rating);
            System.err.println(answer.toJson());
            AnswerID = (Integer) session.save(answer);
            tx.commit();
        } catch (HibernateException e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();

        }
        return AnswerID;



    }

    public static Answer getAnswer(Integer AnswerID) {
        session = sessionFactory.openSession();
        Transaction tx = null;
        Answer answer = null;
        try {
            tx = session.beginTransaction();
            answer = (Answer) session.get(Answer.class, AnswerID);
        } catch (HibernateException e) {
            System.err.println("Rollback on getAnswer");
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return answer;
    }

    public static List<Answer> getAnswerBySurveyId(Integer survey_id) {
        session = sessionFactory.openSession();


        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Answer> criteria = builder.createQuery(Answer.class);
        criteria.from(Answer.class);
        List<Answer> answers = session.createQuery(criteria).getResultList();
        Iterator<Answer> it = answers.iterator();
        List<Answer> results = new ArrayList<>();


        while (it.hasNext()) {
            Answer answ = (Answer) it.next();
            if (answ.getUser().getUser_id() == survey_id) {
                results.add(answ);
            }
        }
        return results;
    }

    public static List<Answer> getAllAnswers() {
        session = sessionFactory.openSession();


        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<Answer> criteria = builder.createQuery(Answer.class);
        criteria.from(Answer.class);
        List<Answer> answers = session.createQuery(criteria).getResultList();
        Iterator<Answer> it = answers.iterator();
        List<Answer> results = new ArrayList<>();
        return results;
    }


    public static Answer editAnswer(Integer AnswerID, int rating) {
        session = sessionFactory.openSession();
        Transaction tx = null;
        Answer answer = null;
        try {
            tx = session.beginTransaction();
            answer = (Answer) session.get(Answer.class, AnswerID);
            session.evict(answer);
            answer.setRating(rating);
            session.update(answer);
            tx.commit();
        } catch (HibernateException e) {
            System.err.println("Rollback on getAnswer");
            if (tx != null) tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return answer;
    }
}
