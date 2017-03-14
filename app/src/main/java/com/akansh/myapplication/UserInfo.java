package com.akansh.myapplication;

import java.util.ArrayList;

/**
 * Created by akansh on 14-11-2016.
 */



public class UserInfo {

    public ArrayList<String> att_name,att_val,possibleValues;
    public String predVal,actualVal="NIL",diseaseName,timeStamp;

    public UserInfo(){}

    public ArrayList<String> getAtt_name() {
        return att_name;
    }

    public void setAtt_name(ArrayList<String> att_name) {
        this.att_name = att_name;
    }

    public ArrayList<String> getAtt_val() {
        return att_val;
    }

    public void setAtt_val(ArrayList<String> att_val) {
        this.att_val = att_val;
    }

    public String getPredVal() {
        return predVal;
    }

    public void setPredVal(String predVal) {
        this.predVal = predVal;
    }

    public String getActualVal() {
        return actualVal;
    }

    public void setActualVal(String actualVal) {
        this.actualVal = actualVal;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public UserInfo(ArrayList<String> s, ArrayList<String> s2, String p, String d, String t,ArrayList<String> possibleValues) {
        this.att_name=s2;
        this.att_val=s;
        this.predVal=p;
        this.diseaseName=d;
        this.timeStamp=t;
        this.possibleValues=possibleValues;
    }





}
