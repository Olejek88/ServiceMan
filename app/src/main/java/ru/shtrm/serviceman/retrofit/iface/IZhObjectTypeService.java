package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.ZhObjectType;

public interface IZhObjectTypeService {
    @GET("/object-type")
    Call<List<ZhObjectType>> getData(@Query("changedAfter") String changeAfter);
}
