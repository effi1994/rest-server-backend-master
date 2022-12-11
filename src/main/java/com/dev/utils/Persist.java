
package com.dev.utils;

import com.dev.objects.GamesObject;
import com.dev.objects.TeamsObject;
import com.dev.objects.User;
import com.dev.objects.UserObject;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
            UserObject userObject=new UserObject("admin","123456","123456");
            this.addUserHibernate(userObject);
        }

        if (this.isTableTeamsEmpty()){
            List<TeamsObject> teamObjects=new ArrayList<>();
            String [] teamNames = {
                    "Manchester-United",
                    "LiverPoll",
                    "Barcelona",
                    "Inter",
                    "FC Bayern München",
                    "Chelsea",
                    "Real Madrid CF",
                    "Manchester City",
                    "Paris Saint-Germain",
                    "Juventus",
                    "Sevilla FC",
                    "Roma"} ;
            UserObject userObject=new UserObject("admin","123456","123456");
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

    public List<User> getAllUsers() {
        List<User> allUsers = new ArrayList<>();
        try {
            ResultSet resultSet =
                    this.connection
                            .createStatement()
                            .executeQuery("SELECT username, token FROM users");
            while (resultSet.next()) {
                String token = resultSet.getString("token");
                String username = resultSet.getString("username");
                User user = new User(username, token);
                allUsers.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return allUsers;
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

    /*public List<UserObject> getAllUsersHibernate () {
        Session session = sessionFactory.openSession();
        UserObject userObject = new UserObject();
        session.save(userObject);
        List<UserObject> userObjects = session.createQuery("FROM UserObject ").list();
        session.close();
        return userObjects;
    }*/

    public boolean usernameAvailableHibernate(String username){
        boolean available = false;
        UserObject userObject = null;
        Session session = sessionFactory.openSession();
        userObject = (UserObject) session.createQuery("FROM UserObject where  username=:username").
                setParameter("username",username)
                .uniqueResult();

        if (userObject ==null){
            available=true;
        }else {
            available=false;
        }
        session.close();
        return  available;

    }

    public List<GamesObject> getGamesHibernate(boolean isLive){
        List<GamesObject> gamesObjects = new ArrayList<>();
        Session session = sessionFactory.openSession();
        gamesObjects = session.createQuery("FROM GamesObject where isLive=:isLive").
                setParameter("isLive",isLive)
                .list();
        session.close();
        return gamesObjects;
    }

    public void addGameHibernate(GamesObject gamesObject){
        Session session = sessionFactory.openSession();
        session.beginTransaction();
        session.save(gamesObject);
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
        if (userObjects.size()==0){
            isEmpty=true;
        }
        session.close();
        return isEmpty;
    }

    public boolean isTableTeamsEmpty(){
        boolean isEmpty=false;
        Session session = sessionFactory.openSession();
        List<TeamsObject> teamObjects = session.createQuery("FROM TeamsObject ").list();
        if (teamObjects.size()==0){
            isEmpty=true;
        }
        session.close();
        return isEmpty;
    }

  /* public void addUser (String username, String token) {
        try {
            PreparedStatement preparedStatement =
                    this.connection
                            .prepareStatement("INSERT INTO users (username, token) VALUE (?,?)");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, token);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean usernameAvailable (String username) {
        boolean available = false;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT id " +
                    "FROM users " +
                    "WHERE username = ?");
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                available = false;
            } else {
                available = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return available;
    }

    public User getUserByToken (String token) {
        User user = null;
        try {
            PreparedStatement preparedStatement = this.connection
                    .prepareStatement(
                            "SELECT id, username FROM users WHERE token = ?");
            preparedStatement.setString(1, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                String username = resultSet.getString("username");
                user = new User(username, token);
                user.setId(id);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return user;
    }

    public void addNote (int userId, String content) {
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("INSERT INTO notes (content, user_id) VALUE (?, ?)");
            preparedStatement.setString(1, content);
            preparedStatement.setInt(2, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
        }
    }

    public List<String> getNotesByUserId (int userId) {
        List<String> notes = new ArrayList<>();
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement("SELECT content FROM notes WHERE user_id = ?");
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                String content = resultSet.getString("content");
                notes.add(content);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return notes;
    }

    public String getUserByCreds (String username, String token) {
        String response = null;
        try {
            PreparedStatement preparedStatement = this.connection.prepareStatement(
                    "SELECT * FROM users WHERE username = ? AND token = ?");
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, token);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                response = token;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return response;
    }*/

}
