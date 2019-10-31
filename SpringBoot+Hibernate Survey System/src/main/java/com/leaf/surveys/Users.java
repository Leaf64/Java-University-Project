package com.leaf.surveys;


import com.google.gson.JsonObject;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "users")
public class Users  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    int user_id;

    @Column(name = "username")
    private String username;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Survey> surveys;

    @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
    private List<Answer> answers;


    public Users() {
        super();
    }

    public Users(String username) {
        super();
        this.username = username;
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }




    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int id) {
        this.user_id = id;
    }

    public JsonObject toJson(){
        JsonObject json = new JsonObject();
        json.addProperty("user_id", user_id);
        json.addProperty("username",username);
        return json;
    }

}