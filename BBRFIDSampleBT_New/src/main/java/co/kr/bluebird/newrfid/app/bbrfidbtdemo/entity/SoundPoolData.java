package co.kr.bluebird.newrfid.app.bbrfidbtdemo.entity;

import android.media.SoundPool;

public class SoundPoolData {
    private SoundPool soundPool;
    private int soundId;
    private float soundVolumen;

    public SoundPool getSoundPool() {
        return soundPool;
    }

    public void setSoundPool(SoundPool soundPool) {
        this.soundPool = soundPool;
    }

    public int getSoundId() {
        return soundId;
    }

    public void setSoundId(int soundId) {
        this.soundId = soundId;
    }

    public float getSoundVolumen() {
        return soundVolumen;
    }

    public void setSoundVolumen(float soundVolumen) {
        this.soundVolumen = soundVolumen;
    }
}
