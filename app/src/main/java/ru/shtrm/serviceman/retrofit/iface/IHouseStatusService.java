package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.HouseStatus;

public interface IHouseStatusService {
    @GET("/house-status")
    Call<List<HouseStatus>> getUsers(@Query("changedAfter") String changeAfter);
}
