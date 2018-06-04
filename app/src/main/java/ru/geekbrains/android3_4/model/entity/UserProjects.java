package ru.geekbrains.android3_4.model.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserProjects {

    @SerializedName("svn_url")
    @Expose
    private String svnUrl;

    public String getUrl() {
        return svnUrl;
    }
}