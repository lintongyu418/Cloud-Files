package com.neu.cloudfiles.event;

public class RegisterSuccessEvent {
    public String username;
    public String password;

    public RegisterSuccessEvent(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
