package com.dev.controllers;

import com.dev.objects.GamesObject;
import com.dev.responses.*;
import com.dev.utils.Persist;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.dev.utils.Constants;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@RestController
public class GamesController {


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



    @PostMapping(value = "/add-games")
    public BasicResponse addGames(@RequestBody List<GamesObject> newGamesObjects) {
        BasicResponse gameAddedResponse = null;
       List<GamesObject> liveGames = persist.getGamesHibernate(true);
        int errorCode = utils.checkIfTeamExistInGames(newGamesObjects,liveGames);
       if (errorCode ==0){
           List<GamesObject> gamesLive = new ArrayList<>();
         gamesLive=  persist.addGamesHibernate(newGamesObjects);
           gameAddedResponse = new GamesResponse(true, Constants.ERROR_CODE_ZERO,gamesLive);
       }else {
           gameAddedResponse = new BasicResponse(false, errorCode);
       }
        return gameAddedResponse;
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
        persist.endOfGamesHibernate(gamesObjects);
        basicResponse = new BasicResponse(true, Constants.ERROR_CODE_ZERO);
        return basicResponse;
    }



}

