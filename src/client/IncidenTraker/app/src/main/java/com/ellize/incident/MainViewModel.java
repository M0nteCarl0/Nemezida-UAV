package com.ellize.incident;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.android.volley.AuthFailureError;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class MainViewModel extends AndroidViewModel {

    static final String ACCOUNT_TYPE = "com.ellize.uav";
    private static final String ACCOUNT_TOKEN_TYPE = "access";
    public static final String BASE_URL = "https://example.com/api/";

    public static final String BASE_AUTH = "auth/get-token/";

    public MutableLiveData<String> msg;
    public MutableLiveData<User> user;
    public MutableLiveData<Boolean> isAuth;
    public MutableLiveData<JSONObject> qRresult;
    public MutableLiveData<Boolean> isEmailPswError;
    public MutableLiveData<Boolean> isConnectionError;
    public MutableLiveData<Boolean> isNoInternet;
    public MutableLiveData<String> test_log;
    /**
     * detects which category was choosen from CategoriesFragment
      */
    public MutableLiveData<Integer> categoryFragmentTraker;
    public MutableLiveData<Boolean> isSomethingLoading;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Log.d("fragment","view model created");
        msg = new MutableLiveData<>();
        msg.setValue("empty");
        user = new MutableLiveData<>();
        isSomethingLoading = new MutableLiveData<>();
        isSomethingLoading.setValue(false);
        User cuser = new User("","");
        user.setValue(cuser);
        isAuth = new MutableLiveData<>();
        qRresult = new MutableLiveData<>();
        isConnectionError = new MutableLiveData<>();
        isEmailPswError = new MutableLiveData<>();
        isNoInternet = new MutableLiveData<>();
        test_log = new MutableLiveData<>();
        Log.d("mathod","value="+isNoInternet.getValue());
        categoryFragmentTraker = new MutableLiveData<>();
    }

    public void createAccount(){
        Account account = new Account(user.getValue().login,ACCOUNT_TYPE);
        AccountManager accountManager = AccountManager.get(getApplication());
        Bundle bundle = new Bundle();
        bundle.putString("company",user.getValue().company);
        bundle.putString("inn",user.getValue().inn);
        bundle.putString("fio",user.getValue().fio);
        bundle.putString("avatar",user.getValue().avatar_url);
        boolean result = accountManager.addAccountExplicitly(account,user.getValue().psw,bundle);
        if(result) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                accountManager.notifyAccountAuthenticated(account);
            }
            accountManager.setAuthToken(account, ACCOUNT_TOKEN_TYPE, user.getValue().token);

        }else {
            //Toast.makeText(getApplication(),"can't create account",Toast.LENGTH_SHORT).show();
        }

    }

    public void login(boolean isFirstTime){
        login(user.getValue().login,user.getValue().psw,getApplication(), isFirstTime);
    }

    /**
     * method to remove error button in SvanQRFragment and TextView in loginFragment
     */
    private void resetErrors(){
        Log.d("method","reset errors");
        isNoInternet.setValue(true);
        isConnectionError.setValue(true);
        isEmailPswError.setValue(true);
    }



    /**
     * catch error to show error textview in loginFragment
     * */
    private void catchErrorOnLoginConnect(VolleyError e){
        Log.d("method","catchErrorOnConnect");
        test_log.setValue(e.toString());
        if(e instanceof NoConnectionError){
            //Toast.makeText(context, "connection error", Toast.LENGTH_SHORT).show();
            isNoInternet.setValue(false);
        }else if(e instanceof TimeoutError) {
            //Toast.makeText(context, "server timeout error", Toast.LENGTH_SHORT).show();
            isConnectionError.setValue(false);
        } else if(e.networkResponse!=null){
            if(e.networkResponse.statusCode == 403){
                //Toast.makeText(context, "password error", Toast.LENGTH_SHORT).show();
                isEmailPswError.setValue(false);

            } else if(e.networkResponse.statusCode == 404){
                //email error
                //Toast.makeText(context, "email error", Toast.LENGTH_SHORT).show();
                isEmailPswError.setValue(false);
            }
        }else{
            isConnectionError.setValue(false);
        }
        isAuth.setValue(false);
    }
    public void login(final String login, final String psw, final Context context, final boolean isFirstTime) {
        //todo tempory
        isAuth.setValue(true);//to delete
        if(true)  return; //to delete
        try {
            JSONObject paramsVolley = new JSONObject();
            try {
                paramsVolley.put("login", login);
                paramsVolley.put("password", psw);

            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            isSomethingLoading.setValue(true);
            JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST,
                    BASE_URL + BASE_AUTH,
                    paramsVolley,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            isSomethingLoading.setValue(false);
                            //Toast.makeText(context, response.toString(), Toast.LENGTH_SHORT).show();

                                Log.d("json", response.toString());
                                //test_log.setValue(response.toString());

                            User currentUser = user.getValue();
                            currentUser.isLogined = true;

                            try {
                                currentUser.token = response.getString("token");
                                currentUser.fio = response.getJSONObject("user").getString("fio");
                                currentUser.inn = response.getJSONObject("user").getString("inn");
                                currentUser.company = response.getJSONObject("user").getString("company");
                                currentUser.avatar_url = response.getJSONObject("user").getString("avatar");
                                //currentUser.locale = getLocale(getApplication());
                                user.postValue(user.getValue());
                                resetErrors();
                                isAuth.setValue(true);
                                /*if(!isFirstTime && !user.getValue().locale.equals(getLocale(getApplication()))){
                                    setLocale2(getLocale(getApplication()));
                                }*/
                                //setLocale2(getLocale(getApplication()));
                                if (isFirstTime){
                                    createAccount();
                                    //set current app locale
                                    //setLocale(getLocale(getApplication()));
                                 //   setLocale2(getLocale(getApplication()));
                                    //setLocaleAsync(getLocale(getApplication()));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                //Toast.makeText(context,"wrong user data",Toast.LENGTH_SHORT).show();
                                test_log.setValue(e.toString());
                            }
                            //Toast.makeText(context, "success:" + response.toString(), Toast.LENGTH_SHORT).show();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError e) {
                            isSomethingLoading.setValue(false);
                            //Toast.makeText(context, "Error Occurred", Toast.LENGTH_SHORT).show();
//                            if(BuildConfig.DEBUG) {
                                Log.d("json", e.toString());
                                test_log.setValue(e.toString());
//                            }
                            //e.printStackTrace();
                            catchErrorOnLoginConnect(e);
                        }
                    }) {
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> headers = new HashMap<>();
                    String credentials = login + ":" + psw;
                    String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                    // headers.put("Content-Type", "application/json");
                    headers.put("Authorization", auth);
                    return headers;
                }//*/
            };

            VolleySingle.getInstance(getApplication()).addToRequestQueue(jsonObjReq);
        }catch(Exception e){
            e.printStackTrace();
        }

    }




    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }



    public void saveLocale(String locale){
        SharedPreferences prefs = getApplication().getSharedPreferences("prefs",Context.MODE_PRIVATE);
        prefs.edit().putString(user.getValue().fio,locale).commit();
    }
    public void setCurrentFragment(int inx){
        categoryFragmentTraker.setValue(inx);
    }
}
