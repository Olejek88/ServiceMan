package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.HouseType;

public interface IHouseTypeService {
    @GET("/house-type")
    Call<List<HouseType>> getData(@Query("changedAfter") String changeAfter);
}
