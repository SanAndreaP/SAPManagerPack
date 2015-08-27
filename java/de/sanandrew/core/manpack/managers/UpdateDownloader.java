/*******************************************************************************************************************
 * Authors:   SanAndreasP
 * Copyright: SanAndreasP
 * License:   Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International
 *                http://creativecommons.org/licenses/by-nc-sa/4.0/
 *******************************************************************************************************************/
package de.sanandrew.core.manpack.managers;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Observable;

/**
 * contains code from http://stackoverflow.com/a/14069976
 */
public class UpdateDownloader
        extends Observable
        implements Runnable
{
    // Max size of download buffer.
    private static final int MAX_BUFFER_SIZE = 1024;

    private final URL url; // download URL
    private int size; // size of download in bytes
    private int downloaded; // number of bytes downloaded
    private final File modJar;
    private volatile EnumDlState status; // current status of download
    private Runnable execWhenSucceed;

    // Constructor for Download.
    public UpdateDownloader(URL url, File jar) {
        this.url = url;
        this.size = -1;
        this.downloaded = 0;
        this.status = EnumDlState.PENDING;
        this.modJar = jar;

        // Begin the download.
        this.download();
    }

    public void setSucceedRunnable(Runnable runnable) {
        this.execWhenSucceed = runnable;
    }

    // Get this download's URL.
    public String getUrl() {
        return this.url.toString();
    }

    // Get this download's size.
    public int getSize() {
        return this.size;
    }

    // Get this download's progress.
    public float getProgress() {
        return ((float) this.downloaded / this.size) * 100;
    }

    // Get this download's status.
    public EnumDlState getStatus() {
        return this.status;
    }

    // Pause this download.
    public void pause() {
        this.status = EnumDlState.PAUSED;
        this.stateChanged();
    }

    // Resume this download.
    public void resume() {
        this.status = EnumDlState.DOWNLOADING;
        this.stateChanged();
        this.download();
    }

    // Cancel this download.
    public void cancel() {
        this.status = EnumDlState.CANCELLED;
        this.stateChanged();
    }

    // Mark this download as having an error.
    private void error() {
        this.status = EnumDlState.ERROR;
        this.stateChanged();
    }

    // Start or resume downloading.
    private void download() {
        Thread thread = new Thread(this);
        thread.start();
    }

    // Get file name portion of URL.
    private static String getFileName(URL url) {
        String fileName = url.getFile();
        return fileName.substring(fileName.lastIndexOf('/') + 1);
    }

    // Download file.
    public void run() {
        try {
            HttpURLConnection connection = (HttpURLConnection) this.url.openConnection();

            connection.setRequestProperty("Range", "bytes=" + this.downloaded + '-');       // Specify what portion of file to download.

            connection.connect();

            if( connection.getResponseCode() / 100 != 2 ) {              // Make sure response code is in the 200 range.
                this.error();
            }

            int contentLength = connection.getContentLength();          // Check for valid content length.
            if( contentLength < 1 ) {
                this.error();
            }

            if( this.size == -1 ) {                   // Set the size for this download if it hasn't been already set.
                this.size = contentLength;
                this.stateChanged();
            }

            try( RandomAccessFile file = new RandomAccessFile(new File(this.modJar.getParent(), getFileName(this.url)), "rw");
                 InputStream stream = connection.getInputStream() )
            {
                file.seek(this.downloaded);         // seek to the end of the file.

                this.status = EnumDlState.DOWNLOADING;
                this.stateChanged();
                while( this.status == EnumDlState.DOWNLOADING ) {
                    byte buffer[];   // Size buffer according to how much of the file is left to download.

                    if( this.size - this.downloaded > MAX_BUFFER_SIZE ) {
                        buffer = new byte[MAX_BUFFER_SIZE];
                    } else {
                        buffer = new byte[this.size - this.downloaded];
                    }

                    int read = stream.read(buffer);     // Read from server into buffer.
                    if( read == -1 ) {
                        break;
                    }

                    file.write(buffer, 0, read);        // Write buffer to file.
                    this.downloaded += read;
                    this.stateChanged();
                }
            }

            if( this.status == EnumDlState.DOWNLOADING ) {          // Change status to complete if this point was reached because downloading has finished.
                this.status = EnumDlState.COMPLETE;
                this.stateChanged();

                if( !this.modJar.delete() ) {
                    error();
                }

                if( UpdateDownloader.this.execWhenSucceed != null ) {
                    UpdateDownloader.this.execWhenSucceed.run();
                }
            }
        } catch( IOException | SecurityException e ) {
            error();
        }
    }

    private void stateChanged() {
        this.setChanged();
        this.notifyObservers();
    }

    public enum EnumDlState
    {
        DOWNLOADING, PAUSED, COMPLETE, CANCELLED, ERROR, PENDING
    }
}
