package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.ZhObjectStatus;

public interface IZhObjectStatusService {
    @GET("/object-status")
    Call<List<ZhObjectStatus>> getData(@Query("changedAfter") String changeAfter);
}
