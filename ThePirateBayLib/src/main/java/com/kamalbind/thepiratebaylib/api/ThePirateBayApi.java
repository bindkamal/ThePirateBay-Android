package com.kamalbind.thepiratebaylib.api;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.kamalbind.thepiratebaylib.model.Torrent;

import java.util.List;

public interface ThePirateBayApi {
    LiveData<List<Torrent>> getTop100RecentContent();
    LiveData<List<Torrent>> getTop100TrendingMovies();
    LiveData<List<Torrent>> getTop100TrendingMusic();
    LiveData<List<Torrent>> findTorrent(String key);
}
