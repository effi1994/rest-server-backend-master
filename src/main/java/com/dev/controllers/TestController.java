package com.dev.controllers;

import com.dev.objects.GamesObject;
import com.dev.objects.TeamsObject;
import com.dev.objects.UserObject;
import com.dev.responses.*;
import com.dev.utils.Persist;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.dev.utils.Constants;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
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
            getLiveGamesResponse = new GamesResponse(true, Constants.ERROR_CODE_ZERO,liveGames);
        } else {
            getLiveGamesResponse = new BasicResponse(false, Constants.ERROR_CODE_ONE);
        }

        return getLiveGamesResponse;
    }

    @RequestMapping(value = "/get-end-games", method = RequestMethod.GET)
    public BasicResponse getEndedGames() {
        BasicResponse getEndedGamesResponse = null;
        List<GamesObject> endedGames = persist.getGamesHibernate(false);
          if (endedGames != null) {
            getEndedGamesResponse = new GamesResponse(true, Constants.ERROR_CODE_ZERO,endedGames);
        } else {
            getEndedGamesResponse = new BasicResponse(false, Constants.ERROR_CODE_ONE);
        }
        return getEndedGamesResponse;

    }

    @RequestMapping(value = "/get-all-games", method = RequestMethod.GET)
     public BasicResponse getAllGames() {
        System.out.println("getAllGames");
        BasicResponse getAllGamesResponse = null;
        List<GamesObject> allGames = persist.getAllGamesHibernate();
        if (allGames != null) {
            getAllGamesResponse = new GamesResponse(true, Constants.ERROR_CODE_ZERO,allGames);
        } else {
            getAllGamesResponse = new BasicResponse(false, Constants.ERROR_CODE_ONE);
        }
        return getAllGamesResponse;

    }


    @RequestMapping(value = "/get-all-teams", method = RequestMethod.GET)
    public BasicResponse getAllTeams() {
        BasicResponse getTeamsResponse = null;
        List<TeamsObject> allTeams = persist.getAllTeamsHibernate();
        if (allTeams != null) {
            getTeamsResponse = new TeamsResponse(true, Constants.ERROR_CODE_ZERO, allTeams);

        } else {
            getTeamsResponse = new BasicResponse(false, Constants.ERROR_CODE_ONE);
        }

        return getTeamsResponse;
    }

    @PostMapping(value = "/add-games")
    public BasicResponse addGames(@RequestBody List<GamesObject> newGamesObject) {
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
                errorMap.put(newGameObject.getId(), Constants.ERROR_MAP_ONE);
                success = false;

            }
            if (allGames.size() > Constants.ZERO) {
                for (GamesObject existGame : allGames) {
                    if (existGame.getGameSession() == newGameObject.getGameSession()) {
                        errorMap.put(newGameObject.getId(),Constants.ERROR_MAP_TWO);
                        success = false;
                    }
                }
            }
        }
        // todo: optimize the oop
       if (success){
           List<GamesObject> gamesLive = new ArrayList<>();
         gamesLive=  persist.addGamesHibernate(newGamesObject);
           gameAddedResponse = new GamesResponse(true, Constants.ERROR_CODE_ZERO,gamesLive);
       }else {
           gameAddedResponse = new GameAddedResponse(false,Constants.ERROR_CODE_ONE,errorMap);
       }

        return gameAddedResponse;
    }


    @RequestMapping(value = "/log-in", method = RequestMethod.POST)
    public BasicResponse logIn(String username, String password) {
        BasicResponse basicResponse;
        String token = utils.createHash(username, password);
        UserObject userObject = persist.getUserByLoginHibernate(username, token);
        if (userObject != null) {
            basicResponse = new SignInResponse(true,Constants.ERROR_CODE_ZERO,userObject);
        } else {
            basicResponse = new BasicResponse(false, Constants.ERROR_CODE_ONE);
        }
        return basicResponse;
    }

    @RequestMapping(value = "/update-game", method = RequestMethod.POST)
    public BasicResponse updateGame(GamesObject gamesObject) {
        BasicResponse basicResponse = null;
            if (gamesObject.getHomeScore() >= Constants.ZERO && gamesObject.getForeignScore() >= Constants.ZERO) {
                persist.updateGameHibernate(gamesObject);
                basicResponse = new BasicResponse(true, Constants.ERROR_CODE_ZERO);
            }else {
                basicResponse = new BasicResponse(false, Constants.ERROR_CODE_ONE);
            }

        return basicResponse;
    }

    @PostMapping(value = "/end-games")
    public BasicResponse endGames(  @RequestBody List<GamesObject> gamesObjects) {
        BasicResponse basicResponse = null;
        System.out.println("pp");
        persist.endOfGamesHibernate(gamesObjects);
        basicResponse = new BasicResponse(true, Constants.ERROR_CODE_ZERO);
        return basicResponse;
    }


@RequestMapping(value = "/get-user-by-token",method = RequestMethod.POST)
    public BasicResponse getUserByToken(String token){
         BasicResponse basicResponse=null;
        UserObject userObject = persist.getUserByTokenHibernate(token);
        if (userObject!=null) {
            basicResponse = new SignInResponse(true, Constants.ERROR_CODE_ZERO, userObject);
        }else {
            basicResponse = new BasicResponse(false,Constants.ERROR_CODE_ONE);
        }
        return basicResponse;
    }
}