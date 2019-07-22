package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.Task;

public interface ITaskService {
    @GET("/task")
    Call<List<Task>> getData(@Query("changedAfter") String changeAfter);

    @GET("/task")
    Call<List<Task>> getByStatus(@Query("status") String status);

    @GET("/task")
    Call<List<Task>> getByStatus(@Query("status[]") List<String> status);

    @POST("/task/results")
    Call<ResponseBody> send(@Body List<Task> orders);

    @POST("/task/in-work")
    Call<ResponseBody> setInWork(@Body String uuid);

    @POST("/task/in-work")
    Call<ResponseBody> setInWork(@Body List<String> uuid);

    @POST("/task/complete")
    Call<ResponseBody> setComplete(@Body String uuid);

    @POST("/task/complete")
    Call<ResponseBody> setComplete(@Body List<String> uuid);

    @POST("/task/un-complete")
    Call<ResponseBody> setUnComplete(@Body String uuid);

    @POST("/task/un-complete")
    Call<ResponseBody> setUnComplete(@Body List<String> uuid);
}
