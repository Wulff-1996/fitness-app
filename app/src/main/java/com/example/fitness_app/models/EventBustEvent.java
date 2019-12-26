package com.example.fitness_app.models;

/**
 * Class for sending message event via EventBus
 * @param <T>
 */
public class EventBustEvent<T> {
    private String event;
    private T data;

    public EventBustEvent(String event, T data) {
        this.event = event;
        this.data = data;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
