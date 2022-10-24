package api.util;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomParamsForUser {
    private final int length = 5;
    private final boolean useLetters = true;
    private final boolean useNumbers = true;

    public String generatedName;
    public String generatedPassword;
    public String generatedEmail;

    public RandomParamsForUser() {
        this.generatedName = RandomStringUtils.random(length, useLetters, useNumbers);
        this.generatedPassword = RandomStringUtils.random(length, useLetters, useNumbers);
        this.generatedEmail = "test_email+" + System.nanoTime() + "@yandex.ru";
    }
    static public String randomEmail(){
        return "test_email+" + System.nanoTime() + "@yandex.ru";
    }
}
