package com.example.fitness_app.models;

public class Account
{
    private int exp;
    private String userType;

    public Account()
    {
    }

    public Account(int exp, String userType)
    {
        this.exp = exp;
        this.userType = userType;
    }

    public int getExp()
    {
        return exp;
    }

    public void setExp(int exp)
    {
        this.exp = exp;
    }

    public String getUserType()
    {
        return userType;
    }

    public void setUserType(String userType)
    {
        this.userType = userType;
    }

    @Override
    public String toString()
    {
        return "Account{" +
                "exp=" + exp +
                ", userType='" + userType + '\'' +
                '}';
    }
}
