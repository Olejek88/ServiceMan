package ru.shtrm.serviceman.retrofit.iface;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import ru.shtrm.serviceman.data.Token;

public interface ITokenService {
    @FormUrlEncoded
    @POST("/auth/request")
    Call<Token> getToken(@Field("login") String usersUuid, @Field("pin") String pinMd5Hash);
}
