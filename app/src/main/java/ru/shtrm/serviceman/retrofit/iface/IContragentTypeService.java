package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.ContragentType;

public interface IContragentTypeService {
    @GET("/contragent-type")
    Call<List<ContragentType>> getData(@Query("changedAfter") String changeAfter);
}
