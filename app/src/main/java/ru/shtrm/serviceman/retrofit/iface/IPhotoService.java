package ru.shtrm.serviceman.retrofit.iface;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.shtrm.serviceman.data.UpdateQuery;

public interface IPhotoService {
    @POST("/photo/update-attribute")
    Call<ResponseBody> updateAttribute(@Body UpdateQuery attribute);
}
