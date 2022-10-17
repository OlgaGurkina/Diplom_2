package ru.yandex.praktikum;

public class LogOut {
    private String token;



    public LogOut(String refreshToken) {
        this.token = refreshToken;
    }

    public void setToken(String refreshToken) {
        this.token = refreshToken;
    }
    public String getToken() {
        return token;
    }
}
