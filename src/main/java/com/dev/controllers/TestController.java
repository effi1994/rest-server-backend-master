package com.dev.controllers;

import com.dev.objects.GamesObject;
import com.dev.objects.TeamsObject;
import com.dev.objects.User;
import com.dev.responses.BasicResponse;
import com.dev.responses.GameAddedResponse;
import com.dev.responses.SignInResponse;
import com.dev.utils.Persist;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class TestController {

    private List<User> myUsers = new ArrayList<>();

    @Autowired
    public Utils utils;


    @Autowired
    private Persist persist;

    @PostConstruct
    public void init () {
    }


   /* @RequestMapping(value = "/check", method = RequestMethod.GET)
    public String getCheck () {
        return "Success from get request";
    }

    @RequestMapping(value = "/check", method = RequestMethod.POST)
    public String postCheck () {
        return "Success from post request";
    }


    @RequestMapping(value = "/get-all-users", method = {RequestMethod.GET, RequestMethod.POST})
    public List<User> getAllUsers () {
        List<User> allUsers = persist.getAllUsers();
        return allUsers;
    }*/

    @RequestMapping(value = "/get-live-games", method=RequestMethod.GET)
    public List<GamesObject> getLiveGames() {
        List<GamesObject> liveGames =  persist.getGamesHibernate(true);
        return liveGames;
    }

    @RequestMapping(value = "/get-ended-games", method=RequestMethod.GET)
    public List<GamesObject> getEndedGames() {
        List<GamesObject> endedGames = persist.getGamesHibernate(false);
        return endedGames;
    }

    @RequestMapping(value = "/get-all-teams", method=RequestMethod.GET)
    public List<TeamsObject> getAllTeams() {
        List<TeamsObject> allTeams = persist.getAllTeamsHibernate();
        return allTeams;
    }

    @RequestMapping(value = "/add-game", method=RequestMethod.POST)
    public GameAddedResponse addGame(
                                     String gameSession, String homeTeam,
                                     String foreignTeam, String homeScore,
                                     String foreignScore, Boolean isLive,
                                     String userId
    ) {
        GameAddedResponse gameAddedResponse = null;
        List<GamesObject> liveGames = persist.getGamesHibernate(true);
        List<GamesObject> endedGames = persist.getGamesHibernate(false);
        List<GamesObject> allGames = new ArrayList<>();
        HashMap<Integer, Integer> errorMap = new HashMap();
        allGames.addAll(liveGames);
        allGames.addAll(endedGames);
        for (GamesObject gamesObject : allGames) {
            if (gamesObject.getGameSession().equals(gameSession)) {

            }
        }
        // todo: optimize the oop
       if (success){
           persist.addGamesHibernate(newGamesObject);
           gameAddedResponse = new BasicResponse(true, 0);
       }else {
           gameAddedResponse = new GameAddedResponse(false,1,errorMap);
       }

        return gameAddedResponse;
    }



/*    @RequestMapping(value = "/sign-in", method = RequestMethod.POST)
    public BasicResponse signIn (String username, String password) {
        BasicResponse basicResponse = null;
        String token = createHash(username, password);
        token = persist.getUserByCreds(username, token);
        if (token == null) {
            if (persist.usernameAvailable(username)) {
                basicResponse = new BasicResponse(false, 1);
            } else {
                basicResponse = new BasicResponse(false, 2);
            }
        } else {
            User user = persist.getUserByToken(token);
            basicResponse = new SignInResponse(true, null, user);
        }
        return basicResponse;
    }*/

  /*  @RequestMapping(value = "/create-account", method = {RequestMethod.GET, RequestMethod.POST})
    public User createAccount (String username, String password) {
        User newAccount = null;
        if (utils.validateUsername(username)) {
            if (utils.validatePassword(password)) {
                if (persist.usernameAvailable(username)) {
                    String token = createHash(username, password);
                    newAccount = new User(username, token);
                    //persist.addUser(username, token);
                } else {
                    System.out.println("username already exits");
                }
            } else {
                System.out.println("password is invalid");
            }
        } else {
            System.out.println("username is invalid");
        }
        return newAccount;
    }*/


    public String createHash (String username, String password) {
        String raw = String.format("%s_%s", username, password);
        String myHash;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(raw.getBytes());
            byte[] digest = md.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return myHash;
    }

    @RequestMapping(value = "/end-games", method = RequestMethod.POST)
    public void endGames(List<GamesObject> gamesObjects) {
       persist.endOfGamesHibernate(gamesObjects);
    }

    @RequestMapping(value = "/update-teams", method = RequestMethod.POST)
    public void updateTeams(List<TeamsObject> teamsObjects){
        persist.updateTeamsHibernate(teamsObjects);
    }






    private boolean checkIfUsernameExists (String username) {
        boolean exists = false;
        for (User user : this.myUsers) {
            if (user.getUsername().equals(username)) {
                exists = true;
                break;
            }
        }

        return exists;
    }


/*    @RequestMapping(value = "/save-note", method = {RequestMethod.GET, RequestMethod.POST})
    private User getUserByToken (String token) {
        User matchedUser = null;
        if (token != null) {
            for (User user : this.myUsers) {
                if (user.getToken().equals(token)) {
                    matchedUser = user;
                    break;
                }
            }
        }
        return matchedUser;
    }




}
