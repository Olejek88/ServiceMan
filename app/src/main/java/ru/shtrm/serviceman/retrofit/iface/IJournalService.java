package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface IJournalService {
    @POST("/journal/create")
    Call<Void> sendData(@Field("data")List<String> data);
}
