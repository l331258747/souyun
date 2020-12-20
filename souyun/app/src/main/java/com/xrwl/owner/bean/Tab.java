package com.xrwl.owner.bean;

import android.util.Log;

import com.ldw.library.bean.BaseEntity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by www.longdw.com on 2017/11/24 下午2:00.
 */

public class Tab extends BaseEntity {
    private String title;
    private String icon;
    private String page;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public static List<Tab> parseJson(JSONObject json) {
        List<Tab> datas = new ArrayList<>();

        try {
            JSONArray array = json.getJSONArray("data");
            for (int i = 0, length = array.length();i < length;i++) {
                JSONObject obj = array.getJSONObject(i);
                Tab tab = new Tab();
                tab.setTitle(obj.optString("title"));
                tab.setIcon(obj.optString("icon"));
                tab.setPage(obj.optString("page"));
                datas.add(tab);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage(), e);
        }

        return datas;
    }
}
