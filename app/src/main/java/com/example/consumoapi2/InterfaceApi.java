package com.example.consumoapi2;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface InterfaceApi {
    @GET
    Call<List<Object>> getData(@Url String fullUrl);
}
