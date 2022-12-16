package com.dev.responses;

import com.dev.objects.TeamsObject;

import java.util.List;

public class TeamsResponse extends BasicResponse {
    private List<TeamsObject> teamsObjectList;

    public TeamsResponse(boolean success, Integer errorCode, List<TeamsObject> allTeams) {
        super(success, errorCode);
        this.teamsObjectList = allTeams;
    }

    public List<TeamsObject> getTeamsObjectList() {
        return teamsObjectList;
    }

    public void setTeamsObjectList(List<TeamsObject> teamsObjectList) {
        this.teamsObjectList = teamsObjectList;
    }
}
