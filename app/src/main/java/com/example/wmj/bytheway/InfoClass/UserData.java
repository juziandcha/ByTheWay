package com.example.wmj.bytheway.InfoClass;

/**
 * Created by wmj on 2018/1/4.
 */

public class UserData {
    private String ID=null;
    private String Name=null;
    private String Gender=null;
    private String PhoneNumber=null;

    public UserData(){};

    public void UserData(String id,String name,String gender,String phoneNumber){
        this.ID=id;
        this.Name=name;
        this.Gender=gender;
        this.PhoneNumber=phoneNumber;
    }

    public void setName(String name){
        this.Name=name;
    }

    public void setGender(String gender){
        this.Gender=gender;
    }

    public void setPhoneNumber(String phoneNumber){
        this.PhoneNumber=phoneNumber;
    }

    public String getID(){
        return ID;
    }

    public String getName(){
        return Name;
    }

    public String getGender(){
        return Gender;
    }

    public String getPhoneNumber(){
        return PhoneNumber;
    }

}
