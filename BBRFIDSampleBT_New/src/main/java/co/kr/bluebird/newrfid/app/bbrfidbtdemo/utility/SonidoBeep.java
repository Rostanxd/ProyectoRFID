package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.annotation.TargetApi;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Build;

//import co.kr.bluebird.newrfid.app.bbrfidbtdemo.R;
import co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity.SoundPoolData;

public class SonidoBeep {

    private Context mContext;
    private SoundPool mSoundPool;
    private float mSoundVolume;
    private int mSoundId = 0;
    private int beep;

    public SonidoBeep(Context context, int beep_){
        mContext = context;
        this.beep = beep_;
    }

    public SoundPoolData getSoundPool(){
        SoundPoolData soundPoolData = new SoundPoolData();
        createSoundPool();
        soundPoolData.setSoundPool(mSoundPool);
        soundPoolData.setSoundVolumen(mSoundVolume);
        soundPoolData.setSoundId(mSoundId);
        return soundPoolData;
    }
    private void createSoundPool() {
        boolean b = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            b = createNewSoundPool();
        else
            b = createOldSoundPool();
        if (b) {
            AudioManager audioManager = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
            float actVolume = (float) audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            float maxVolume = (float) audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            mSoundVolume = actVolume / maxVolume;
            SoundLoadListener();
        }
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private boolean createNewSoundPool(){
        AudioAttributes attributes = new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION).build();
        mSoundPool = new SoundPool.Builder().setAudioAttributes(attributes).setMaxStreams(5).build();
        if (mSoundPool != null)
            return true;
        return false;
    }

    @SuppressWarnings("deprecation")
    private boolean createOldSoundPool(){
        mSoundPool = new SoundPool(5, AudioManager.STREAM_MUSIC, 0);
        if (mSoundPool != null)
            return true;
        return false;
    }

    private void SoundLoadListener() {

        if (mSoundPool != null) {
            mSoundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
                @Override
                public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
                    // TODO Auto-generated method stub

                }
            });
            //mSoundId = mSoundPool.load(mContext, R.raw.beep, 1);
            mSoundId = mSoundPool.load(mContext, beep, 1);
        }

    }
}
