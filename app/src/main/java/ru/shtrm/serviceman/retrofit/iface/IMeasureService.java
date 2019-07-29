package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.shtrm.serviceman.data.Measure;
import ru.shtrm.serviceman.data.UpdateQuery;

public interface IMeasureService {
    @POST("/measure/create")
    Call<ResponseBody> sendData(@Body List<Measure> data);

    @POST("/measure/update-attribute")
    Call<ResponseBody> updateAttribute(@Body UpdateQuery attribute);
}
