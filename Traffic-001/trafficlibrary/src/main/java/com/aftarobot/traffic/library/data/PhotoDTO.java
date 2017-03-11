package com.aftarobot.traffic.library.data;

/**
 * Created by aubreymalabie on 2/23/17.
 */

public class PhotoDTO {

    private String photoID, caption, url;
    private int type, height, width;
    private Long dateTaken, dateUploaded;

    public static final int
            TRAFFIC_FINE = 1,
            TRAFFIC_ACCIDENT = 2,
            USER = 3,
            OTHER = 4,
            ORIENTATION_PORTRAIT = 5,
            ORIENTATION_LANDSCAPE = 6;

    public String getPhotoID() {
        return photoID;
    }

    public void setPhotoID(String photoID) {
        this.photoID = photoID;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public Long getDateTaken() {
        return dateTaken;
    }

    public void setDateTaken(Long dateTaken) {
        this.dateTaken = dateTaken;
    }

    public Long getDateUploaded() {
        return dateUploaded;
    }

    public void setDateUploaded(Long dateUploaded) {
        this.dateUploaded = dateUploaded;
    }
}
