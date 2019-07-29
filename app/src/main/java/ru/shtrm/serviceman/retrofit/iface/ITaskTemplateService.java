package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.TaskTemplate;

public interface ITaskTemplateService {
    @GET("/task-template")
    Call<List<TaskTemplate>> getData(@Query("changedAfter") String changeAfter);
}
