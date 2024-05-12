package com.example.pomodorify;

import android.content.Intent;
import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;




public class Settings extends Fragment {

    public Settings() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        GetTimes getTimes = new DBHelper(getActivity());
        ChangeTimes changeTimes = new DBHelper(getActivity());

        //ustawienie dlugosci timera focus
        SeekBar focusBar = view.findViewById(R.id.focusBar);
        TextView focusDuration = view.findViewById(R.id.focusDuration);
        int focusTime = getTimes.getFocusTime();
        focusBar.setProgress(focusTime - 1);
        focusDuration.setText(String.valueOf(focusTime));
        focusBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                focusDuration.setText(String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //wyslij do bazy danych
                changeTimes.ChangeFocus(focusBar.getProgress() + 1);
            }
        });

        //ustawienie dlugosci timera shortbreak
        SeekBar shortBar = view.findViewById(R.id.shortBar);
        TextView shortDuration = view.findViewById(R.id.shortDuration);
        int shortTime = getTimes.getShortBreakTime();
        shortBar.setProgress(shortTime - 1);
        shortDuration.setText(String.valueOf(shortTime));
        shortBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                shortDuration.setText(String.valueOf(progress + 1));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //wyslij do bazy danych
                changeTimes.ChangeShortBreak(shortBar.getProgress() + 1);
            }
        });

        //ustawienie dlugosci timera long break
        SeekBar longBar = view.findViewById(R.id.longBar);
        TextView longDuration = view.findViewById(R.id.longDuration);
        int longTime = getTimes.getLongBreakTime();
        longBar.setProgress(longTime - 15);
        longDuration.setText(String.valueOf(longTime));
        longBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                longDuration.setText(String.valueOf(progress + 15));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //wyslij do bazy danych
                changeTimes.ChangeLongBreak(longBar.getProgress() + 15);
            }
        });

        //Notification and sound after timer ends buttons
        SetEndNotificationPreferences setEndNotificationPreferences = new DBHelper(getActivity());
        GetEndNotficationPreferences getEndNotficationPreferences = new DBHelper(getActivity());

        ToggleButton timerEndNotificationButton = (ToggleButton) view.findViewById(R.id.notisSessionFinished);
        timerEndNotificationButton.setChecked(getEndNotficationPreferences.getEndNotificationPreferences());

        //handles what to do according to whether user gave permission to send notification
        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (!isGranted) {//user refused to give permission
                        timerEndNotificationButton.setChecked(false);
                    }
                });

        timerEndNotificationButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setEndNotificationPreferences.setEndNotificationPreferences(isChecked);//save user preferences

                if(isChecked){
                    //Handle request for permissions when user turns notifications on
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){//in earlier versions app will ask for permission on its own
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.POST_NOTIFICATIONS)) {
                            Toast.makeText(getContext(), "You must give permission to send notifications first.", Toast.LENGTH_LONG).show();

                            timerEndNotificationButton.setChecked(false);//set to false because we don't know yet, if user will allow for notifications or not

                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                        else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {//doesnt have permission
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
                        }
                    }
                }

            }
        });

        ToggleButton timerEndSoundButton = (ToggleButton) view.findViewById(R.id.soundSessionFinished);
        timerEndSoundButton.setChecked(getEndNotficationPreferences.getEndSoundPreferences());
        timerEndSoundButton .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setEndNotificationPreferences.setEndSoundPreferences(isChecked);
            }
        });

        //read state of dark theme switch from db
        GetDarkThemePreferences getDarkThemePreferences = new DBHelper(getActivity());

        SwitchCompat darkThemeSwitch = (SwitchCompat) view.findViewById(R.id.themeSwitch);
        darkThemeSwitch.setChecked(getDarkThemePreferences.getDarkThemePreferences());

        //save state of dark theme switch from db
        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SetDarkThemePreferences setDarkThemePreferences = new DBHelper(getActivity());
                setDarkThemePreferences.setDarkThemePreferences(isChecked);

                if(isChecked){
                    //darkThemeOn();
                }
            }
        });

        return view;
    }



}