package com.kamalbind.thepiratebaylib;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class HttpRequestHelper {

    public static final int STATUS_INIT = -1;
    public static final int STATUS_RESPONSE_OK = 0;
    public static final int STATUS_RESPONSE_ERROR = 1;
    private RequestQueue mRequestQueue;

    public HttpRequestHelper(Context context) {
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public LiveResponse<String> getTextDataFromUrl(String url) {
        LiveResponse<String> liveResponse = new LiveResponse<>();
        mRequestQueue.add(new StringRequest(Request.Method.GET, url,
                response -> {
                    liveResponse.updateResponse(STATUS_RESPONSE_OK, response);
                },
                error -> {
                    liveResponse.updateResponse(STATUS_RESPONSE_ERROR,null);
                }));
        return liveResponse;
    }


    public class LiveResponse<T> extends LiveData<T> {

        private int mStatus;

        public LiveResponse() {
            mStatus = STATUS_INIT;
        }

        public int getStatus() {
            return mStatus;
        }

        protected void updateResponse(int status, @Nullable T responseData) {
            this.mStatus = status;
            super.postValue(responseData);
        }
    }
}
