package com.example.pomodorify;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class Timer extends CountDownTimer {

    protected NotifyPomodoro listener;//tylko foucstimer lub braektimer moga z tego skorzystac bo protected
    private TextView timeLeft;

    public Timer(){
        super(1000,1000);
        this.listener = null;
        this.timeLeft = null;
    };

    public void setCustomObjectListener(NotifyPomodoro listener) {
        this.listener = listener;
    }

    public Timer(long millisInFuture, long countDownInterval, TextView timeLeft) {
        super(millisInFuture, countDownInterval);
        this.listener = null;
        this.timeLeft = timeLeft;
    }

    @Override
    public void onTick(long millisUntilFinished) {
        String time = Utility.formatMillis(millisUntilFinished);
        timeLeft.setText(time);
    }

    @Override
    public void onFinish() {
        //timer break wiec nie wstawia statystyk
        if (listener != null)//powiadom pomodor o tym ze przestales liczyc
            listener.onFinish();
    }
}
