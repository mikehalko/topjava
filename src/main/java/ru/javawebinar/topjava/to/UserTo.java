package ru.javawebinar.topjava.to;

import java.util.Date;

public class UserTo {
    private Integer id;

    private Date registered;

    private String name;

    private String email;

    private int caloriesPerDay;

    public UserTo(Integer id, Date registered, String name, String email, int caloriesPerDay) {
        this.id = id;
        this.registered = registered;
        this.name = name;
        this.email = email;
        this.caloriesPerDay = caloriesPerDay;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getRegistered() {
        return registered;
    }

    public void setRegistered(Date registered) {
        this.registered = registered;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCaloriesPerDay() {
        return caloriesPerDay;
    }

    public void setCaloriesPerDay(int caloriesPerDay) {
        this.caloriesPerDay = caloriesPerDay;
    }
}
