package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.Defect;

public interface IDefectService {
    @GET("/defect")
    Call<List<Defect>> getData(@Query("changedAfter") String changeAfter);

    @GET("/defect")
    Call<List<Defect>> getData(@Query("equipmentUuid") String equipmentUuid, @Query("changedAfter") String changeAfter);

    @GET("/defect")
    Call<List<Defect>> getData(@Query("equipmentUuid") List<String> equipmentUuid, @Query("changedAfter") String changeAfter);
}
