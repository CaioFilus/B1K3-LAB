package com.npdeas.b1k3labapp.Database.Structs;

import com.npdeas.b1k3labapp.Database.Enuns.Deficiency;
import com.npdeas.b1k3labapp.Database.Enuns.Gender;
import com.npdeas.b1k3labapp.Database.Enuns.Scholarity;

public class UserForm {

    private Gender gender;
    private int age;
    private Scholarity scholarity;
    private int income;
    private String state;
    private String city;
    private String district;
    private Deficiency whatDefcy;

    private boolean isSended = false;



    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Scholarity getScholarity() {
        return scholarity;
    }

    public void setScholarity(Scholarity scholarity) {
        this.scholarity = scholarity;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public Deficiency getWhatDefcy() {
        return whatDefcy;
    }

    public void setWhatDefcy(Deficiency whatDefcy) {
        this.whatDefcy = whatDefcy;
    }

    public boolean isSended() {
        return isSended;
    }

    public void setSended(boolean sended) {
        isSended = sended;
    }
}
