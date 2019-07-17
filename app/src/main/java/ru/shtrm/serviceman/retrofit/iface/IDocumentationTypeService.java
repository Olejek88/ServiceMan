package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.DocumentationType;

public interface IDocumentationTypeService {
    @GET("/documentation-type")
    Call<List<DocumentationType>> getData(@Query("changedAfter") String changeAfter);
}
