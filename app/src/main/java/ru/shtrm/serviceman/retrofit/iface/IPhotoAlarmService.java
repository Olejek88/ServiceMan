package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.shtrm.serviceman.data.PhotoAlarm;

public interface IPhotoAlarmService {
    @POST("/photo-alarm/create")
    Call<ResponseBody> sendData(@Body List<PhotoAlarm> data);
}
