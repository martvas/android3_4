package ru.geekbrains.android3_4.model.entity;

import com.google.gson.annotations.Expose;

public class User {
    @Expose
    String avatarUrl;
    @Expose
    String login;
    @Expose
    String repos_url;

    public String getReposUrl() {
        return repos_url;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getLogin() {
        return login;
    }
}
