package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.Documentation;

public interface IDocumentationService {
    @GET("/documentation")
    Call<List<Documentation>> getData(@Query("changedAfter") String changeAfter);

    @GET("/documentation")
    Call<List<Documentation>> getData(@Query("equipmentUuid") String equipmentUuid, @Query("changedAfter") String changeAfter);

    @GET("/documentation")
    Call<List<Documentation>> getData(@Query("equipmentUuid") List<String> equipmentUuid, @Query("changedAfter") String changeAfter);
}
