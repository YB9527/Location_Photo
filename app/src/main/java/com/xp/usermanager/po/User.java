package com.xp.usermanager.po;

import com.google.gson.annotations.Expose;
import com.xp.zjd.po.ZJD;

import java.util.List;

public class User {
    @Expose
    private Long id;
    /**
     * 账号
     */
    @Expose
    private String account;
    /**
     * 昵称
     */
    @Expose
    private String nickName;
    /**
     * 密码
     */
    @Expose
    private String password;
    /**
     * 注册日期
     */
    @Expose
    private String registDate;
    /**
     * 角色级别
     */
    @Expose
    private Level level;

    /**
     * 用户邮箱，用于找回密码
     */
    @Expose
    private String email;

    /**
     * 自己创建的宅基地
     */
    private List<ZJD> zjds;

    public User(){

    }

    public User(String account, String password) {
        this.account = account;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRegistDate() {
        return registDate;
    }

    public void setRegistDate(String registDate) {
        this.registDate = registDate;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ZJD> getZjds() {
        return zjds;
    }

    public void setZjds(List<ZJD> zjds) {
        this.zjds = zjds;
    }

    @Override
    public String toString() {
        return "{" +
                "id=" + id +
                ", account='" + account + '\'' +
                ", nickName='" + nickName + '\'' +
                ", password='" + password + '\'' +
                ", registDate='" + registDate + '\'' +
                ", level=" + level +
                ", email='" + email + '\'' +
                ", zjds=" + zjds +
                '}';
    }
}
