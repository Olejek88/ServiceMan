package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.Message;
import ru.shtrm.serviceman.data.UpdateQuery;

public interface IMessageService {
    @POST("/message/create")
    Call<ResponseBody> sendData(@Body List<Message> data);

    @GET("/message")
    Call<List<Message>> getData(@Query("changedAfter") String changeAfter);

    @POST("/message/update-attribute")
    Call<ResponseBody> updateAttribute(@Body UpdateQuery attribute);
}
