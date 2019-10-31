package com.leaf.surveys.web;


import com.google.gson.JsonObject;
import com.leaf.surveys.*;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@RestController
public class ApiRestController {

    @RequestMapping("/hello")
    public String sayHello(@RequestParam(value = "name") String name) {
        return "Hello " + name + "!";
    }

    @RequestMapping(value = "/api/user", method = RequestMethod.POST)
    public String createUser(@RequestParam("username") String username) {
        Integer userID = UsersHandler.createUser(username);
        System.err.println(userID);
        return  UsersHandler.getUser(userID).toJson().toString();

    }

    @RequestMapping(value = "/api/survey", method = RequestMethod.POST)
    public String createSurvey(@RequestParam("user_id") int user_id, @RequestParam("title") String title, @RequestParam("question") String question) {
        Integer surveyID = SurveyHandler.createSurvey(user_id,title,question);
        System.err.println("SURVEY ID TO: " + surveyID);
        return  SurveyHandler.getSurvey(surveyID).toJson().toString();
    }

    @RequestMapping(value = "api/survey/user/{id}", method = RequestMethod.GET)
    public String getUserSurveysByUserId(@PathVariable int id){

        List<Survey> results = SurveyHandler.getSurveysByUserId(id);

        JsonObject json = new JsonObject();
        for(Survey survey: results){
            json.add(survey.getSurvey_title(),survey.toJson());
        }
        return json.toString();
    }

    @RequestMapping(value = "api/survey/", method = RequestMethod.GET)
    public String getUserSurveysByUserId(){

        List<Survey> results = SurveyHandler.getAllSurveys();

        JsonObject json = new JsonObject();
        for(Survey survey: results){
            json.add(survey.getSurvey_title(),survey.toJson());
        }
        return json.toString();
    }

    @RequestMapping(value = "/api/answer", method = RequestMethod.POST)
    public String createAnswer(@RequestParam("user_id") int user_id, @RequestParam("survey_id") int survey_id, @RequestParam("rating") int rating) {

        Integer answerID = AnswerHandler.createAnswer(user_id,survey_id,rating);
        System.err.println("ANSWER ID TO:" + answerID);
        return  AnswerHandler.getAnswer(answerID).toJson().toString();
    }

    @RequestMapping(value = "api/survey/{id}",method = RequestMethod.DELETE)
    public String removeSurvey(@PathVariable int id){
        boolean done = SurveyHandler.deleteSurvey(id);
        if(done){
            return "204";
        }else{
            return "nie usunieto";
        }
    }

    @RequestMapping(value = "api/answer/{id}", method = RequestMethod.GET)
    public String getAnswer(@PathVariable int id){

        Answer result = AnswerHandler.getAnswer(id);
        return result.toJson().toString();
    }

    @RequestMapping(value = "api/answer/{id}", method = RequestMethod.PUT)
    public String editAnswer(@PathVariable int id, @RequestParam("rating") int rating){
        return AnswerHandler.editAnswer(id,rating).toJson().toString();
    }

    @RequestMapping(value = "api/stats/user/{id}", method = RequestMethod.GET)
    String getUserStats(@PathVariable int id){

        return UsersHandler.getUserStats(id);
    }

    @RequestMapping(value = "api/stats", method = RequestMethod.GET)
    String getApiStats(){

       JsonObject jsonObject = new JsonObject();
       List<Survey> surveys = SurveyHandler.getAllSurveys();
       jsonObject.addProperty("Amount of surveys:", surveys.size() );



       return jsonObject.toString();
    }

}
