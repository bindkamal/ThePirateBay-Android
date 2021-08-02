package com.kamalbind.thepiratebay_android_api_test_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.kamalbind.thepiratebaylib.api.ThePirateBayApi;
import com.kamalbind.thepiratebaylib.api.ThePirateBayGateWay;
import com.kamalbind.thepiratebaylib.model.Torrent;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ThePirateBayApi mApi;
    private TextView mResultTextView;
    private LiveData<List<Torrent>> mResultData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mApi = new ThePirateBayGateWay(this);
        mResultTextView = findViewById(R.id.result_textview);

        findViewById(R.id.movie_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mResultTextView.setText("data requested!!");
                fetchData(R.id.movie_button);
            }
        });
        findViewById(R.id.music_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mResultTextView.setText("data requested!!");
                fetchData(R.id.music_button);
            }
        });
        findViewById(R.id.recent_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mResultTextView.setText("data requested!!");
                fetchData(R.id.recent_button);
            }
        });

    }

    private void fetchData(int id) {
        if (mResultData != null) {
            mResultData.removeObservers(this);
        }
        switch (id) {
            case R.id.movie_button:
                mResultData = mApi.getTop100TrendingMovies();
                break;
            case R.id.music_button:
                mResultData = mApi.getTop100TrendingMusic();
                break;
            case R.id.recent_button:
                mResultData = mApi.getTop100RecentContent();
                break;
        }
        mResultData.observe(this, torrents -> {
            torrents.forEach(torrent -> mResultTextView.append("\n" + "\n" + torrent.toString()));
        });
    }

}