package com.dev.responses;

import com.dev.objects.User;
import com.dev.objects.UserObject;

public class SignInResponse extends BasicResponse{
    private UserObject userObject;

    public SignInResponse(boolean success, Integer errorCode, UserObject userObject) {
        super(success, errorCode);
        this.userObject = userObject;
    }

    public UserObject getUserObject() {
        return userObject;
    }

    public void setUserObject(UserObject userObject) {
        this.userObject = userObject;
    }
}
