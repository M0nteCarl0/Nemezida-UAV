package com.ellize.incident;

public class User {
    public String login;
    public String psw;
    public String token;
    public String fio;
    public String inn;
    public String company;
    public String code;
    public String avatar_url;
    public String locale;

    public boolean isLogined;
    public User(String login, String psw) {
        this.login = login;
        this.psw = psw;
        isLogined = false;
        token = null;
        code = "";
        avatar_url = "";
        locale = "";

    }
}
