package com.ov3rk1ll.kinocast.api.mirror;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.webkit.ValueCallback;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ov3rk1ll.kinocast.R;
import com.ov3rk1ll.kinocast.ui.DetailActivity;
import com.ov3rk1ll.kinocast.ui.MainActivity;
import com.ov3rk1ll.kinocast.utils.Utils;

import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GoUnlimited extends Host {
    private static final String TAG = GoUnlimited.class.getSimpleName();
    public static final int HOST_ID = 84;
    private static final Pattern regexMp4 = Pattern.compile("http[s]*:\\/\\/([0-9a-z.]*)\\/([0-9a-z.]*?)\\/v\\.mp4");

    @Override
    public int getId() {
        return HOST_ID;
    }

    @Override
    public String getName() {
        return "GoUnlimited";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getVideoPath(DetailActivity.QueryPlayTask queryTask) {
        if(TextUtils.isEmpty(url)) return null;

        queryTask.updateProgress(queryTask.getContext().getString(R.string.host_progress_getdatafrom, url));

        Log.d(TAG, "resolve " + url);
        if(url.contains("/f/")){
            String id = url.substring(url.indexOf("/f/") + 3);
            if(id.contains("/")) id = id.substring(0, id.indexOf("/"));
            queryTask.updateProgress(queryTask.getContext().getString(R.string.host_progress_getvideoforid,id));
            url = "https://gounlimited.to/embed-" + id + ".html";
        }
        else if(url.contains("/embed/")){
            String id = url.substring(url.indexOf("/embed/") + 7);
            if(id.contains("/")) id = id.substring(0, id.indexOf("/"));
            queryTask.updateProgress(queryTask.getContext().getString(R.string.host_progress_getvideoforid,id));
            url = "https://gounlimited.to/embed-" + id + ".html";
        }

        String link = getLink(url);

        Log.d(TAG, "Request. Got " + link);
        queryTask.updateProgress(queryTask.getContext().getString(R.string.host_progress_1sttry));

        return link;
    }

    private String getLink(final String url){
        try {
            Document doc = Utils.buildJsoup(url).get();
            Matcher m = regexMp4.matcher(doc.html());
            if (m.find()) {
                return m.group(0);
            }

            ArrayList<String> list = Utils.unPackAll(doc.html());

            for (String pack : list) {
                m = regexMp4.matcher(pack);
                if (m.find()) {
                    return m.group(0);
                }
            }      } catch (IOException e) {
            e.printStackTrace();
        }

        final boolean[] requestDone = {false};
        final String[] solvedUrl = {null};

        MainActivity.activity.runOnUiThread(new Runnable() {
            @SuppressLint("SetJavaScriptEnabled")
            @Override
            public void run() {
                // Virtual WebView
                final WebView webView = MainActivity.webView;

                webView.setVisibility(View.GONE);
                webView.getSettings().setUserAgentString(Utils.USER_AGENT);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setWebViewClient(new WebViewClient() {

                    public void onPageFinished(WebView view, String url) {

                        webView.evaluateJavascript(
                                "(function() { return document.querySelector(\"video\").getAttribute(\"src\") })();",
                                new ValueCallback<String>() {
                                    @Override
                                    public void onReceiveValue(String html) {
                                        // code here
                                        html = html.replaceAll("^\"|\"$", "");
                                        Log.d("HTML", html);
                                        solvedUrl[0] = html;
                                        requestDone[0] = true;
                                    }
                                });

                    }

                });
                webView.loadUrl(url);
            }
        });

        int timeout = 50;
        // Wait for the webView to load the correct url
        while (!requestDone[0]){
            SystemClock.sleep(200);
            timeout--;
            if(timeout <= 0)
                break;
        }

        if(Utils.isStringEmpty(solvedUrl[0])) return null;
        return Utils.getUrl(solvedUrl[0]);

    }

    @Override
    public Boolean canHandleUri(Uri uri) {
        return ("gounlimited.to".equalsIgnoreCase(uri.getHost())
                ||  "www.gounlimited.to".equalsIgnoreCase(uri.getHost()));
    }
    @Override
    public void handleUri(Uri uri) {
        setUrl(uri.toString());
    }
}
