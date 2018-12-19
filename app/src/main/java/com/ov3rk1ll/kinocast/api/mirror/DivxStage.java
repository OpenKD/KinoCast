package com.ov3rk1ll.kinocast.api.mirror;

import android.text.TextUtils;
import android.util.Log;

import com.ov3rk1ll.kinocast.R;
import com.ov3rk1ll.kinocast.ui.DetailActivity;
import com.ov3rk1ll.kinocast.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class DivxStage extends Host {
    private static final String TAG = DivxStage.class.getSimpleName();
    public static final int HOST_ID = 8;

    @Override
    public int getId() {
        return HOST_ID;
    }

    @Override
    public String getName() {
        return "DivxStage";
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getVideoPath(DetailActivity.QueryPlayTask queryTask) {
        if(TextUtils.isEmpty(url)) return null;
        try {
            url = url.replace("/Out/?s=", "");
            Log.d(TAG, "Resolve " + url);
            String videoId = url.substring(url.lastIndexOf("/") + 1);
            Log.d(TAG, "API call to " + "http://www.divxstage.to/mobile/ajax.php?videoId=" + videoId);
            queryTask.updateProgress(queryTask.getContext().getString(R.string.host_progress_getvideoforid, videoId));
            JSONObject json = Utils.readJson("http://www.divxstage.to/mobile/ajax.php?videoId=" + videoId);
            return Utils.getRedirectTarget("http://www.divxstage.to/mobile/" + json.getJSONArray("items").getJSONObject(0).getString("download"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
