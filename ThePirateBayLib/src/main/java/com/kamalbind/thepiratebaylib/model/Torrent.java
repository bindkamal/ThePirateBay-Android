package com.kamalbind.thepiratebaylib.model;

public class Torrent {
    public long id;
    public String info_hash;
    public long category;
    public String name;
    public String status;
    public long num_files;
    public long size;
    public long seeders;
    public long leechers;
    public String username;
    public long added;
    public long anon;
    public String imdb;

    public Torrent(long id,
                   String info_hash,
                   long category,
                   String name,
                   String status,
                   long num_files,
                   long size,
                   long seeders,
                   long leechers,
                   String username,
                   long added,
                   long anon,
                   String imdb) {
        this.id = id;
        this.info_hash = info_hash;
        this.category = category;
        this.name = name;
        this.status = status;
        this.num_files = num_files;
        this.size = size;
        this.seeders = seeders;
        this.leechers = leechers;
        this.username = username;
        this.added = added;
        this.anon = anon;
        this.imdb = imdb;
    }

    @Override
    public String toString() {
        return "Torrent{" +
                "id=" + id +
                ", info_hash='" + info_hash + '\'' +
                ", category=" + category +
                ", name='" + name + '\'' +
                ", status='" + status + '\'' +
                ", num_files=" + num_files +
                ", size=" + size +
                ", seeders=" + seeders +
                ", leechers=" + leechers +
                ", username='" + username + '\'' +
                ", added=" + added +
                ", anon=" + anon +
                ", imdb='" + imdb + '\'' +
                '}';
    }
}
