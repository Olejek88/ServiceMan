package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.OperationTemplate;

public interface IOperationTemplateService {
    @GET("/operation-template")
    Call<List<OperationTemplate>> getData(@Query("changedAfter") String changeAfter);
}
