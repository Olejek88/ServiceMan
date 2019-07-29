package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.DefectType;

public interface IDefectTypeService {
    @GET("/defect-type")
    Call<List<DefectType>> getData(@Query("changedAfter") String changeAfter);
}
