package com.example.fitness_app.models;

import java.util.List;

public class UserTypes
{
    private List<String> types;

    public UserTypes()
    {
    }

    public UserTypes(List<String> types)
    {
        this.types = types;
    }

    public List<String> getTypes()
    {
        return types;
    }

    public void setTypes(List<String> types)
    {
        this.types = types;
    }

    @Override
    public String toString()
    {
        return "UserTypes{" +
                "types=" + types +
                '}';
    }
}
