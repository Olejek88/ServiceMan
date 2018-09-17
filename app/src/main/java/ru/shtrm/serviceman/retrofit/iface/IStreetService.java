package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.Street;

public interface IStreetService {
    @GET("/street")
    Call<List<Street>> getUsers(@Query("changedAfter") String changeAfter);
}
