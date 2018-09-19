package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.AlarmStatus;

public interface IAlarmStatusService {
    @GET("/alarm-status")
    Call<List<AlarmStatus>> getData(@Query("changedAfter") String changeAfter);
}
