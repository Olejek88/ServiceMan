package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.UserHouse;

public interface IUserHouseService {
    @GET("/user-house")
    Call<List<UserHouse>> getData(@Query("changedAfter") String changeAfter);
}
