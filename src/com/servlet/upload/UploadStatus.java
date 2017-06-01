package com.servlet.upload;

/**
 * Created by ki264 on 2017/6/1.
 */
public class UploadStatus {
    private long bytesRead;//已上傳的位元組數，單位:位元組
    private long contentLength;//所有檔案的總長度，單位:位元組
    private int items;//正在上傳第幾個檔案
    private long startTime = System.currentTimeMillis();//開始上傳的時間，用於計算上傳速率等

    public long getBytesRead() {
        return bytesRead;
    }

    public void setBytesRead(long bytesRead) {
        this.bytesRead = bytesRead;
    }

    public long getContentLength() {
        return contentLength;
    }

    public void setContentLength(long contentLength) {
        this.contentLength = contentLength;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
}
