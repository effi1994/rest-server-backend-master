
package com.dev.utils;

import com.dev.objects.GamesObject;
import com.dev.objects.TeamsObject;
import com.dev.objects.User;
import com.dev.objects.UserObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.dev.utils.Constants;
import javax.annotation.PostConstruct;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Component
public class Persist {

    private Connection connection;

    private final SessionFactory sessionFactory;

    @Autowired
    public Persist (SessionFactory sf) {
        this.sessionFactory = sf;
    }




    @PostConstruct
    public void createConnectionToDatabase () {
        try {
            this.connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/football_project", "root", "1234");
            System.out.println("Successfully connected to DB");
          this.defaultDBContent();

            System.out.println();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void defaultDBContent (){
        if (this.isTableUsersEmpty()){
            Utils utils =new Utils();
            String token = utils.createHash("admin", "123456");
            UserObject userObject=new UserObject("admin",token);
            this.addUserHibernate(userObject);
        }

        if (this.isTableTeamsEmpty()){
            List<TeamsObject> teamObjects=new ArrayList<>();
            String [] teamNames = {
                    "Manchester-United",
                    "LiverPoll",
                    "Barcelona",
                    "Inter",
                    "FC Bayern Munchen",
                    "Chelsea",
                    "Real Madrid CF",
                    "Manchester City",
                    "Paris Saint-Germain",
                    "Juventus",
                    "Sevilla FC",
                    "Roma"} ;
            Utils utils =new Utils();
            String token = utils.createHash("admin", "123456");
            UserObject userObject=new UserObject("admin",token);
            userObject.setId(1);

            for (int i = 0; i < teamNames.length; i++) {
                TeamsObject teamObject=new  TeamsObject(0,0,0,0,teamNames[i],0,userObject);
                teamObjects.add(teamObject);
            }
            this.addTeamsHibernate(teamObjects);

        }
    }

    public void addTeamsHibernate (List<TeamsObject>  teamObjects){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for (TeamsObject teamObject : teamObjects) {
            session.save(teamObject);
        }
        session.getTransaction().commit();
        session.close();
    }


    public List<TeamsObject> getAllTeamsHibernate() {
        List<TeamsObject> allTeams = new ArrayList<>();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        allTeams = session.createQuery("from TeamsObject").list();
        session.getTransaction().commit();
        session.close();
        return allTeams;
    }



    public List<GamesObject> getGamesHibernate(boolean live){
        List<GamesObject> gamesObjects = new ArrayList<>();
        Session session = sessionFactory.openSession();
        gamesObjects = session.createQuery("FROM GamesObject where live=:live").
                setParameter("live",live)
                .list();
        session.close();
        return gamesObjects;
    }

    public List<GamesObject> getAllGamesHibernate(){
        List<GamesObject> gamesObjects = new ArrayList<>();
        Session session = sessionFactory.openSession();
        gamesObjects = session.createQuery("FROM GamesObject").list();
        session.close();
        return gamesObjects;
    }

    public List<GamesObject> addGamesHibernate(List<GamesObject> gamesObjects){
        List<GamesObject> gamesObjects1 = new ArrayList<>();
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for (GamesObject gamesObject : gamesObjects) {
            int id = (int) session.save(gamesObject);
              gamesObject.setId(id);
            gamesObjects1.add(gamesObject);
        }
        session.getTransaction().commit();
        session.close();
        return gamesObjects1;
    }


    public void updateGameHibernate(GamesObject gamesObject){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.update(gamesObject);
        session.getTransaction().commit();
        session.close();
    }

    public void endOfGamesHibernate(List<GamesObject> gamesObjects){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for (GamesObject gamesObject : gamesObjects) {
            gamesObject.setLive(false);
            session.update(gamesObject);
        }
        session.getTransaction().commit();
        session.close();
    }








    public void updateTeamsHibernate(List<TeamsObject> teamsObjects){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        for (TeamsObject teamsObject : teamsObjects) {
            session.update(teamsObject);
        }
        session.getTransaction().commit();
        session.close();
    }

    public void addUserHibernate(UserObject userObject){
        Session session = sessionFactory.openSession();
        session.save(userObject);
        session.close();

    }

    public  boolean isTableUsersEmpty(){
        boolean isEmpty=false;
        Session session = sessionFactory.openSession();
        List<UserObject> userObjects = session.createQuery("FROM UserObject ").list();
        if (userObjects.size()==Constants.ZERO_OBJECT_SIZE){
            isEmpty=true;
        }
        session.close();
        return isEmpty;
    }

    public boolean isTableTeamsEmpty(){
        boolean isEmpty=false;
        Session session = sessionFactory.openSession();
        List<TeamsObject> teamObjects = session.createQuery("FROM TeamsObject ").list();
        if (teamObjects.size()==Constants.ZERO_OBJECT_SIZE){
            isEmpty=true;
        }
        session.close();
        return isEmpty;
    }

    public UserObject getUserByLoginHibernate(String username, String token){
        UserObject userObject = null;
        Session session = sessionFactory.openSession();
        userObject = (UserObject) session.createQuery("FROM UserObject where  username=:username AND token=:token").
                   setParameter("username",username).setParameter("token",token)
                .uniqueResult();
        session.close();
        return userObject;
    }


    public UserObject getUserByTokenHibernate(String token){
        UserObject userObject = null;
        Session session = sessionFactory.openSession();
        userObject = (UserObject) session.createQuery("FROM UserObject where token=:token").setParameter("token",token)
                .uniqueResult();
        session.close();
        return userObject;
    }


}
