package com.example.top10downloader;

public class FeedEntry {
    private String name;
    private String releaseDate;
    private String artist;
    private String summery;
    private String imageUrl;


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getSummery() {
        return summery;
    }

    public void setSummery(String summery) {
        this.summery = summery;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    @Override
    public String toString() {
        return "name=" + name + '\n' +
                ", releaseDate=" + releaseDate + '\n' +
                ", artist=" + artist + '\n' +
                ", imageUrl=" + imageUrl + '\n';
    }
}
