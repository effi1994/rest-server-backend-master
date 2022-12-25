package com.dev.objects;


import javax.persistence.*;


@Entity
@Table(name = "teams",uniqueConstraints = {
        @UniqueConstraint(columnNames = { "name" })
})
public class TeamsObject {
    @Id

    @GeneratedValue(strategy = GenerationType.IDENTITY)

    @Column

    private int id;



    @Column (name = "name")

    private String nameTeams;




    public TeamsObject(String nameTeams,UserObject userObject) {
        this.nameTeams = nameTeams;
        this.userObject = userObject;
    }

    public  TeamsObject(){

    }

    @ManyToOne

    @JoinColumn (name = "user_id")



    private UserObject userObject;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameTeams() {
        return nameTeams;
    }

    public void setNameTeams(String nameTeams) {
        this.nameTeams = nameTeams;
    }


    public UserObject getUserObject() {
        return userObject;
    }

    public void setUserObject(UserObject userObject) {
        this.userObject = userObject;
    }
}
