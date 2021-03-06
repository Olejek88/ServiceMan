package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.TaskVerdict;

public interface ITaskVerdictService {
    @GET("/task-verdict")
    Call<List<TaskVerdict>> getData(@Query("changedAfter") String changeAfter);
}
