package com.example.pomodorify.pomodoro;

import android.widget.ProgressBar;
import android.widget.TextView;
import android.os.SystemClock;

import com.example.pomodorify.app.Utility;

public class Timer extends CountDownTimer {

    protected NotifyPomodoro listener;

    protected TextView timeLeft;

    protected ProgressBar progressBar;

    private long mPauseTime;//for pause functionality

    public void setCustomObjectListener(NotifyPomodoro listener) {
        this.listener = listener;
    }

    public Timer(int minutes, long countDownInterval, TextView timeLeft, ProgressBar progressBar) {
        super(Utility.SecondsToMinutesOnRelease((long)minutes * 1000), countDownInterval);
        this.listener = null;
        this.timeLeft = timeLeft;
        this.progressBar = progressBar;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        String time = Utility.formatMillis(millisUntilFinished);
        timeLeft.setText(time);
        progressBar.setProgress((int) (millisUntilFinished/1000));
    }

    @Override
    public void onFinish() {
        if (listener != null)//notify pomodoro.java that you stopped counting so that it can update UI
            listener.onFinish();
    }

    //Those methods extend/modify CountDownTimer so that we can resume timer:

    @Override
    public synchronized void cancel() {
        super.cancel();
        mPauseTime = mStopTimeInFuture - SystemClock.elapsedRealtime();//save time
    }

    public synchronized final CountDownTimer resume() {
        mCancelled = false;
        if (mMillisInFuture <= 0) {
            onFinish();
            return this;
        }
        mStopTimeInFuture = mPauseTime + SystemClock.elapsedRealtime();
        mHandler.sendMessage(mHandler.obtainMessage(MSG));
        return this;
    }

    public boolean isCancelled(){
        return mCancelled;
    }
}
