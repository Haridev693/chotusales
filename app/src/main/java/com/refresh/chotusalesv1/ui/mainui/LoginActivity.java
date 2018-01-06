package com.refresh.chotusalesv1.ui.mainui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.refresh.chotusalesv1.R;
import com.refresh.chotusalesv1.apiops.usersapiop;
import com.refresh.chotusalesv1.responseclasses.responseParser;
import com.refresh.chotusalesv1.serializer.BooleanSerializer;
import com.refresh.chotusalesv1.techicalservices.AndroidDatabase;
import com.refresh.chotusalesv1.techicalservices.connectivity;
import com.refresh.chotusalesv1.techicalservices.sessionmanager;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import android.support.design.widget.Snackbar;
//import sriharidevsoftsols.chotusales.R;
//import sriharidevsoftsols.chotusales.apiops.usersapiop;
//import sriharidevsoftsols.chotusales.classes.ResponseClasses.responseParser;
//import sriharidevsoftsols.chotusales.classes.chain;
//import sriharidevsoftsols.chotusales.serializer.BooleanSerializer;
//import sriharidevsoftsols.chotusales.utils.sessionmanager;
//import sriharidevsoftsols.chotusales.utils.sorryStat.connectivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends Activity  {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
//    private static final int REQUEST_READ_CONTACTS = 0;

    private Gson gson;

    private sessionmanager SLoginSession;


    // UI references.
    @BindView(R.id.email) EditText mEmailView;
    @BindView(R.id.password) EditText mPasswordView;
    @BindView(R.id.login_progress) View mProgressView;
//    private EditText mEmailView;
//    private EditText mPasswordView;
//    private View mProgressView;
    @BindView(R.id.login_form) View mLoginFormView;
    @BindView(R.id.email_sign_in_button) Button mEmailSignInButton;

    private usersapiop users;
//    private chain chainDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        SLoginSession = new sessionmanager(getApplicationContext());


        // Set up the login form.
//        mEmailView = (EditText) findViewById(R.id.email);
//        populateAutoComplete();

//        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

//        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

//        mLoginFormView = findViewById(R.id.login_form);
//        mProgressView = findViewById(R.id.login_progress);
        BooleanSerializer serializer = new BooleanSerializer();
        GsonBuilder b=  new GsonBuilder();
        b.registerTypeAdapter(Boolean.class, serializer);
        b.registerTypeAdapter(boolean.class, serializer);
        gson = b.create();
        buildAPIRetrofit();
        initiateCoreApp();
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
//        if (mAuthTask != null) {
//            return;
//        }
        //CommentThis
//        mEmailView.setText("resturantapp@gmail.com");
//        mPasswordView.setText("admin@123");

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String email = mEmailView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError(getString(R.string.error_field_required));
            focusView = mEmailView;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mEmailView.setError(getString(R.string.error_invalid_email));
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            users.authUsers(email,password).enqueue(complaindetailcall);

