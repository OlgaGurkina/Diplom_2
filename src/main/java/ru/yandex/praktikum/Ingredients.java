package ru.yandex.praktikum;

import java.util.List;

public class Ingredients {

    private Boolean success;
    private List<Ingredient> data;




    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public List<Ingredient> getData() {
        return data;
    }

    public void setData(List<Ingredient> data) {
        this.data = data;
    }
}
