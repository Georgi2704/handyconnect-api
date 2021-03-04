package com.georgivasil.springjwt.models;

public class AlreadyExistsError {

    private boolean emailAlreadyExists;
    private boolean userAlreadyExists;

    public AlreadyExistsError() {
        this.emailAlreadyExists = false;
        this.userAlreadyExists = false;
    }

    public boolean isEmailAlreadyExists() {
        return emailAlreadyExists;
    }

    public void setEmailAlreadyExists(boolean emailAlreadyExists) {
        this.emailAlreadyExists = emailAlreadyExists;
    }

    public boolean isUserAlreadyExists() {
        return userAlreadyExists;
    }

    public void setUserAlreadyExists(boolean userAlreadyExists) {
        this.userAlreadyExists = userAlreadyExists;
    }

    public boolean hasAnyErrors(){
        if (this.userAlreadyExists || this.emailAlreadyExists){
            return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return '{' + "emailAlreadyExists=" + String.valueOf(emailAlreadyExists) +
                ", userAlreadyExists=" + String.valueOf(userAlreadyExists) +
                '}';
    }
}
