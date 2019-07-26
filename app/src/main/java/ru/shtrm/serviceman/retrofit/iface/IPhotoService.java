package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IPhotoService {
    @Multipart
    @POST("/photo/update-attribute")
    Call<ResponseBody> updateAttribute(
            @Part("_id") RequestBody _id,
            @Part("modelClass") RequestBody modelClass,
            @Part("modelUuid") RequestBody modelUuid,
            @Part("attribute") RequestBody attribute,
            @Part("value") RequestBody value,
            @Part("createdAt") RequestBody createdAt,
            @Part("changedAt") RequestBody changedAt,
            @Part List<MultipartBody.Part> files);

    @Multipart
    @POST("/photo/create")
    Call<ResponseBody> sendData(@Part("descr") RequestBody descr, @Part List<MultipartBody.Part> files);
}
