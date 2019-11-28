package com.example.fitness_app.models;

import java.util.List;

public class BenchmarkCategories
{
    private List<String> categories;

    public BenchmarkCategories()
    {
    }

    public BenchmarkCategories(List<String> categories)
    {
        this.categories = categories;
    }

    public List<String> getCategories()
    {
        return categories;
    }

    public void setCategories(List<String> categories)
    {
        this.categories = categories;
    }

    @Override
    public String toString()
    {
        return "BenchmarkCategories{" +
                "categories=" + categories +
                '}';
    }
}
