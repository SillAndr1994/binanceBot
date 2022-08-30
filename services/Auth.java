package services;

public class Auth {
    private static String api_key = "TWFtdnfIXD9bCJnqe6jrhUysiNSviRrwbFCbxphYB8XRzZ0uDttiW9fTtt2euAIr";
    private static String secret_key = "MCMaPGjCwWPvoXBl84igjJVSeuBxrWhJdLBp8BAHTM00sC3vGYQEQ5rfvlYzxElX";

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
}
