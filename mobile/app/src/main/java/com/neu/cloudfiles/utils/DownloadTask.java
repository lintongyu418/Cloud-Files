package com.neu.cloudfiles.utils;

import android.os.AsyncTask;
import android.util.Pair;

import com.neu.cloudfiles.bean.DownloadFileVo;
import com.neu.cloudfiles.event.UpdateDownloadStatus;
import com.neu.cloudfiles.constant.Constant;

import okhttp3.ResponseBody;

public class DownloadTask extends AsyncTask<ResponseBody, Pair<Integer, Long>, UpdateDownloadStatus> {

    private DownloadFileVo fileVo;

    public DownloadTask(DownloadFileVo fileVo) {
        this.fileVo = fileVo;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected UpdateDownloadStatus doInBackground(ResponseBody... responseBodies) {
        String result = FileUtils.saveToDisk(this,
                responseBodies[0],
                this.fileVo.getFileName(),
                fileVo.getFileSize());
        String status = !result.isEmpty() ? Constant.DOWNLOAD_SUCCESS : Constant.DOWNLOAD_FAILED;
        return new UpdateDownloadStatus(this.fileVo.getUserFileId(), status, result);
    }

    @Override
    protected void onProgressUpdate(Pair<Integer, Long>... values) {
    }

    public void doProgress(Pair<Integer, Long> progressDetails) {
        publishProgress(progressDetails);
    }

    @Override
    protected void onPostExecute(UpdateDownloadStatus status) {
        RxBus.getInstance().post(status);
    }

}
