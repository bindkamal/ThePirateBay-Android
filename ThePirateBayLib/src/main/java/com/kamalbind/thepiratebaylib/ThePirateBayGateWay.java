package com.kamalbind.thepiratebaylib;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ThePirateBayGateWay {

    private final static String TAG = ThePirateBayGateWay.class.getSimpleName();
    private final static String PROXY_COLLECTION_SERVER = "https://proxybay.github.io/";
    private final static List<String> API_SUB_SERVER_LIST = Arrays.asList("/newapi", "/apibay", "/api.php?url=");
    private final static String API_END_POINT_TOP_100_RECENT = "/precompiled/data_top100_recent.json";
    private final static Type collectionType = new TypeToken<Collection<Torrent>>(){}.getType();
    private final static Gson gson = new Gson();
    private FragmentActivity mContext;
    private HttpRequestHelper mHttpRequestHelper;
    private String mSupportedProxyServer;

    public ThePirateBayGateWay(FragmentActivity context) {
        mContext = context;
        mHttpRequestHelper = new HttpRequestHelper(context);
        HttpRequestHelper.LiveResponse<String> response
                = mHttpRequestHelper.getTextDataFromUrl(PROXY_COLLECTION_SERVER);
        response.observe(context, responseData -> {
            switch (response.getStatus()) {
                case HttpRequestHelper.STATUS_INIT : return;
                case HttpRequestHelper.STATUS_RESPONSE_OK :
                    List<String> allProxyServers = new ArrayList<>();
                    new WebScrapingHelper().getAllTheProxyHost(responseData).forEach(proxyHost -> {
                        API_SUB_SERVER_LIST.forEach( subServer ->
                            allProxyServers.add(proxyHost + subServer));
                    });
                    allProxyServers.forEach(s-> Log.d(TAG, s));
                    Log.d(TAG, "Size : " + allProxyServers.size()/API_SUB_SERVER_LIST.size());
                    initializeSupportedProxyServer(allProxyServers, 0);
                    break;
                case HttpRequestHelper.STATUS_RESPONSE_ERROR :
                    Log.d(TAG, "Error");
                    break;
                default: Log.d(TAG, "Invalid Status");
            }
        });
    }

    private void initializeSupportedProxyServer(List<String> allProxyServers, int serverIndex) {
        if (serverIndex == allProxyServers.size()) {
            Log.d(TAG, "Can not find any supported proxy server at the moment");
            return;
        }
        Log.d(TAG, "......... trying to connect.......... " + allProxyServers.get(serverIndex));
        HttpRequestHelper.LiveResponse<String> response
                = mHttpRequestHelper.getTextDataFromUrl(allProxyServers.get(serverIndex) + API_END_POINT_TOP_100_RECENT);
        response.observe(mContext, responseData -> {
            switch (response.getStatus()) {
                case HttpRequestHelper.STATUS_INIT : return;
                case HttpRequestHelper.STATUS_RESPONSE_OK :
                    responseData = responseData.trim();
                    try {
                        List<Torrent> torrentList = gson.fromJson(responseData, collectionType);
                        mSupportedProxyServer = allProxyServers.get(serverIndex);
                        Log.d(TAG, "Supported Proxy Server Found " + mSupportedProxyServer);
                        Log.d(TAG, "Data " + torrentList.get(0).toString());
                    } catch (Exception e) {
                        Log.d(TAG, "Unsupported Proxy Server " + allProxyServers.get(serverIndex));
                        initializeSupportedProxyServer(allProxyServers, serverIndex + 1);
                    }
                    break;
                case HttpRequestHelper.STATUS_RESPONSE_ERROR :
                    Log.d(TAG, "Unsupported Proxy Server " + allProxyServers.get(serverIndex));
                    initializeSupportedProxyServer(allProxyServers, serverIndex + 1);
                    break;
                default: Log.d(TAG, "Invalid Status");
            }
        });
    }



}
