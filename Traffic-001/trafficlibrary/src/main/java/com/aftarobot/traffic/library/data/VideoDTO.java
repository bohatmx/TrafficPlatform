package com.aftarobot.traffic.library.data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by aubreymalabie on 2/23/17.
 */

public class VideoDTO implements Serializable{

    private String videoID, ticketID, userID,caption, url, format, frameRate, bitRate,
    localFilePath, stringDateTaken, stringDateUploaded;
    private int type, height, width, bytes;
    private double duration;
    private Long dateTaken, dateUploaded;
    private AudioDTO audio;
    private VideoMetadataDTO videoMetadata;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd MMMM yyyy HH:mm:ss");
    public static final int
            TRAFFIC_FINE = 1,
            TRAFFIC_ACCIDENT = 2,
            USER = 3,
            OTHER = 4,
            ORIENTATION_PORTRAIT = 5,
            ORIENTATION_LANDSCAPE = 6;

    public VideoDTO() {
        dateTaken = new Date().getTime();
        stringDateTaken = sdf.format(new Date());
    }

    public String getFormat() {
        return format;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getFrameRate() {
        return frameRate;
    }

    public void setFrameRate(String frameRate) {
        this.frameRate = frameRate;
    }

    public String getBitRate() {
        return bitRate;
    }

    public void setBitRate(String bitRate) {
        this.bitRate = bitRate;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public AudioDTO getAudio() {
        return audio;
    }

    public void setAudio(AudioDTO audio) {
        this.audio = audio;
    }

    public VideoMetadataDTO getVideoMetadata() {
        return videoMetadata;
    }

    public void setVideoMetadata(VideoMetadataDTO videoMetadata) {
        this.videoMetadata = videoMetadata;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getTicketID() {
        return ticketID;
    }

    public void setTicketID(String ticketID) {
        this.ticketID = ticketID;
    }

    public String getLocalFilePath() {
        return localFilePath;
    }

    public String getStringDateTaken() {
        return stringDateTaken;
    }

    public void setStringDateTaken(String stringDateTaken) {
        this.stringDateTaken = stringDateTaken;
    }

    public int getBytes() {
        return bytes;
    }

    public void setBytes(int bytes) {
        this.bytes = bytes;
    }

    public String getStringDateUploaded() {
        return stringDateUploaded;
    }

    public void setStringDateUploaded(String stringDateUploaded) {
        this.stringDateUploaded = stringDateUploaded;
    }

    public void setLocalFilePath(String localFilePath) {
        this.localFilePath = localFilePath;
    }

    public String getVideoID() {
        return videoID;
    }

    public void setVideoID(String videoID) {
        this.videoID = videoID;
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
        stringDateUploaded = sdf.format(new Date(dateUploaded));
        this.dateUploaded = dateUploaded;
    }
}
