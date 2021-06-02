package com.example.consumows;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Irevistas {
   // @Headers("Public-Merchant-Id: 84e1d0de1fbf437e9779fd6a52a9ca18")
    @GET("ws/issues.php")
    Call<List<revistas>> RetroRevistas(@Query("j_id") String id);
}
