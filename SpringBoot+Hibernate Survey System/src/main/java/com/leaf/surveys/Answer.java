package com.leaf.surveys;

import com.google.gson.JsonObject;

import javax.persistence.*;
import java.io.Serializable;


@Entity
@Table(name = "answer")
public class Answer implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "answer_id")
    private int answer_id;


    @ManyToOne
    @JoinColumn(name = "survey_id", nullable = true)
    private Survey survey;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    private Users user;

    @Column(name = "rating")
    private int rating;


    public Answer(Survey survey, Users user, int rating) {
        super();
        this.survey = survey;
        this.user = user;
        this.rating = rating;
    }




    public Answer() {
    }


    public int getAnswer_id() {
        return answer_id;
    }

    public void setAnswer_id(int answer_id) {
        this.answer_id = answer_id;
    }

    public Survey getSurvey() {
        return survey;
    }

    public void setSurvey(Survey survey) {
        this.survey = survey;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }


    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("answer_id", answer_id);
        //json.add("survey", survey.toJson());
        //json.add("user", user.toJson());
        json.addProperty("rating", rating);
        return json;
    }

}
