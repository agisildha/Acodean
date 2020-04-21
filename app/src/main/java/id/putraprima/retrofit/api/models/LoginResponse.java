package id.putraprima.retrofit.api.models;

public class LoginResponse {
    public String token, token_type;
    public int expiresIn;

    public LoginResponse(String token, String token_type, int expiresIn) {
        this.token = token;
        this.token_type = token_type;
        this.expiresIn = expiresIn;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getTokenType() {
        return token_type;
    }

    public void setTokenType(String token_type) {
        this.token_type = token_type;
    }

    public int getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(int expiresIn) {
        this.expiresIn = expiresIn;
    }
}
