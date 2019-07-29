package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.shtrm.serviceman.data.Alarm;
import ru.shtrm.serviceman.data.UpdateQuery;

public interface IAlarmService {
    @POST("/alarm/create")
    Call<ResponseBody> sendData(@Body List<Alarm> data);

    @POST("/alarm/update-attribute")
    Call<ResponseBody> updateAttribute(@Body UpdateQuery attribute);
}
