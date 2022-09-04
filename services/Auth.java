package services;

public class Auth {
    private static String api_key = "";
    private static String secret_key = "";
    private static String test_api_key = "Yrwg0Zg1Mxw4gYb24Q8blJ5LiKW3JiJm4ZDy8xswkYqIBdcSJi8rk3KxmabSBmJN";
    private static String test_secret_key = "ADID3QE5PC5aOjM9ClQdQk3DV6SHPa6osM6DHPbA3DieKerH1eagTpLCaheS2YM6";

    public static String getApi_key() {
        return api_key;
    }

    public static String getSecret_key() {
        return secret_key;
    }

    public void setApi_key(String api_key) {
        this.api_key = api_key;
    }

    public void setSecret_key(String secret_key) {
        this.secret_key = secret_key;
    }

    public static String getTest_api_key() {
        return test_api_key;
    }

    public static String getTest_secret_key() {
        return test_secret_key;
    }
}
