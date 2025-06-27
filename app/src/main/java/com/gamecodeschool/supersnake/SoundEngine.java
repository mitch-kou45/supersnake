package com.gamecodeschool.supersnake;


import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;

import java.io.IOException;

public class SoundEngine {
    private final SoundPool mSP;
    private int mEat_ID = -1;
    private int mCrashID = -1;
    private int mBoom_id = -1;
    private int mPower_id = -1;
    private MediaPlayer mBackgroundPlayer;
    private static SoundEngine instance;
    private SoundEngine(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_MEDIA)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            mSP = new SoundPool.Builder()
                    .setMaxStreams(5)
                    .setAudioAttributes(audioAttributes)
                    .build();
        } else {
            mSP = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        }
        try {
            AssetManager assetManager = context.getAssets();
            AssetFileDescriptor descriptor;

            descriptor = assetManager.openFd("get_apple.ogg");
            mEat_ID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("snake_death.ogg");
            mCrashID = mSP.load(descriptor, 0);

            descriptor = assetManager.openFd("boom2.ogg");
            mBoom_id = mSP.load(descriptor,0);

            descriptor = assetManager.openFd("sound3.ogg");
            mPower_id = mSP.load(descriptor,0);

            mBackgroundPlayer = loadFromAssets(assetManager, "minecraft.ogg");

            mBackgroundPlayer.setLooping(true);
        } catch (IOException e) {
            // Error
        }
    }
    public static SoundEngine getInstance(Context context) {
        if (instance == null) {
            instance = new SoundEngine(context);
        }
        return instance;
    }
    private MediaPlayer loadFromAssets(AssetManager assetManager, String filename) throws IOException {
        MediaPlayer mediaPlayer = new MediaPlayer();
        AssetFileDescriptor assetFileDescriptor = assetManager.openFd(filename);
        mediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
        mediaPlayer.prepare();
        return mediaPlayer;
    }
    void playEat() {
        mSP.play(mEat_ID, 1, 1, 0, 0, 1);
    }

    void playCrash() {
        mSP.play(mCrashID, 1, 1, 0, 0, 1);
    }

    void playBoom() {
        mSP.play(mBoom_id, 1, 1, 0, 0, 1);
    }

    void playPower(){
        mSP.play(mPower_id, 1, 1, 0, 0, 1);
    }

    void playBackground() {
        if (mBackgroundPlayer != null && !mBackgroundPlayer.isPlaying()) {
            mBackgroundPlayer.start();
        }
    }


}


