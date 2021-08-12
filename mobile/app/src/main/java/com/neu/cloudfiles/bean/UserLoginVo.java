package com.neu.cloudfiles.bean;

public class UserLoginVo {
    private long userId;
    private String username;
    private String telephone;
    private String email;
    private String sex;
    private String birthday;
    private String imageUrl;
    private String registerTime;
    private String lastLoginTime;
    private String token;

    public UserLoginVo(long userId, String username, String telephone, String email, String sex, String birthday, String imageUrl, String registerTime, String lastLoginTime, String token) {
        this.userId = userId;
        this.username = username;
        this.telephone = telephone;
        this.email = email;
        this.sex = sex;
        this.birthday = birthday;
        this.imageUrl = imageUrl;
        this.registerTime = registerTime;
        this.lastLoginTime = lastLoginTime;
        this.token = token;
    }

    public UserLoginVo() {
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public String getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}