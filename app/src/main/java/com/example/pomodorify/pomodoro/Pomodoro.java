package com.example.pomodorify.pomodoro;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    public void onStart() {//This function is called when a fragment becomes visible to the user. It is where you start any animations or update the UI.
        super.onStart();

        /*
            if not counting then set timer based on which button is selected, usefull for example when user selects short timer,
            goes to settings and goes back to pomodoro because fragment remember itself which button was chosen
         */
        if(timer == null){
            int selectedId = getSelectedRadioId(radioGroup);
            int time = getMinutesDatabase(selectedId);

            timeLeft.setText(Utility.formatMillis(time * 1000));
            progressBar.setMax(time);
        }

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
        FocusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setFocus();
            }
        });
        sBreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setShortBreak();
            }
        });
        lBreakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
        timeLeft.setText(Utility.formatMillis((long)time * 1000));

        //znajdz progress bar
        progressBar = view.findViewById(R.id.progressBar);
        progressBar.setMax(time);//potem trzeba dodac *60 jesli zmienimy z odliczania sekund na minuty

        // Inflate the layout for this fragment
        return view;
    }

    private void switchCounting(){//jesli nie odlicza to zacznij odliczac, jesli odlicza to przerwij odliczac, na to jest ustawiony onclick

        //zamiast tego if .. else można dać zrobić state pattern: https://www.geeksforgeeks.org/state-design-pattern/
        if(timer == null){//zacznij liczyc

            //Odczytaj rodzaj aktywnosci focus/break
            int selectedId = getSelectedRadioId(radioGroup);

            //pobierz ilosc minut z bazy danych dla danej aktywnosci
            int minutes = getMinutesDatabase(selectedId);

            //pobierz etykiete aktywnosci
            EditText activity = getActivity().findViewById(R.id.activity);
            String activityLabel = activity.getText().toString();

            if(selectedId == getActivity().findViewById(R.id.FocusButton).getId()){//jesli sesja focus
                InsertStatistics insertStatistics = DBHelper.getInstance(getActivity());;

                timer = new FocusTimer(minutes * 1000, 1000, timeLeft, insertStatistics, activityLabel, progressBar);
            }
            else//jesli sesja break
                timer = new Timer(minutes * 1000, 1000, timeLeft, progressBar);

            timer.setCustomObjectListener(new NotifyPomodoro(){
                @Override
                public void onFinish() {
                    //If user turned on notifications then send notification about end of session
                    sendNotificationIfPossible(activityLabel);

                    //Bring UI back to ready-for-counting state
                    resetCounting();
                }
            });
            //tutaj fajnie widać 3 zasade SOLID, nie wazne czy timer to Timer czy FocusTimer i tak sie poprawnie zachowa
            timer.start();

            setupCounting();
        }else{//przestan liczyc
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

    private void setupCounting(){//ustawia UI w tryb odliczania czyli np. pojawia sie przycisk pause

        btnStartStop.setText("Reset");

        enablePauseButton();

        disableButtons();
    }

    private void resetCounting(){//ustawia UI w tryb w ktorym nie odlicza
        timer = null;
        btnStartStop.setText("Start");

        disablePauseButton();

        enableButtons();

        Button continueButton = getActivity().findViewById(R.id.pauseButton);
        continueButton.setText("Pause");

        //zaaktualizuj timer, np. wpisz 25 jesli wybrany focus
        int selectedId = getSelectedRadioId(radioGroup);
        int time = getMinutesDatabase(selectedId);

        timeLeft.setText(Utility.formatMillis((long)time * 1000));

        //ustaw progressBar na 0
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

        timeLeft.setText(Utility.formatMillis((long)time * 1000));
        progressBar.setMax(time);
    }

    public void setLongBreak(){
        GetTimes getTimes = DBHelper.getInstance(getActivity());
        int time = getTimes.getLongBreakTime();

        timeLeft.setText(Utility.formatMillis((long)time * 1000));
        progressBar.setMax(time);
    }

    public void setShortBreak(){
        GetTimes getTimes = DBHelper.getInstance(getActivity());
        int time = getTimes.getShortBreakTime();

        timeLeft.setText(Utility.formatMillis((long)time * 1000));
        progressBar.setMax(time);
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