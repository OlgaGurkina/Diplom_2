package ru.yandex.praktikum;

import java.util.Arrays;
import java.util.List;

public class Order {


 //   String firstTestIngredient = "61c0c5a71d1f82001bdaaa6f";
 //  String secondTestIngredien = "61c0c5a71d1f82001bdaaa71";

    List<String> ingredients;

    public Order() {
    }

    public Order(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<String> ingredients) {
        this.ingredients = ingredients;
    }



}
