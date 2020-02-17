package co.kr.bluebird.newrfid.app.bbrfidbtdemo.utility;

import android.widget.ProgressBar;

import java.util.Timer;
import java.util.TimerTask;

public class LocatedTimer {

    private TimerTask mLocateTimerTask;
    private Timer mClearLocateTimer;
    private ProgressBar mLocateProgress;

    public LocatedTimer(ProgressBar mLocateProgress_) {
        this.mLocateProgress = mLocateProgress_;
    }


    public void startLocateTimer() {
        stopLocateTimer();

        mLocateTimerTask = new TimerTask() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                locateTimeout();
            }
        };
        mClearLocateTimer = new Timer();
        mClearLocateTimer.schedule(mLocateTimerTask, 500);
    }

    private void stopLocateTimer() {
        if (mClearLocateTimer != null ) {
            mClearLocateTimer.cancel();
            mClearLocateTimer = null;
        }
    }

    private void locateTimeout() {
        mLocateProgress.setProgress(0);
    }
}
