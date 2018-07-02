package com.heimz.androidwebwechat;

import android.app.Activity;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RecordTool {


    private File mAudioSavePathFile = null;
    private String mAudioSavePath = null;
    private  MediaRecorder mMediaRecorder = null;
    private  SimpleDateFormat mDateFormat = new SimpleDateFormat(
            "yyyyMMddhhmmss");
    public  File mAudioFile = null;
    private MediaPlayer mMediaPlayer = null;
    public Activity context;
    public boolean isRecording = false;

    public RecordTool(Activity context) {
        // TODO Auto-generated constructor stub
        this.context = context;
        mAudioSavePath = Environment.getExternalStorageDirectory() + File.separator
                + "RecordAudio";
        mAudioSavePathFile = new File(mAudioSavePath);
        if (!mAudioSavePathFile.exists()) {
            mAudioSavePathFile.mkdir();
        }

        mMediaPlayer = new MediaPlayer();
    }

    public  void StartRecord(){


        if(isRecording)
        {
            Toast.makeText(context, "已经在录音" , Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            //Log.i("Garment0911", "mAudioSavePathFile" + mAudioSavePathFile.getAbsolutePath());

            mAudioFile = File.createTempFile("cldvoice"+mDateFormat.format(new Date()), ".amr", mAudioSavePathFile);
            //Log.i("Garment0911", "mAudioFile" + mAudioFile.getAbsolutePath());
            mMediaRecorder = new MediaRecorder();
			/* 设置录音来源为MIC */
            mMediaRecorder
                    .setAudioSource(MediaRecorder.AudioSource.MIC);
            mMediaRecorder
                    .setOutputFormat(MediaRecorder.OutputFormat.DEFAULT);
            mMediaRecorder
                    .setAudioEncoder(MediaRecorder.AudioEncoder.DEFAULT);
            mMediaRecorder.setOutputFile(mAudioFile.getAbsolutePath());
            mMediaRecorder.prepare();
            mMediaRecorder.start();
            isRecording = true;
            Toast.makeText(context, "开始录音" , Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }



    public void StopRecord(){

        if(!isRecording)
        {
            Toast.makeText(context, "没有在录音,请先开始录音" , Toast.LENGTH_SHORT).show();
            return;
        }

        if (mAudioFile != null) {
            mMediaRecorder.stop();
            mMediaRecorder.release();
            mMediaRecorder = null;
            isRecording  =  false;
            Toast.makeText(context, "录音完成" , Toast.LENGTH_SHORT).show();
        }
    }

    public void Play(){
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(mAudioFile.getAbsolutePath());
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }



    public void PlayWithFilePath(String FilePath){
        mMediaPlayer.reset();
        try {
            mMediaPlayer.setDataSource(FilePath);
            if (!mMediaPlayer.isPlaying()) {
                mMediaPlayer.prepare();
                mMediaPlayer.start();
            }
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
