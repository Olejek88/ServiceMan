package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.shtrm.serviceman.data.Measure;

public interface IMeasureService {
    @POST("/measure/create")
    Call<Void> sendData(@Body List<Measure> data);
}
