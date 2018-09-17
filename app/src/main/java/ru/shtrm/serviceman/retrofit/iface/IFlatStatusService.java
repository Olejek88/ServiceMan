package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.FlatStatus;

public interface IFlatStatusService {
    @GET("/flat-status")
    Call<List<FlatStatus>> getData(@Query("changedAfter") String changeAfter);
}
