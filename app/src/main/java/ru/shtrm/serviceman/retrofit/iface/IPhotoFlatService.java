package ru.shtrm.serviceman.retrofit.iface;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import ru.shtrm.serviceman.data.PhotoFlat;

public interface IPhotoFlatService {
    @POST("/photo-flat/create")
    Call<Void> sendData(@Body List<PhotoFlat> data);
}
