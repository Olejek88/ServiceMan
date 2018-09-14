package ru.shtrm.serviceman.data;

public class AuthorizedUser {

    private User mUser;
    private String mToken;
    private static AuthorizedUser mInstance;

    public static synchronized AuthorizedUser getInstance() {
        if (mInstance == null) {
            mInstance = new AuthorizedUser();
        }

        return mInstance;
    }

    /**
     * @return the Uuid
     */
    public User getUser() {
        return mUser;
    }

    /**
     * @param user User
     */
    public void setUser(User user) {
        mUser = user;
    }

    /**
     * @return the mToken
     */
    public String getToken() {
        return mToken;
    }

    /**
     * @param token the Token to set
     */
    public void setToken(String token) {
        this.mToken = token;
    }

    /**
     * @return The bearer
     */
    public String getBearer() {
        return "Bearer " + mToken;
    }

    /**
     * Обнуляем информацию о текущем пользователе.
     */
    public void reset() {
        mToken = null;
        mUser = null;
    }
}
