package com.casestudy.exceptions.team;

public class TeamNotFoundException extends RuntimeException{
    public TeamNotFoundException() {
        super();
    }

    public TeamNotFoundException(String message) {
        super(message);
    }
}
