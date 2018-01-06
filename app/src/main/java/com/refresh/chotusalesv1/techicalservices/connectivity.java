package com.refresh.chotusalesv1.techicalservices;

import android.content.Context;
import android.net.ConnectivityManager;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Lenovo on 10/28/2017.
 */

public class connectivity {

    //public static String hostip="http://192.168.0.111/SlimSwachhmApp/src/routes/";

//    public static String hostip="https://urposdemo.000webhostapp.com/chotusales/src/routes/";

    public static String hostip="http://shs2apicalls.pe.hu/chotusales/src/routes/";
    public static AndroidDatabase globalDB;

//    public static String SMSApi="http://tra1.smsmyntraa.com/api/"; //SMS Myntra

//    public static String SMSApi="https://instantalerts.co/api/web/send/"; ///Spring Edge

        public static String SMSApi="http://api.textlocal.in/send/"; ///textlocal


//    public static String hostip="http://swachm.000webhostapp.com/SlimSwachhmApp/src/routes/";

//    public static String hostip="http://192.168.0.105/chotusales/src/routes/";

//        public static String hostip="http://192.168.1.100/chotusales/src/routes/";

//
//    public static String hostip="http://129.122.145.215/chotusales/src/routes/";// 129.122.150.90

//    public static String hostip="http://192.168.1.100/SlimSwachhmApp/src/routes/";

    public static boolean isNetworkConnected(Context c) {
        ConnectivityManager cm = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }


    public static Retrofit buildRetrofit()
    {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

//        gson = new GsonBuilder()
//                .registerTypeAdapter(complaintview.class,new complainviewdeserializer())
//                .enableComplexMapKeySerialization()
//                .serializeNulls()
//                .setVersion(1.0)
//                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(hostip)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient);

        Retrofit retrofit = builder.build();

        return retrofit;
    }


    public static Retrofit buildSMSRetrofit()
    {

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        // set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

//        gson = new GsonBuilder()
//                .registerTypeAdapter(complaintview.class,new complainviewdeserializer())
//                .enableComplexMapKeySerialization()
//                .serializeNulls()
//                .setVersion(1.0)
//                .create();

        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(15, TimeUnit.SECONDS)
                .connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();

        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(SMSApi)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient);

        Retrofit retrofit = builder.build();

        return retrofit;
    }


}
