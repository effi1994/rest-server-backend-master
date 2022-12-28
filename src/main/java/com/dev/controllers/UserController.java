package com.dev.controllers;
import com.dev.objects.UserObject;
import com.dev.responses.*;
import com.dev.utils.Persist;
import com.dev.utils.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.dev.utils.Constants;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    public Utils utils;


    @Autowired
    private Persist persist;

    @PostConstruct
    public void init() {
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
