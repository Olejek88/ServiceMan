package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.User;

public interface IUsersService {
    @GET("/users")
    Call<List<User>> getUsers(@Query("changedAfter") String changeAfter);
}
