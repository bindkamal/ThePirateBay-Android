package com.kamalbind.thepiratebaylib.api;

import android.util.Log;

import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kamalbind.thepiratebaylib.helper.HttpRequestHelper;
import com.kamalbind.thepiratebaylib.helper.WebScrapingHelper;
import com.kamalbind.thepiratebaylib.model.Torrent;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class ThePirateBayGateWay implements ThePirateBayApi {

    private final static String TAG = ThePirateBayGateWay.class.getSimpleName();
    private final static String PROXY_COLLECTION_SERVER = "https://proxybay.github.io/";
    private final static List<String> API_SUB_SERVER_LIST = Arrays.asList("/newapi", "/apibay", "/api.php?url=");
    private final static String API_TOP_100 = "/precompiled/data_top100";
    private final static String JSON_FILE_EXTENSION = ".json";
    private final static String RECENT_EXTENSION = "_recent";
    private final static String MUSIC_EXTENSION = "_100";
    private final static String MOVIES_EXTENSION = "_200";

    private final static Type collectionType = new TypeToken<Collection<Torrent>>() {}.getType();
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
                case HttpRequestHelper.STATUS_INIT:
                    return;
                case HttpRequestHelper.STATUS_RESPONSE_OK:
                    List<String> allProxyServers = new ArrayList<>();
                    new WebScrapingHelper().getAllTheProxyHost(responseData).forEach(proxyHost -> {
                        API_SUB_SERVER_LIST.forEach(subServer ->
                                allProxyServers.add(proxyHost + subServer));
                    });
                    allProxyServers.forEach(s -> Log.d(TAG, s));
                    Log.d(TAG, "Size : " + allProxyServers.size() / API_SUB_SERVER_LIST.size());
                    initializeSupportedProxyServer(allProxyServers, 0);
                    break;
                case HttpRequestHelper.STATUS_RESPONSE_ERROR:
                    Log.d(TAG, "Error");
                    break;
                default:
                    Log.d(TAG, "Invalid Status");
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
                = mHttpRequestHelper.getTextDataFromUrl(allProxyServers.get(serverIndex) + API_TOP_100 + RECENT_EXTENSION + JSON_FILE_EXTENSION);
        response.observe(mContext, responseData -> {
            switch (response.getStatus()) {
                case HttpRequestHelper.STATUS_INIT:
                    return;
                case HttpRequestHelper.STATUS_RESPONSE_OK:
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
                case HttpRequestHelper.STATUS_RESPONSE_ERROR:
                    Log.d(TAG, "Unsupported Proxy Server " + allProxyServers.get(serverIndex));
                    initializeSupportedProxyServer(allProxyServers, serverIndex + 1);
                    break;
                default:
                    Log.d(TAG, "Invalid Status");
            }
        });
    }

    @Override
    public LiveData<List<Torrent>> getTop100RecentContent() {
        return getTorrentList(mSupportedProxyServer + API_TOP_100 + RECENT_EXTENSION + JSON_FILE_EXTENSION);
    }

    @Override
    public LiveData<List<Torrent>> getTop100TrendingMovies() {
        return getTorrentList(mSupportedProxyServer + API_TOP_100 + MOVIES_EXTENSION + JSON_FILE_EXTENSION);
    }

    @Override
    public LiveData<List<Torrent>> getTop100TrendingMusic() {
        return getTorrentList(mSupportedProxyServer + API_TOP_100 + MUSIC_EXTENSION  + JSON_FILE_EXTENSION);
    }

    @Override
    public LiveData<List<Torrent>> findTorrent(String key) {
        return getTorrentList(mSupportedProxyServer + API_TOP_100);
    }

    private LiveData<List<Torrent>> getTorrentList(String url) {
        MutableLiveData<List<Torrent>> torrentList = new MutableLiveData<>();
        HttpRequestHelper.LiveResponse response = mHttpRequestHelper.getTextDataFromUrl(url);
        response.observeForever(responseData -> {
            switch (response.getStatus()) {
                case HttpRequestHelper.STATUS_INIT:
                    return;
                case HttpRequestHelper.STATUS_RESPONSE_OK:
                    List<Torrent> torrents = gson.fromJson(responseData.toString(), collectionType);
                    torrentList.postValue(torrents);
                    break;
                case HttpRequestHelper.STATUS_RESPONSE_ERROR:
                    torrentList.postValue(new ArrayList<>());
                    break;
                default:
                    Log.d(TAG, "Invalid Status");
            }
        });
        return torrentList;
    }
}
