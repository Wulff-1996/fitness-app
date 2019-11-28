package com.example.fitness_app.models;

public class Benchmark
{
    private String date;
    private String exerciseCategory;
    private Float value;

    public Benchmark()
    {
    }

    public Benchmark(String date, String exerciseCategory, Float value)
    {
        this.date = date;
        this.exerciseCategory = exerciseCategory;
        this.value = value;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getExerciseCategory()
    {
        return exerciseCategory;
    }

    public void setExerciseCategory(String exerciseCategory)
    {
        this.exerciseCategory = exerciseCategory;
    }

    public Float getValue()
    {
        return value;
    }

    public void setValue(Float value)
    {
        this.value = value;
    }
}
