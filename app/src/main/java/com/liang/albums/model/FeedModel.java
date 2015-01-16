package com.liang.albums.model;

import java.util.Date;

/**
 * Created by liang on 15/1/17.
 */
public class FeedModel {
    private String ID;
    private String FROM;
    private String MESSAGE;
    private String SCREEN_NAME;
    private Date DATE;

    public String getID() {
        return ID;
    }

    public void setID(String IMG_ID) {
        this.ID = IMG_ID;
    }

    public String getFROM() {
        return FROM;
    }

    public void setFROM(String FROM) {
        this.FROM = FROM;
    }

    public String getMESSAGE() {
        return MESSAGE;
    }

    public void setMESSAGE(String MESSAGE) {
        this.MESSAGE = MESSAGE;
    }

    public String getSCREENNAME() {
        return SCREEN_NAME;
    }

    public void setSCREENNAME(String SCREEN_NAME) {
        this.SCREEN_NAME = SCREEN_NAME;
    }

    public Date getDATE() {
        return DATE;
    }

    public void setDATE(Date DATE) {
        this.DATE = DATE;
    }
}
