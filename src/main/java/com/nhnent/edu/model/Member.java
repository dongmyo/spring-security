package com.nhnent.edu.model;

// TODO : #5 Member DTO
public class Member {
    private String name;
    private String password;


    public Member() {
        // nothing
    }

    public Member(String name, String password) {
        this.name = name;
        this.password = password;
    }


    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

}
