package com.neu.cloudfiles.bean;

public class UserVo {

    private String username;

    private String telephone;

    private String email;

    private String sex;

    private String birthday;

    private String imageUrl;

    private String registerTime;

    public UserVo(String username, String telephone, String email, String sex, String birthday, String imageUrl, String registerTime) {
        this.username = username;
        this.telephone = telephone;
        this.email = email;
        this.sex = sex;
        this.birthday = birthday;
        this.imageUrl = imageUrl;
        this.registerTime = registerTime;
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
}