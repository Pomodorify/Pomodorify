package com.example.pomodorify.pomodoro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.pomodorify.R;
import com.example.pomodorify.app.Utility;
import com.example.pomodorify.database.DBHelper;
import com.example.pomodorify.database.GetEndNotficationPreferences;
import com.example.pomodorify.database.GetTimes;
import com.example.pomodorify.database.InsertStatistics;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class Pomodoro extends Fragment{

    private Timer timer;
    private Button btnStartStop;
    private TextView timeLeft;
    private RadioGroup radioGroup;
    private ProgressBar progressBar;

    public Pomodoro() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        timer = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(timer != null)
            timer.cancel();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pomodoro, container, false);

        btnStartStop = view.findViewById(R.id.startStop);
        timeLeft = view.findViewById(R.id.timeLeft);
        btnStartStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchCounting();
            }
        });


        radioGroup = view.findViewById(R.id.selectType);

        RadioButton FocusButton = radioGroup.findViewById(R.id.FocusButton);
        RadioButton sBreakButton = radioGroup.findViewById(R.id.sBreakButton);
        RadioButton lBreakButton = radioGroup.findViewById(R.id.lBreakButton);

        FocusButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    setFocus();
            }
        });

        sBreakButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    setShortBreak();
            }
        });

        lBreakButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                    setLongBreak();
            }
        });

        Button continueButton = view.findViewById(R.id.pauseButton);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pauseTimer();
            }
        });

        GetTimes getTimes = DBHelper.getInstance(getActivity());
        int time = getTimes.getFocusTime();
        timeLeft.setText(Utility.formatMillis(Utility.SecondsToMinutesOnRelease((long)time * 1000)));

        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax((int) Utility.SecondsToMinutesOnRelease((long) time));

        // Inflate the layout for this fragment
        return view;
    }

    private void switchCounting(){//if not counting then start counting, if counting then stop counting

        if(timer == null){//then start counting

            //type of activity - focus/break
            int selectedId = getSelectedRadioId(radioGroup);

            //get duration from database
            int minutes = getMinutesDatabase(selectedId);

            //get activity label
            EditText activity = getActivity().findViewById(R.id.activity);
            String activityLabel = activity.getText().toString();

            if(selectedId == getActivity().findViewById(R.id.FocusButton).getId()){
                InsertStatistics insertStatistics = DBHelper.getInstance(getActivity());;

                timer = new FocusTimer(minutes, 1000, timeLeft, insertStatistics, activityLabel, progressBar);
            }
            else
                timer = new Timer(minutes, 1000, timeLeft, progressBar);

            timer.setCustomObjectListener(new NotifyPomodoro(){
                @Override
                public void onFinish() {
                    //If user turned on notifications then send notification about end of session
                    sendNotificationIfPossible(activityLabel);

                    //Bring UI back to ready-for-counting state
                    resetCounting();
                }
            });
            timer.start();

            setupCounting();
        }else{//then stop counting
            timer.cancel();
            resetCounting();
        }
    }

    private int getMinutesDatabase(int selectedId){
        GetTimes getTimes = DBHelper.getInstance(getActivity());
        if(selectedId == getActivity().findViewById(R.id.FocusButton).getId()){
            return getTimes.getFocusTime();
        }else if(selectedId == getActivity().findViewById(R.id.sBreakButton).getId()){
            return getTimes.getShortBreakTime();
        }else{
            return getTimes.getLongBreakTime();
        }
    }

    private void setupCounting(){//set UI to counting mode, so for example enables pause button etc.

        btnStartStop.setText("Reset");

        enablePauseButton();

        disableButtons();
    }

    private void resetCounting(){//set UI to no counting mode
        timer = null;
        btnStartStop.setText("Start");

        disablePauseButton();

        enableButtons();

        Button continueButton = getActivity().findViewById(R.id.pauseButton);
        continueButton.setText("Pause");

        int selectedId = getSelectedRadioId(radioGroup);
        int time = getMinutesDatabase(selectedId);

        timeLeft.setText(Utility.formatMillis(Utility.SecondsToMinutesOnRelease((long)time * 1000)));

        progressBar.setProgress(0);
    }

    private void disableButtons(){
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(false);
        }

        EditText activity = getActivity().findViewById(R.id.activity);
        activity.setEnabled(false);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setEnabled(false);
        }

    }

    private void enableButtons(){
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            radioGroup.getChildAt(i).setEnabled(true);
        }

        EditText activity = getActivity().findViewById(R.id.activity);
        activity.setEnabled(true);

        BottomNavigationView bottomNavigationView = getActivity().findViewById(R.id.bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        for (int i = 0; i < menu.size(); i++) {
            MenuItem menuItem = menu.getItem(i);
            menuItem.setEnabled(true);
        }
    }

    public void setFocus(){
        GetTimes getTimes = DBHelper.getInstance(getActivity());
        int time = getTimes.getFocusTime();

        timeLeft.setText(Utility.formatMillis(Utility.SecondsToMinutesOnRelease((long)time * 1000)));
        progressBar.setMax((int) Utility.SecondsToMinutesOnRelease(time));
    }

    public void setLongBreak(){
        GetTimes getTimes = DBHelper.getInstance(getActivity());
        int time = getTimes.getLongBreakTime();

        timeLeft.setText(Utility.formatMillis(Utility.SecondsToMinutesOnRelease((long)time * 1000)));
        progressBar.setMax((int) Utility.SecondsToMinutesOnRelease(time));
    }

    public void setShortBreak(){
        GetTimes getTimes = DBHelper.getInstance(getActivity());
        int time = getTimes.getShortBreakTime();

        timeLeft.setText(Utility.formatMillis(Utility.SecondsToMinutesOnRelease((long)time * 1000)));
        progressBar.setMax((int) Utility.SecondsToMinutesOnRelease(time));
    }

    private void enablePauseButton(){
        Button continueButton = getActivity().findViewById(R.id.pauseButton);
        continueButton.setEnabled(true);
    }

    private void disablePauseButton(){
        Button continueButton = getActivity().findViewById(R.id.pauseButton);
        continueButton.setEnabled(false);
    }

    private int getSelectedRadioId(RadioGroup radioGroup){
        RadioButton radioButton = radioGroup.findViewById( radioGroup.getCheckedRadioButtonId());
        return radioButton.getId();
    }

    private void pauseTimer(){
        if(timer != null){
            if(!timer.isCancelled()){
                timer.cancel();
                Button continueButton = getActivity().findViewById(R.id.pauseButton);
                continueButton.setText("Resume");
            }else{
                timer.resume();
                Button continueButton = getActivity().findViewById(R.id.pauseButton);
                continueButton.setText("Pause");
            }
        }
    }

    private void sendNotificationIfPossible(String activityLabel){
        //Check user preferences regarding sending notification and act accordingly
        GetEndNotficationPreferences getEndNotficationPreferences = DBHelper.getInstance(getActivity());

        boolean sound = getEndNotficationPreferences.getEndSoundPreferences();
        boolean notification = getEndNotficationPreferences.getEndNotificationPreferences();

        TimerEndNotification timerEndNotification = new TimerEndNotification(getContext());

        //create text to display in notification
        String displayText = "";
        if(notification){
            if(!activityLabel.isEmpty())
                displayText = "Your session " + activityLabel + " just ended!";
            else
                displayText = "Your session just ended!";
        }

        if(notification && sound){
            timerEndNotification.buildStandardNotification(displayText);
            timerEndNotification.sendNotification();
        } else if (notification) {
            timerEndNotification.buildSoundLessNotification(displayText);
            timerEndNotification.sendNotification();
        } else if (sound) {
            timerEndNotification.playSoundOnly();
        }
    }

}