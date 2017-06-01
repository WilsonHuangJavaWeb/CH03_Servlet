package com.servlet.upload;

import org.apache.commons.fileupload.ProgressListener;

/**
 * Created by ki264 on 2017/6/1.
 */
public class UploadListener implements ProgressListener {

    private UploadStatus status;//紀錄上船資訊的java bean

    public UploadListener(UploadStatus status) {//建構式
        this.status = status;
    }

    /**
     * 上傳元件會呼叫該方法
     *
     * @param bytesRead
     * @param contentLength
     * @param items
     */
    @Override
    public void update(long bytesRead, long contentLength, int items) {
        status.setBytesRead(bytesRead);//已讀取的資料長度
        status.setContentLength(contentLength); //檔案的總長度
        status.setItems(items);//正在儲存第幾個檔案

    }
}
