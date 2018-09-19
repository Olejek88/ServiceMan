package ru.shtrm.serviceman.data;

public class Token {
    public static final String TOKEN_INTENT = "ru.shtrm.serviceman.token-intent";
    private String usersUuid;
    private String token;

    public String getUsersUuid() {
        return usersUuid;
    }

    public void setUsersUuid(String usersUuid) {
        this.usersUuid = usersUuid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
