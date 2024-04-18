package com.example.pomodorify;

import android.os.CountDownTimer;
import android.widget.TextView;

public class Timer extends CountDownTimer {

    protected NotifyPomodoro listener;
    private TextView timeLeft;

    public Timer(){
        super(1000,1000);
        this.listener = null;
        this.timeLeft = null;
    };

    public void setCustomObjectListener(NotifyPomodoro listener) {

    }

    public Timer(long millisInFuture, long countDownInterval, TextView timeLeft) {
        super(millisInFuture, countDownInterval);
    }

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {

    }
}
