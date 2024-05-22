package com.example.pomodorify.settings;

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
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.pomodorify.R;
import com.example.pomodorify.database.ChangeTimes;
import com.example.pomodorify.database.DBHelper;
import com.example.pomodorify.database.GetDarkThemePreferences;
import com.example.pomodorify.database.GetEndNotficationPreferences;
import com.example.pomodorify.database.GetTimes;
import com.example.pomodorify.database.SetDarkThemePreferences;
import com.example.pomodorify.database.SetEndNotificationPreferences;

public class Settings extends Fragment {

    public Settings() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        setupDarkThemeButton(view);

        setupNotificationButton(view);

        setupSoundButton(view);

        setupTimerLengthChange(view);

        return view;
    }

    private void setupDarkThemeButton(View view){
        GetDarkThemePreferences getDarkThemePreferences = DBHelper.getInstance(getActivity());

        SwitchCompat darkThemeSwitch = (SwitchCompat) view.findViewById(R.id.themeSwitch);
        darkThemeSwitch.setChecked(getDarkThemePreferences.getDarkThemePreferences());

        darkThemeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //remember user preferences
                SetDarkThemePreferences setDarkThemePreferences = DBHelper.getInstance(getActivity());
                setDarkThemePreferences.setDarkThemePreferences(isChecked);

                //turn on/off dark theme
                if(isChecked){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                } else {
                    AppCompatDelegate.setDefaultNightMode (AppCompatDelegate.MODE_NIGHT_NO);
                }
            }
        });
    }

    private void setupNotificationButton(View view){
        SetEndNotificationPreferences setEndNotificationPreferences = DBHelper.getInstance(getActivity());
        GetEndNotficationPreferences getEndNotficationPreferences = DBHelper.getInstance(getActivity());

        ToggleButton timerEndNotificationButton = (ToggleButton) view.findViewById(R.id.notisSessionFinished);
        timerEndNotificationButton.setChecked(getEndNotficationPreferences.getEndNotificationPreferences());

        //handles what to do according to whether user gave permission to send notification
        ActivityResultLauncher<String> requestPermissionLauncher =
                registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                    if (!isGranted) {//user refused to give permission for the first time
                        timerEndNotificationButton.setChecked(false);//switch button back to false
                    }
                });

        timerEndNotificationButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                setEndNotificationPreferences.setEndNotificationPreferences(isChecked);//save user preferences

                if(isChecked){
                    //Handle request for permissions when user turns notifications on
                    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){//in earlier versions app will ask for permission on its own
                        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.POST_NOTIFICATIONS)) {//when user declined permission before
                            Toast.makeText(getContext(), "You must give permission to send notifications first.", Toast.LENGTH_LONG).show();

                            timerEndNotificationButton.setChecked(false);//set to false because we don't know yet, if user will allow for notifications or not

                            //redirect user to settings
                            Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getContext().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                        else if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);//ask for permission (when user hasn't declined permission before)
                        }
                    }
                }

            }
        });
    }

    private void setupSoundButton(View view){
        GetEndNotficationPreferences getEndNotficationPreferences = DBHelper.getInstance(getActivity());

        ToggleButton timerEndSoundButton = (ToggleButton) view.findViewById(R.id.soundSessionFinished);
        timerEndSoundButton.setChecked(getEndNotficationPreferences.getEndSoundPreferences());
        timerEndSoundButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SetEndNotificationPreferences setEndNotificationPreferences = DBHelper.getInstance(getActivity());
                setEndNotificationPreferences.setEndSoundPreferences(isChecked);
            }
        });
    }

    private void setupTimerLengthChange(View view){
        GetTimes getTimes = DBHelper.getInstance(getActivity());
        ChangeTimes changeTimes = DBHelper.getInstance(getActivity());

        //setup settings for focus timer
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
                //save to database
                changeTimes.ChangeFocus(focusBar.getProgress() + 1);
            }
        });

        //setup settings for short break timer
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
                //save to database
                changeTimes.ChangeShortBreak(shortBar.getProgress() + 1);
            }
        });

        //setup settings for long break timer
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
                //send to database
                changeTimes.ChangeLongBreak(longBar.getProgress() + 15);
            }
        });
    }

}