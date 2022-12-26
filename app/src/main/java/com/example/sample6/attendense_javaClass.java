package com.example.sample6;

import java.util.Date;

public class attendense_javaClass {
    String date,PorA;

    public attendense_javaClass(String date,String PorA) {
        this.date = date;
        this.PorA = PorA;
    }

    public String getPorA() {
        return PorA;
    }

    public void setPorA(String porA) {
        PorA = porA;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
