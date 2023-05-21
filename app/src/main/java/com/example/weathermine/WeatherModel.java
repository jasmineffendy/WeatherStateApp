package com.example.weathermine;

public class WeatherModel {
    private String kondisi, date;
    private int img;

    public WeatherModel(String kondisi, String date, int img) {
        this.kondisi = kondisi;
        this.date = date;
        this.img = img;
    }

    public String getKondisi() {
        return kondisi;
    }

    public void setKondisi(String kondisi) {
        this.kondisi = kondisi;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }
}
