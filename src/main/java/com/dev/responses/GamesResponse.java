package com.dev.responses;

import com.dev.objects.GamesObject;

import java.util.List;

public class GamesResponse  extends BasicResponse{
    private List<GamesObject> gamesObjectList;

    public GamesResponse(boolean success, Integer errorCode, List<GamesObject> allGames) {
        super(success, errorCode);
        this.gamesObjectList = allGames;
    }

    public List<GamesObject> getGamesObjectList() {
        return gamesObjectList;
    }

    public void setGamesObjectList(List<GamesObject> gamesObjectList) {
        this.gamesObjectList = gamesObjectList;
    }
}
