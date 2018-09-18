package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.FlatType;

public interface IFlatTypeService {
    @GET("/flat-type")
    Call<List<FlatType>> getData(@Query("changedAfter") String changeAfter);
}
