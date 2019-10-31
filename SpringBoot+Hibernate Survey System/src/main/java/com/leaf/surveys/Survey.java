package com.leaf.surveys;

import com.google.gson.JsonObject;


import javax.persistence.*;
import java.io.Serializable;
import java.util.List;



@Entity
@Table(name = "survey")
public class Survey implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "survey_id")
    private int survey_id;

    @Column(name = "survey_title")
    private String survey_title;

    @Column(name = "question")
    private String question;


    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private Users user;


    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL)
    private List<Answer> answers;

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public Survey() {
        super();
    }

    public Survey(Users user, String survey_title, String question) {
        super();
        this.survey_title = survey_title;
        this.question = question;
        this.user = user;
    }


    public int getSurvey_id() {
        return survey_id;
    }

    public void setSurvey_id(int survey_id) {
        this.survey_id = survey_id;
    }

    public String getSurvey_title() {
        return survey_title;
    }

    public void setSurvey_title(String survey_title) {
        this.survey_title = survey_title;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }



    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("survey_id", survey_id);
        jsonObject.addProperty("survey_title", survey_title);
        jsonObject.addProperty("question", question);
        jsonObject.add("user", user.toJson());
        return jsonObject;
    }
}
