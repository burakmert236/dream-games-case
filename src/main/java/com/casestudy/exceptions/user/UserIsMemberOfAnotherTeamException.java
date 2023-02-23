package com.casestudy.exceptions.user;

public class UserIsMemberOfAnotherTeamException extends RuntimeException {

    public UserIsMemberOfAnotherTeamException() {
        super();
    }

    public UserIsMemberOfAnotherTeamException(String message) {
        super(message);
    }
}
