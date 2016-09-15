package com.zis.musapp.base.utils;

import com.thin.downloadmanager.DefaultRetryPolicy;
import com.thin.downloadmanager.DownloadRequest;
import com.thin.downloadmanager.DownloadStatusListenerV1;
import com.thin.downloadmanager.ThinDownloadManager;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;

import javax.inject.Inject;

import rx.Observable;

/**
 * Created by mikhailz on 15/09/2016.
 */
public class DownloadUtils {

    private final Context mContext;

    private ThinDownloadManager downloadManager = new ThinDownloadManager();

    @Inject
    public DownloadUtils(final Context context) {
        mContext = context.getApplicationContext();

    }

    public Observable get(Uri downloadUri, Uri destinationUri, String authToken) {

        return Observable.create(subscriber -> {
            if (subscriber.isUnsubscribed()) {
                return;
            }
            DownloadRequest downloadRequest = new DownloadRequest(downloadUri)
                    .addCustomHeader("Auth-Token", authToken)
                    .setRetryPolicy(new DefaultRetryPolicy())
                    .setDestinationURI(destinationUri)
                    .setPriority(DownloadRequest.Priority.HIGH)
                    .setDownloadContext(mContext)
                    .setStatusListener(new DownloadStatusListenerV1() {
                        @Override
                        public void onDownloadComplete(DownloadRequest downloadRequest) {
                            subscriber.onCompleted();
                        }

                        @Override
                        public void onDownloadFailed(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
                            subscriber.onError(new DownloadException(downloadRequest, errorCode, errorMessage));
                        }

                        @Override
                        public void onProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
                            subscriber.onNext(new DownloadProgress(downloadRequest, totalBytes, downloadedBytes, progress));
                        }
                    });
            downloadManager.add(downloadRequest);
        });
    }

    public void cancelDownload(int downloadId) {
        downloadManager.cancel(downloadId);
    }

    public void cancelDownloadAll() {
        downloadManager.cancelAll();
    }

    public void release() {
        downloadManager.release();
    }

    /**
     * @return
     * STATUS_PENDING
     * STATUS_STARTED
     * STATUS_RUNNING
     */
    public int getStatus(int downloadId) {
        return downloadManager.query(downloadId);
    }

    public class DownloadException extends Exception {
        private final DownloadRequest mDownloadRequest;
        private final int mErrorCode;
        private final String mErrorMessage;


        public DownloadException(DownloadRequest downloadRequest, int errorCode, String errorMessage) {
            mDownloadRequest = downloadRequest;
            mErrorCode = errorCode;
            mErrorMessage = errorMessage;
        }

        public DownloadRequest getDownloadRequest() {
            return mDownloadRequest;
        }

        public int getErrorCode() {
            return mErrorCode;
        }

        public String getErrorMessage() {
            return mErrorMessage;
        }
    }

    public class DownloadProgress {
        private final DownloadRequest mDownloadRequest;
        private final long mTotalBytes;
        private final long mDownloadedBytes;
        private final int mProgress;

        public DownloadProgress(DownloadRequest downloadRequest, long totalBytes, long downloadedBytes, int progress) {
            mDownloadRequest = downloadRequest;
            mTotalBytes = totalBytes;
            mDownloadedBytes = downloadedBytes;
            mProgress = progress;
        }

        public DownloadRequest getDownloadRequest() {
            return mDownloadRequest;
        }

        public long getTotalBytes() {
            return mTotalBytes;
        }

        public long getDownloadedBytes() {
            return mDownloadedBytes;
        }

        public int getProgress() {
            return mProgress;
        }
    }
}
