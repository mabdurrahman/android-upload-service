package com.alexbbb.uploadservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Broadcast receiver from which to inherit when creating a receiver for
 * {@link UploadService}.
 *
 * It provides the boilerplate code to properly handle broadcast messages coming from the
 * upload service and dispatch them to the proper handler method.
 *
 * @author alexbbb (Alex Gotev)
 * @author eliasnaur
 * @author cankov
 * @author mabdurrahman
 *
 */
public class UploadServiceBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            if (UploadService.getActionBroadcast().equals(intent.getAction())) {
                final int status = intent.getIntExtra(UploadService.STATUS, 0);
                final String uploadId = intent.getStringExtra(UploadService.UPLOAD_ID);

                switch (status) {
                    case UploadService.STATUS_ERROR:
                        final Exception exception = (Exception) intent
                                .getSerializableExtra(UploadService.ERROR_EXCEPTION);
                        onError(uploadId, exception);
                        break;

                    case UploadService.STATUS_COMPLETED:
                        final int responseCode = intent.getIntExtra(UploadService.SERVER_RESPONSE_CODE, 0);
                        final String responseMsg = intent.getStringExtra(UploadService.SERVER_RESPONSE_MESSAGE);
                        onCompleted(uploadId, responseCode, responseMsg);
                        break;

                    case UploadService.STATUS_IN_PROGRESS:
                        final int progress = intent.getIntExtra(UploadService.PROGRESS, 0);
                        onProgress(uploadId, progress);

                        final long uploadedBytes = intent.getLongExtra(UploadService.PROGRESS_UPLOADED_BYTES, 0);
                        final long totalBytes = intent.getLongExtra(UploadService.PROGRESS_TOTAL_BYTES, 1);
                        onProgress(uploadId, uploadedBytes, totalBytes);

                        break;

                    case UploadService.STATUS_CANCELLED:
                        onCancelled(uploadId);
                        break;

                    default:
                        break;
                }
            }
        }

    }

    /**
     * Register this upload receiver.
     * If you use this receiver in an {@link android.app.Activity}, you have to call this method inside
     * {@link android.app.Activity#onResume()}, after {@code super.onResume();}.
     * If you use it in a {@link android.app.Service}, you have to
     * call this method inside {@link android.app.Service#onCreate()}, after {@code super.onCreate();}.
     *
     * @param context context in which to register this receiver
     */
    public void register(final Context context) {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(UploadService.getActionBroadcast());
        context.registerReceiver(this, intentFilter);
    }

    /**
     * Unregister this upload receiver.
     * If you use this receiver in an {@link android.app.Activity}, you have to call this method inside
     * {@link android.app.Activity#onPause()}, after {@code super.onPause();}.
     * If you use it in a {@link android.app.Service}, you have to
     * call this method inside {@link android.app.Service#onDestroy()}.
     *
     * @param context context in which to unregister this receiver
     */
    public void unregister(final Context context) {
        context.unregisterReceiver(this);
    }

    /**
     * Called when the upload progress changes. Override this method to add your own logic.
     *
     * @param uploadId unique ID of the upload request
     * @param progress value from 0 to 100
     */
    public void onProgress(final String uploadId, final int progress) {
    }

    /**
     * Called when the upload progress changes. Override this method to add your own logic.
     *
     * @param uploadId unique ID of the upload request
     * @param uploadedBytes the count of the bytes uploaded so far
     * @param totalBytes the total expected bytes to upload
     */
    public void onProgress(final String uploadId, final long uploadedBytes, final long totalBytes) {
    }

    /**
     * Called when an error happens during the upload. Override this method to add your own logic.
     *
     * @param uploadId unique ID of the upload request
     * @param exception exception that caused the error
     */
    public void onError(final String uploadId, final Exception exception) {
    }

    /**
     * Called when the upload is completed successfully. Override this method to add your own logic.
     *
     * @param uploadId unique ID of the upload request
     * @param serverResponseCode status code returned by the server
     * @param serverResponseMessage string containing the response received from the server.
     *                              If your server responds with a JSON, you can parse it from
     *                              this string using a library such as org.json
     *                              (embedded in Android) or google's gson
     */
    public void onCompleted(final String uploadId, final int serverResponseCode,
                            final String serverResponseMessage) {
    }

    /**
     * Called when the upload is cancelled. Override this method to add your own logic.
     *
     * @param uploadId unique ID of the upload request
     */
    public void onCancelled(final String uploadId) {
    }
}