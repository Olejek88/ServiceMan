package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IPhotoHouseService {
    @Multipart
    @POST("/photo-house/create")
    Call<ResponseBody> sendData(@Part("descr") RequestBody descr, @Part List<MultipartBody.Part> files);
}
