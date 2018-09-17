package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.AlarmType;

public interface IAlarmTypeService {
    @GET("/alarm-type")
    Call<List<AlarmType>> getData(@Query("changedAfter") String changeAfter);
}
