package com.dev.utils;

import com.dev.objects.GamesObject;
import org.springframework.stereotype.Component;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

@Component
public class Utils {

    //TODO: Make a validate username and password

    public boolean validateUsername (String username) {
        boolean valid = false;
        if (username != null) {
            if (username.contains("@")) {
                valid = true;
            }
        }
        return valid;
    }

    public boolean validatePassword (String password) {
        boolean valid = false;
        if (password != null) {
            if (password.length() >= 8) {
                valid = true;
            }
        }
        return valid;
    }

    public boolean validateNote (String note) {
        boolean valid = false;
        if (note != null && note.length() > 0) {
            valid = true;
        }
        return valid;
    }

    public String createHash (String username, String password) {
        String myHash;
        try {
            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            String raw = String.format("%s_%s", username, password);
            sha256.update(raw.getBytes());
            byte[] digest = sha256.digest();
            myHash = DatatypeConverter
                    .printHexBinary(digest).toUpperCase();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return myHash;
    }

    public int checkIfTeamExistInGames(List<GamesObject> newGamesObjects,List<GamesObject> liveGames){

        for (GamesObject newGameObject : newGamesObjects) {
            if (!newGameObject.getLive()) {
                newGameObject.setLive(true);
            }
            if (newGameObject.getHomeTeam().equals(newGameObject.getForeignTeam())) {
                return 1;
            }
            if (liveGames.size() >0){
                for (GamesObject liveGame : liveGames) {
                    if (liveGame.getHomeTeam().equals(newGameObject.getHomeTeam()) ||
                            liveGame.getHomeTeam().equals(newGameObject.getForeignTeam()) ||
                            liveGame.getForeignTeam().equals(newGameObject.getHomeTeam()) ||
                            liveGame.getForeignTeam().equals(newGameObject.getForeignTeam())) {
                        return 2;
                    }
                }
            }

        }
        return 0;
    }
}
