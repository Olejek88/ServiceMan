package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.WorkStatus;

public interface IWorkStatusService {
    @GET("/work-status")
    Call<List<WorkStatus>> getData(@Query("changedAfter") String changeAfter);
}
