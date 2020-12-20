package com.xrwl.owner.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.xrwl.owner.bean.Account;


/**
 * Created by www.longdw.com on 2017/11/29 上午11:00.
 */

public class AccountUtil {

    public static final String SP_NAME = "com.xrwl.owner.account";

    public static final String KEY = "account";

    public static void saveAccount(Context context, Account account) {

        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        Gson gson = new Gson();
        String json = gson.toJson(account);
        editor.putString(KEY, json).apply();
    }

    public static Account getAccount(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sp.getString(KEY, null);

//        gson.fromJson(json, new TypeToken<Account>(){}.getType());
        return gson.fromJson(json, Account.class);
    }

    public static void clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }
}
