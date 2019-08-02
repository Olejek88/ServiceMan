package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import retrofit2.http.Url;
import ru.shtrm.serviceman.data.Documentation;

public interface IDocumentationService {
    @GET("/documentation")
    Call<List<Documentation>> getData(@Query("changedAfter") String changeAfter);

    @GET("/documentation")
    Call<List<Documentation>> getData(@Query("eqUuid") String equipmentUuid, @Query("changedAfter") String changeAfter);

    @GET("/documentation")
    Call<List<Documentation>> getData(@Query("eqUuid") List<String> equipmentUuid, @Query("changedAfter") String changeAfter);

    @GET
    Call<ResponseBody> getFile(@Url String url);
}
