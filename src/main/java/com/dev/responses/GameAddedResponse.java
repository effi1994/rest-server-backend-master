package com.dev.responses;

import com.dev.objects.GamesObject;

import java.util.HashMap;
import java.util.List;

public class GameAddedResponse extends BasicResponse {
    HashMap <Integer,Integer> errorMap;

   public GameAddedResponse(boolean success, Integer errorCode, HashMap <Integer,Integer> errorMap) {
       super(success, errorCode);
       this.errorMap = errorMap;
    }
}
