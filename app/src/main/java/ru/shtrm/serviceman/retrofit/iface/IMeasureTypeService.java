package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.MeasureType;

public interface IMeasureTypeService {
    @GET("/measure-type")
    Call<List<MeasureType>> getData(@Query("changedAfter") String changeAfter);
}
