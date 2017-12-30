package com.refresh.chotusalesv1.apiops;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

/**
 * Created by Lenovo on 11/2/2017.
 */
public interface usersapiop {


    @GET("AuthChainNUsers.php/api/chainuser/{email}/{password}")
    Call<ResponseBody> authUsers(
            @Path("email") String email,
            @Path("password") String password);


    @POST("AuthChainNUsers.php/api/user/new")
    Call<ResponseBody> postnewuser(
//            @Body User body
    );
//            @Part("complaints") complaints complaintc,
//            @Part MultipartBody.Part Photo);

    @POST("AuthChainNUsers.php/api/chain/{userID}/{status}")
    Call<ResponseBody> logoutUser(
            @Path("userID") String email,
            @Path("status") Boolean status);



}
