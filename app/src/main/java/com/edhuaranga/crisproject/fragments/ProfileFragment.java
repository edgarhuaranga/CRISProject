package com.edhuaranga.crisproject.fragments;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.edhuaranga.crisproject.AlarmReceiver;
import com.edhuaranga.crisproject.OrtodentechApplication;
import com.edhuaranga.crisproject.R;
import com.edhuaranga.crisproject.model.User;
import com.edhuaranga.crisproject.utils.SharedPreferencesUtil;

import java.util.Calendar;


public class ProfileFragment extends Fragment implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    private PendingIntent pendingIntent;
    AlarmManager alarmManager;


    TextView textViewUserName;
    TextView textViewUserMail;

    Switch switchOne;
    Switch switchTwo;
    Switch switchThree;
    ImageView buttonEditAlarmOne;
    ImageView buttonEditAlarmTwo;
    ImageView buttonEditAlarmThree;
    TextView textViewTimeAlarmOne;
    TextView textViewTimeAlarmTwo;
    TextView textViewTimeAlarmThree;
    TextView textViewTitleAlarmOne;
    TextView textViewTitleAlarmTwo;
    TextView textViewTitleAlarmThree;

    User user;

    AlarmManager alarmManagerOne;
    AlarmManager alarmManagerTwo;
    AlarmManager alarmManagerThree;
    PendingIntent pendingIntentOne;
    PendingIntent pendingIntentTwo;
    PendingIntent pendingIntentThree;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        textViewUserName = view.findViewById(R.id.textView_username);
        textViewUserMail = view.findViewById(R.id.textview_usermail);

        switchOne = view.findViewById(R.id.switch_alarm_one);
        switchTwo = view.findViewById(R.id.switch_alarm_two);
        switchThree = view.findViewById(R.id.switch_alarm_three);

        buttonEditAlarmOne = view.findViewById(R.id.imageview_edit_alarm_one);
        buttonEditAlarmTwo = view.findViewById(R.id.imageview_edit_alarm_two);
        buttonEditAlarmThree = view.findViewById(R.id.imageview_edit_alarm_three);

        textViewTimeAlarmOne = view.findViewById(R.id.textview_alarm_one_time);
        textViewTimeAlarmTwo = view.findViewById(R.id.textview_alarm_two_time);
        textViewTimeAlarmThree = view.findViewById(R.id.textview_alarm_three_time);

        textViewTitleAlarmOne = view.findViewById(R.id.textview_alarm_one_title);
        textViewTitleAlarmTwo = view.findViewById(R.id.textview_alarm_two_title);
        textViewTitleAlarmThree = view.findViewById(R.id.textview_alarm_three_title);

        buttonEditAlarmOne.setOnClickListener(this);
        buttonEditAlarmTwo.setOnClickListener(this);
        buttonEditAlarmThree.setOnClickListener(this);

        switchOne.setOnCheckedChangeListener(this);
        switchTwo.setOnCheckedChangeListener(this);
        switchThree.setOnCheckedChangeListener(this);

        user = ((OrtodentechApplication)getActivity().getApplicationContext()).getUsuario();

        textViewUserName.setText(user.getName());
        textViewUserMail.setText(user.getEmail());


        setupDefaultAlarmValues();

        return view;
    }

    private void setupDefaultAlarmValues() {
        try{
            textViewTitleAlarmOne.setText(SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).getValue(SharedPreferencesUtil.Keys.ALARM_ONE_TITLE));
            textViewTitleAlarmTwo.setText(SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).getValue(SharedPreferencesUtil.Keys.ALARM_TWO_TITLE));
            textViewTitleAlarmThree.setText(SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).getValue(SharedPreferencesUtil.Keys.ALARM_THREE_TITLE));
        }catch (Exception e){
            e.printStackTrace();
        }

        textViewTimeAlarmOne.setText(String.format("%02d:%02d", SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).getIntValue(SharedPreferencesUtil.Keys.ALARM_ONE_HOUR),
                SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).getIntValue(SharedPreferencesUtil.Keys.ALARM_ONE_MINUTE)));

        textViewTimeAlarmTwo.setText(String.format("%02d:%02d", SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).getIntValue(SharedPreferencesUtil.Keys.ALARM_TWO_HOUR),
                SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).getIntValue(SharedPreferencesUtil.Keys.ALARM_TWO_MINUTE)));

        textViewTimeAlarmThree.setText(String.format("%02d:%02d", SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).getIntValue(SharedPreferencesUtil.Keys.ALARM_THREE_HOUR),
                SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).getIntValue(SharedPreferencesUtil.Keys.ALARM_THREE_MINUTE)));


    }


    private void showStatusDialog(final int viewId) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        final View view = getActivity().getLayoutInflater().inflate(R.layout.new_alarm_dialog, null);
        final EditText editTextAlarmTitle = view.findViewById(R.id.edittext_alarm_title);
        final TimePicker timePicker = view.findViewById(R.id.time_picker);

        alertDialogBuilder.setView(view);
        alertDialogBuilder.setTitle("Status envio");
        alertDialogBuilder.setCancelable(false);
        switch(viewId){
            case R.id.imageview_edit_alarm_one:
                alertDialogBuilder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        updateAlarm(viewId, editTextAlarmTitle.getText().toString(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                    }
                });
                break;
            case R.id.imageview_edit_alarm_two:
                alertDialogBuilder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        updateAlarm(viewId, editTextAlarmTitle.getText().toString(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                    }
                });
                break;
            case R.id.imageview_edit_alarm_three:
                alertDialogBuilder.setPositiveButton("Entendido", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        updateAlarm(viewId, editTextAlarmTitle.getText().toString(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                    }
                });
                break;
        }


        alertDialogBuilder.create();
        alertDialogBuilder.show();
    }

    private void updateAlarm(int viewId, String title, int hour, int minute) {
        switch (viewId){
            case R.id.imageview_edit_alarm_one:
                textViewTitleAlarmOne.setText(title);
                textViewTimeAlarmOne.setText(String.format("%02d:%02d", hour, minute));
                SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_ONE_TITLE, title);
                SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_ONE_HOUR, hour);
                SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_ONE_MINUTE, minute);
                break;
            case R.id.imageview_edit_alarm_two:
                textViewTitleAlarmTwo.setText(title);
                textViewTimeAlarmTwo.setText(String.format("%02d:%02d", hour, minute));
                SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_TWO_TITLE, title);
                SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_TWO_HOUR, hour);
                SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_TWO_MINUTE, minute);
                break;
            case R.id.imageview_edit_alarm_three:
                textViewTitleAlarmThree.setText(title);
                textViewTimeAlarmThree.setText(String.format("%02d:%02d", hour, minute));
                SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_THREE_TITLE, title);
                SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_THREE_HOUR, hour);
                SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_THREE_MINUTE, minute);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        showStatusDialog(v.getId());
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        alarmManagerOne = (AlarmManager)getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManagerTwo = (AlarmManager)getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManagerThree = (AlarmManager)getActivity().getApplicationContext().getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(getActivity().getApplicationContext(), AlarmReceiver.class);
        pendingIntentOne = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, 0);
        pendingIntentTwo = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, 0);
        pendingIntentThree = PendingIntent.getBroadcast(getActivity().getApplicationContext(), 0, intent, 0);


        switch (buttonView.getId()){
            case R.id.switch_alarm_one:
                if(isChecked){
                    SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_ONE_STATUS, 1);

                    String hourTimeOne = textViewTimeAlarmOne.getText().toString().substring(0, 2);
                    String minuteTimeOne = textViewTimeAlarmOne.getText().toString().substring(3, 5);

                    Calendar calendar = Calendar.getInstance();
                    calendar.setTimeInMillis(System.currentTimeMillis());
                    calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hourTimeOne));
                    calendar.set(Calendar.MINUTE, Integer.parseInt(minuteTimeOne));

                    alarmManagerOne.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                            AlarmManager.INTERVAL_DAY, pendingIntentOne);
                }
                else{
                    if (alarmManagerOne!= null) {
                        alarmManagerOne.cancel(pendingIntentOne);
                    }

                    SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_ONE_STATUS, 0);
                }
                break;
            case R.id.switch_alarm_two:
                if(isChecked){
                    SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_TWO_STATUS, 1);
                }
                else{
                    SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_TWO_STATUS, 0);
                }
                break;
            case R.id.switch_alarm_three:
                if(isChecked){
                    SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_THREE_STATUS, 1);
                }
                else{
                    SharedPreferencesUtil.getInstance(getActivity().getApplicationContext()).setValue(SharedPreferencesUtil.Keys.ALARM_THREE_STATUS, 0);
                }
                break;
        }
    }
}
