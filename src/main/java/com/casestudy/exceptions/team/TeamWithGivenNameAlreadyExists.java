package com.casestudy.exceptions.team;

public class TeamWithGivenNameAlreadyExists extends RuntimeException{
    public TeamWithGivenNameAlreadyExists() {
        super();
    }
    public TeamWithGivenNameAlreadyExists(String message) {
        super(message);
    }
}
