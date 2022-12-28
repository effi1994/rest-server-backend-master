package com.dev.controllers;
import com.dev.objects.TeamsObject;
import com.dev.responses.*;
import com.dev.utils.Persist;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.dev.utils.Constants;

import javax.annotation.PostConstruct;
import java.util.List;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class TeamsController {
    @Autowired
    public Utils utils;


    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
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


}