//            mAuthTask = new UserLoginTask(email, password);
//            mAuthTask.execute((Void) null);
        }
    }


    Callback<ResponseBody> complaindetailcall = new Callback<ResponseBody>() {
        @Override
        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

            if (response.isSuccessful()) {
                showProgress(false);
                try {
                    if (response != null) {

                        String sres = response.body().string();
                        responseParser responseB = new responseParser();
                        responseB =responseB.getResponse(sres);
//                        responseParser responseB = //gson.fromJson(sres, responseParser.class);
//                        chain c;

                            if (responseB.resStatus.equals("error")) {
//                                prodialog.hide();
                                Toast.makeText(getApplicationContext(), responseB.resText, Toast.LENGTH_SHORT).show();
                            } else if (responseB.resStatus.equals("success")) {

                                if (!responseB.resText.isEmpty() && !response.equals(null)) {
                                    processResponse(responseB.resText);
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "error while processing response", Toast.LENGTH_SHORT).show();
                                }
//                                Toast.makeText(mContext, resp.resText, Toast.LENGTH_SHORT).show();
//                                thanksmessagescreen();
                            }

                    }

                } catch (Exception e) {

                    Log.e("LoginActivityCallback",e.getMessage());
                    Log.e("LoginActivityStackTrace",e.getStackTrace().toString());
                }
            }
            else
            {
                showProgress(false);
                Toast.makeText(LoginActivity.this,"Server is down, Please try again",Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onFailure(Call<ResponseBody> call, Throwable t) {
            showProgress(false);
            if(connectivity.isNetworkConnected(getApplicationContext()))
            {
                Toast.makeText(LoginActivity.this,"Server is down, Please try again",Toast.LENGTH_SHORT).show();

            }
            else
            {
//                Toast.makeText()
            }
//            Snackbar.make(mLoginFormView,"",Snackbar.LENGTH_SHORT);
        }
    };

//    private void showposlogin() {
//        Intent i = new Intent(getApplicationContext(),PosLoginActivity.class);
//        startActivity(i);
//    }

    private void processResponse(String sres)
    {
        SLoginSession.createLoginSession("UserName",mEmailView.getText().toString(),"");

        shownavigationscreen();
//        }
    }



    private void initiateCoreApp() {
        AndroidDatabase database = new AndroidDatabase(getApplicationContext());
        connectivity.globalDB =  database;
        Log.d("Core App", "INITIATE");
    }

    private void setLanguage(String localeString) {
        Locale locale = new Locale(localeString);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }


    private void shownavigationscreen()
    {

        Intent i = new Intent(this,PosLoginActivity.class);
        startActivity(i);
    }


    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }


    private void buildAPIRetrofit() {


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
                .readTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();


        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl(connectivity.hostip)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient);

        Retrofit retrofit = builder.build();

        users = retrofit.create(usersapiop.class);

       // complaint = retrofit.create(complaintsapiop.class);

    }


    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }


    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

//    @Override
//    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
//        return new CursorLoader(this,
//                // Retrieve data rows for the device user's 'profile' contact.
//                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
//                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,
//
//                // Select only email addresses.
//                ContactsContract.Contacts.Data.MIMETYPE +
//                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
//                .CONTENT_ITEM_TYPE},
//
//                // Show primary email addresses first. Note that there won't be
//                // a primary email address if the user hasn't specified one.
//                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
//    }
//
//    @Override
//    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
//        List<String> emails = new ArrayList<>();
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            emails.add(cursor.getString(ProfileQuery.ADDRESS));
//            cursor.moveToNext();
//        }
//
//        addEmailsToAutoComplete(emails);
//    }
//
//    @Override
//    public void onLoaderReset(Loader<Cursor> cursorLoader) {
//
//    }
//
//    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
//        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
//        ArrayAdapter<String> adapter =
//                new ArrayAdapter<>(LoginActivity.this,
//                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);
//
//        //mEmailView.setAdapter(adapter);
//    }


//    private interface ProfileQuery {
//        String[] PROJECTION = {
//                ContactsContract.CommonDataKinds.Email.ADDRESS,
//                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
//        };
//
//        int ADDRESS = 0;
//        int IS_PRIMARY = 1;
//    }
//
    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
//    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
//
//        private final String mEmail;
//        private final String mPassword;
//
//        UserLoginTask(String email, String password) {
//            mEmail = email;
//            mPassword = password;
//        }
//
//        @Override
//        protected Boolean doInBackground(Void... params) {
//            // TODO: attempt authentication against a network service.
//
//            try {
//
//
//                // Simulate network access.
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                return false;
//            }
////
////            for (String credential : DUMMY_CREDENTIALS) {
////                String[] pieces = credential.split(":");
////                if (pieces[0].equals(mEmail)) {
////                    // Account exists, return true if the password matches.
////                    return pieces[1].equals(mPassword);
////                }
////            }
//
//            // TODO: register the new account here.
//            return true;
//        }
//
//        @Override
//        protected void onPostExecute(final Boolean success) {
//            mAuthTask = null;
//            showProgress(false);
//
//            if (success) {
//                finish();
//            } else {
//                mPasswordView.setError(getString(R.string.error_incorrect_password));
//                mPasswordView.requestFocus();
//            }
//        }
//
//        @Override
//        protected void onCancelled() {
//            mAuthTask = null;
//            showProgress(false);
//        }
//    }
}

