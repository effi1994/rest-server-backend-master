package com.dev.responses;

import com.dev.objects.GamesObject;
import com.dev.objects.User;

public class GameAddedResponse extends BasicResponse {
    private GamesObject gamesObject;

    public GameAddedResponse(boolean success, Integer errorCode, GamesObject gamesObject) {
        super(success, errorCode);
        this.gamesObject = gamesObject;
    }
}
