package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.shtrm.serviceman.data.Subject;

public interface ISubjectService {
    @GET("/subject")
    Call<List<Subject>> getUsers(@Query("changedAfter") String changeAfter);
}
