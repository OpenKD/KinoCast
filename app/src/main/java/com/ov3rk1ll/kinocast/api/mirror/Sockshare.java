package com.ov3rk1ll.kinocast.api.mirror;

import android.text.TextUtils;

import com.ov3rk1ll.kinocast.ui.DetailActivity;

public class Sockshare extends Host {
    private static final String TAG = Sockshare.class.getSimpleName();
    public static final int HOST_ID = 5;

    public Sockshare(int id) {
        super(id);
    }


    @Override
    public int getId() {
        return HOST_ID;
    }

    @Override
    public String getName() {
        return "Sockshare";
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getVideoPath(DetailActivity.QueryPlayTask queryTask) {
        if(TextUtils.isEmpty(url)) return null;
        //TODO implement Sockshare
        return null;
    }
}
