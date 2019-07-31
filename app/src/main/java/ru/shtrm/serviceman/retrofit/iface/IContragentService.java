package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.Contragent;

public interface IContragentService {
    @GET("/contragent")
    Call<List<Contragent>> getData(@Query("changedAfter") String changeAfter);
}
