package com.casestudy.exceptions.user;

public class UserIsAlreadyMemberOfTheTeamException extends RuntimeException{

    public UserIsAlreadyMemberOfTheTeamException() {
        super();
    }

    public UserIsAlreadyMemberOfTheTeamException(String message) {
        super(message);
    }
}
