package com.casestudy.exceptions.team;

public class TeamIsFullException extends RuntimeException{

    public TeamIsFullException() {
        super();
    }

    public TeamIsFullException(String message) {
        super(message);
    }
}
