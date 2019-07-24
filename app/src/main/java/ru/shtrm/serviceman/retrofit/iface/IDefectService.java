package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.Defect;
import ru.shtrm.serviceman.data.UpdateQuery;

public interface IDefectService {
    @GET("/defect")
    Call<List<Defect>> getData(@Query("changedAfter") String changeAfter);

    @GET("/defect")
    Call<List<Defect>> getData(@Query("eqUuid") String equipmentUuid, @Query("changedAfter") String changeAfter);

    @GET("/defect")
    Call<List<Defect>> getData(@Query("eqUuid") List<String> equipmentUuid, @Query("changedAfter") String changeAfter);

    @POST("/defect/update-attribute")
    Call<ResponseBody> updateAttribute(@Body UpdateQuery attribute);
}
