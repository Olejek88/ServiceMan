package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.TaskType;

public interface ITaskTypeService {
    @GET("/task-type")
    Call<List<TaskType>> getData(@Query("changedAfter") String changeAfter);
}
