package com.dev.controllers;

import com.dev.objects.GamesObject;
import com.dev.objects.TeamsObject;
import com.dev.objects.UserObject;
import com.dev.responses.*;
import com.dev.utils.Persist;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
public class TestController {


    @Autowired
    public Utils utils;


    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
    }


    @RequestMapping(value = "/get-live-games", method = RequestMethod.GET)
    public BasicResponse getLiveGames() {
        BasicResponse getLiveGamesResponse = null;
        List<GamesObject> liveGames = persist.getGamesHibernate(true);
        if (liveGames != null) {
            getLiveGamesResponse = new GamesResponse(true, 0,liveGames);
        } else {
            getLiveGamesResponse = new BasicResponse(false, 1);
        }

        return getLiveGamesResponse;
    }

    @RequestMapping(value = "/get-end-games", method = RequestMethod.GET)
    public BasicResponse getEndedGames() {
        BasicResponse getEndedGamesResponse = null;
        List<GamesObject> endedGames = persist.getGamesHibernate(false);
          if (endedGames != null) {
            getEndedGamesResponse = new GamesResponse(true, 0,endedGames);
        } else {
            getEndedGamesResponse = new BasicResponse(false, 1);
        }
        return getEndedGamesResponse;

    }

    @RequestMapping(value = "/get-all-teams", method = RequestMethod.GET)
    public BasicResponse getAllTeams() {
        BasicResponse getTeamsResponse = null;
        List<TeamsObject> allTeams = persist.getAllTeamsHibernate();
        if (allTeams != null) {
            getTeamsResponse = new TeamsResponse(true, 0, allTeams);

        } else {
            getTeamsResponse = new BasicResponse(false, 1);
        }

        return getTeamsResponse;
    }

    @RequestMapping(value = "/add-games", method = RequestMethod.POST)
    public BasicResponse addGames(List<GamesObject> newGamesObject) {
        BasicResponse gameAddedResponse = null;
        List<GamesObject> liveGames = persist.getGamesHibernate(true);
        List<GamesObject> endedGames = persist.getGamesHibernate(false);
        List<GamesObject> allGames = new ArrayList<>();
        HashMap<Integer, Integer> errorMap = new HashMap();
        allGames.addAll(liveGames);
        allGames.addAll(endedGames);
        boolean success = true;

        for (GamesObject newGameObject : newGamesObject) {
            if (!newGameObject.getLive()) {
                newGameObject.setLive(true);
            }

            if (newGameObject.getHomeTeam().equals(newGameObject.getForeignTeam())) {
                errorMap.put(newGameObject.getId(), 1);
                success = false;

            }

            for (GamesObject existGame : allGames) {
                if (existGame.getGameSession() == newGameObject.getGameSession()) {
                    errorMap.put(newGameObject.getId(),2);
                    success = false;
                }
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


    @RequestMapping(value = "/log-in", method = RequestMethod.POST)
    public BasicResponse logIn(String username, String password) {
        BasicResponse basicResponse;
        String token = utils.createHash(username, password);
        UserObject userObject = persist.getUserByLoginHibernate(username, token);
        if (userObject != null) {
            basicResponse = new SignInResponse(true, 0,userObject);
        } else {
            basicResponse = new BasicResponse(false, 1);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/update-game", method = RequestMethod.POST)
    public BasicResponse updateGame(GamesObject gamesObject) {
        BasicResponse basicResponse = null;
        if (gamesObject.getLive()) {
            if (gamesObject.getHomeScore() >= 0 && gamesObject.getForeignScore() >= 0) {
                persist.updateGameHibernate(gamesObject);
                basicResponse = new BasicResponse(true, 0);
            }else {
                basicResponse = new BasicResponse(false, 1);
            }
        }
        return basicResponse;
    }

    @RequestMapping(value = "/end-games", method = RequestMethod.POST)
    public BasicResponse endGames(List<GamesObject> gamesObjects) {
        BasicResponse basicResponse = null;
        persist.endOfGamesHibernate(gamesObjects);
        basicResponse = new BasicResponse(true, 0);
        return basicResponse;
    }

    @RequestMapping(value = "/update-teams", method = RequestMethod.POST)
    public BasicResponse updateTeams(List<TeamsObject> teamsObjects){
        BasicResponse basicResponse = null;
        persist.updateTeamsHibernate(teamsObjects);
        basicResponse = new BasicResponse(true, 0);
        return basicResponse;
    }

@RequestMapping(value = "/get-user-by-token",method = RequestMethod.POST)
    public BasicResponse getUserByToken(String token){
         BasicResponse basicResponse=null;
        UserObject userObject = persist.getUserByTokenHibernate(token);
        if (userObject!=null) {
            basicResponse = new SignInResponse(true, 0, userObject);
        }else {
            basicResponse = new BasicResponse(false,1);
        }
        return basicResponse;
    }








  /*  private boolean checkIfUsernameExists (String username) {
        boolean exists = false;
        for (User user : this.myUsers) {
            if (user.getUsername().equals(username)) {
                exists = true;
                break;
            }
        }

        return exists;
    }*/


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
 */
}