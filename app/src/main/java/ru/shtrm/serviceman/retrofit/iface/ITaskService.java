package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.Task;

public interface ITaskService {
    @GET("/task")
    Call<List<Task>> getData(@Query("changedAfter") String changeAfter);

    @GET("/task")
    Call<List<Task>> getByStatus(@Query("status") String status);

    @GET("/task")
    Call<List<Task>> getByStatus(@Query("status[]") List<String> status);
}
