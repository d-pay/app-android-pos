package com.example.matheuscatossi.d_pay_pos.webservice;

import com.example.matheuscatossi.d_pay_pos.model.ResultRequest;
import com.example.matheuscatossi.d_pay_pos.utils.Constants;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


public interface APIInterface {

    @GET(Constants.TRANSACTION)
    Call<ResultRequest> transaction(@Query("name") String name, @Query("valor") String valor);

}

