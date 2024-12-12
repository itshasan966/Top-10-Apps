package com.example.top10downloader;

public class FeedEntry {
    private String name;
    private String releaseDate;
    private String artist;
    private String summary;
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

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
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
                ", summary="+summary +'\n'+
                ", imageUrl=" + imageUrl + '\n';
    }
}
