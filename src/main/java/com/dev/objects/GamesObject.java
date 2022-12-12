package com.dev.objects;
import javax.persistence.*;
@Entity
@Table(name = "games")
public class GamesObject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private int id;

    @Column
    private String gameSession;

    @Column
    private String homeTeam;

    @Column
    private String foreignTeam;

    @Column
    private int homeScore;

    @Column
    private int foreignScore;

    @Column
    private Boolean isLive;
    @Column
    private String userId;



    public GamesObject( String gameSession, String homeTeam, String foreignTeam, int homeScore, int foreignScore, Boolean isLive, String userId) {

        this.gameSession = gameSession;
        this.homeTeam = homeTeam;
        this.foreignTeam = foreignTeam;
        this.homeScore = homeScore;
        this.foreignScore = foreignScore;
        this.isLive = isLive;
        this.userId = userId;

    }

    public GamesObject() {
    }





    public int getId() { 
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGameSession() {
        return gameSession;
    }

    public void setGameSession(String gameSession) {
        this.gameSession = gameSession;
    }

    public String getHomeTeam() {
        return homeTeam;
    }

    public void setHomeTeam(String homeTeam) {
        this.homeTeam = homeTeam;
    }

    public String getForeignTeam() {
        return foreignTeam;
    }

    public void setForeignTeam(String foreignTeam) {
        this.foreignTeam = foreignTeam;
    }

    public int getHomeScore() {
        return homeScore;
    }

    public void setHomeScore(int homeScore) {
        this.homeScore = homeScore;
    }

    public int getForeignScore() {
        return foreignScore;
    }

    public void setForeignScore(int foreignScore) {
        this.foreignScore = foreignScore;
    }

    public Boolean getLive() {
        return isLive;
    }

    public void setLive(Boolean live) {
        isLive = live;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
