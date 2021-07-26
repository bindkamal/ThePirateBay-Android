package com.kamalbind.thepiratebaylib;

import java.util.List;

public interface ThePirateBayApi {
    List<Torrent> getTop100TrendingContent();
    List<Torrent> getTop100TrendingMovies();
    List<Torrent> getTop100TrendingMusic();
    List<Torrent> findTorrent(String key);
}
