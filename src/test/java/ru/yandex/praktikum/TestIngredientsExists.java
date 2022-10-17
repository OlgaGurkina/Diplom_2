package ru.yandex.praktikum;

import com.google.gson.Gson;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Objects;

public class TestIngredientsExists {


    @Test
    @DisplayName("need to check that Ingredients used in test exists in system")
    public void checkIngredientsExists(){

         Ingredients ingredients = RestAssured.with()
                .header("Content-Type","application/json")
                .baseUri(ApiClient.BASE_URL)
                .get("/api/ingredients")
                .then().extract()
                .body().as(Ingredients.class);
        List<Ingredient> actual = ingredients.getData();

        boolean firstIngredientExists = false;
        boolean secondIngredientExists = false;
         for(int i=0; i < 15; i++){
             Ingredient testIngredient = actual.get(i);
             if(testIngredient.get_id().equals("61c0c5a71d1f82001bdaaa6f"))
             {   firstIngredientExists = true;
                 System.out.println("FirstIngredient is in List of Ingredients");
             } else if (testIngredient.get_id().equals("61c0c5a71d1f82001bdaaa71")) {
                 secondIngredientExists = true;
                 System.out.println("SecondIngredient is in List of Ingredients");
             }
         }
        Assert.assertTrue(firstIngredientExists);
        Assert.assertTrue(secondIngredientExists);

    }

}
