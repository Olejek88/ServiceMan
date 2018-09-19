package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.Flat;

public interface IFlatService {
    @GET("/flat")
    Call<List<Flat>> getData(@Query("changedAfter") String changeAfter);
}
