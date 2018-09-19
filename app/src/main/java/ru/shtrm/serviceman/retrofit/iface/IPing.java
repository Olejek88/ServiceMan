package ru.shtrm.serviceman.retrofit.iface;

import retrofit2.Call;
import retrofit2.http.GET;

public interface IPing {
    @GET("/ping")
    Call<Void> ping();
}
