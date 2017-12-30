package com.refresh.chotusalesv1.apiops;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by Lenovo on 12/21/2017.
 */

public interface smsapiops {

//    http://tra1.smsmyntraa.com/api/
//    @POST("send_transactional_sms.php?username={username}
// &msg_token={password}&sender_id={senderID}
// &message={message}&mobile={mobilenum}")

    @POST("send_transactional_sms.php")
    Call<ResponseBody> postsms(
            @Query("username") String username,
            @Query("msg_token") String password,
            @Query("sender_id") String SenderID,
            @Query("message") String message,
            @Query("mobile") String mobilenum
    );

//    https://instantalerts.co/api/web/send/?
//    // apikey=69iq54a4m4s4ib0agg135o3y0yfbkbmbu&sender=SEDEMO&
//    // to=919035xxxxx&message=Test+message&format=json

    //Spring Edge
    @POST(".")
    Call<ResponseBody> postSpringsms(
            @Query("apikey") String apikey,
            @Query("sender") String SenderID,
            @Query("to") String mobilenum,
            @Query("message") String message,
            @Query("format") String format

    );


//    http://api.textlocal.in/send/?
//    $data = "username=".$username."&hash=".$hash."&message=".$message."&sender=".$sender."&numbers=".$numbers."&test=".$test;

    //Spring Edge
    @POST(".")
    Call<ResponseBody> posttextLocalSMS(
            @Query("username") String username,
            @Query("hash") String hashkey,
            @Query("message") String message,
            @Query("sender") String senderID,
            @Query("numbers") String mobilenum,
            @Query("test") String test
    );


}
