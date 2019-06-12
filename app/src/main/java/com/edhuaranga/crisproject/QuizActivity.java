package com.edhuaranga.crisproject;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.edhuaranga.crisproject.model.Question;
import com.edhuaranga.crisproject.model.Quiz;
import com.edhuaranga.crisproject.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class QuizActivity extends AppCompatActivity implements View.OnClickListener, Response.ErrorListener, Response.Listener<String> {

    Quiz quiz;
    int questionQuizId;
    Question currentQuestion;
    boolean lastQuestion;

    CardView cardViewQuestion;
    TextView textViewStatementQuestion;
    RadioButton radioButtonOption1;
    RadioButton radioButtonOption2;
    RadioButton radioButtonOption3;
    RadioButton radioButtonOption4;
    TextView textViewTimer;
    Button buttonContinue;
    RadioGroup radioGroupOptions;
    //SeekBar seekBarQuizProgress;
    View thumbView;
    User user;
    Dialog feedbackDialog;
    Dialog searchingDialog;

    CardView cardViewResults;
    TextView textViewPoints;
    TextView textViewCorrect;
    TextView textViewBlank;
    TextView textViewIncorrect;
    Button buttonMoreQuestions;
    Button buttonProfile;

    int totalPoints = 0;
    int correctAnswers = 0;
    int blankAnswers = 0;
    int incorretAnswers = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        setupUI();
        initializeObjects();

        startTimer();
        populateQuestion();
    }

    private void initializeObjects() {
        user = ((OrtodentechApplication)getApplicationContext()).getUsuario();
        quiz = (Quiz) getIntent().getSerializableExtra(Constants.EXTRAS_QUIZ);
        questionQuizId = getIntent().getIntExtra("question_id", 0);
        lastQuestion = false;
        //seekBarQuizProgress.setMax(quiz.getQuestions().size());
    }

    private void setupUI(){

        cardViewQuestion = findViewById(R.id.cardView);
        textViewStatementQuestion = findViewById(R.id.textview_question_statement);
        radioGroupOptions = findViewById(R.id.radiogroup_options);
        radioButtonOption1 = findViewById(R.id.radiogroup_option1);
        radioButtonOption2 = findViewById(R.id.radiogroup_option2);
        radioButtonOption3 = findViewById(R.id.radiogroup_option3);
        radioButtonOption4 = findViewById(R.id.radiogroup_option4);
        textViewTimer = findViewById(R.id.textview_question_topic);
        buttonContinue = findViewById(R.id.button_continue);
        //seekBarQuizProgress = findViewById(R.id.seekBar);
        cardViewResults = findViewById(R.id.cardiew_game_result);
        textViewPoints = findViewById(R.id.textView_totalpoints);
        textViewCorrect = findViewById(R.id.textView_correct_questions);
        textViewBlank = findViewById(R.id.textView_blank_questions);
        textViewIncorrect = findViewById(R.id.textView_incorrect_questions);
        buttonMoreQuestions = findViewById(R.id.button_more_questions);
        buttonProfile = findViewById(R.id.button_profile);

        thumbView = LayoutInflater.from(QuizActivity.this).inflate(R.layout.progress_thumb, null, false);

        buttonContinue.setOnClickListener(this);
        /*seekBarQuizProgress.setThumb(getThumb(0));
        seekBarQuizProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                seekBar.setThumb(getThumb(progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });*/

        /*seekBarQuizProgress.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });*/
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_feedback, null);

        alertDialogBuilder.setView(dialogView);
        searchingDialog = alertDialogBuilder.create();

    }

    private void startTimer() {
        new CountDownTimer(30000, 1000) {

            public void onTick(long millisUntilFinished) {
                textViewTimer.setText(millisUntilFinished / 1000+"s remaining");
            }

            public void onFinish() {
                textViewTimer.setText("done!");
                cardViewResults.setVisibility(VISIBLE);
                textViewPoints.setText(totalPoints+"\npoints");
                textViewCorrect.setText(correctAnswers+"\ncorrect");
                textViewBlank.setText(blankAnswers+"\nblank");
                textViewIncorrect.setText(incorretAnswers+"\nincorrect");
            }
        }.start();
    }

    private void populateQuestion() {
        currentQuestion = quiz.getQuestions().get(questionQuizId);

        radioGroupOptions.clearCheck();
        textViewStatementQuestion.setText(currentQuestion.getStatement());
        radioButtonOption1.setText(currentQuestion.getOption1());
        radioButtonOption2.setText(currentQuestion.getOption2());
        radioButtonOption3.setText(currentQuestion.getOption3());
        radioButtonOption4.setText(currentQuestion.getOption4());
        //seekBarQuizProgress.setProgress(questionQuizId+1);
        getSupportActionBar().setTitle(currentQuestion.getTopic());

    }

    public void nextQuestion(){
        if(lastQuestion){
            showPerformance();
        }
        else{
            questionQuizId++;
            populateQuestion();
        }
    }

    private void showPerformance() {
        cardViewQuestion.setVisibility(GONE);
        cardViewResults.setVisibility(VISIBLE);
        textViewPoints.setText(totalPoints+"\npoints");
        textViewCorrect.setText(correctAnswers+"\ncorrect");
        textViewBlank.setText(blankAnswers+"\nblank");
        textViewIncorrect.setText(incorretAnswers+"\nincorrect");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.button_continue:
                searchingDialog.show();
                searchingDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                searchingDialog.getWindow().setLayout(300, 300);
                checkQuestion();
                break;
        }
    }

    private void checkQuestion() {

        int answer = 0;
        switch (radioGroupOptions.getCheckedRadioButtonId()){
            case R.id.radiogroup_option1:
                answer = 1;
                break;
            case R.id.radiogroup_option2:
                answer = 2;
                break;
            case R.id.radiogroup_option3:
                answer = 3;
                break;
            case R.id.radiogroup_option4:
                answer = 4;
                break;
        }

        String url = Constants.BACKEND_URL+"question/"+currentQuestion.getQuestionId()+
                "/check?userid="+ user.getId()+
                "&myanswer="+answer;

        if(questionQuizId == quiz.getQuestions().size() -1) lastQuestion = true;

        RESTManager.getInstance((OrtodentechApplication)getApplicationContext()).
                addStringRequest( Request.Method.GET,
                        Constants.GET_CHECK_ANSWER_QUESTION,
                        url,
                        this,
                        this,
                        null);


    }

    public Drawable getThumb(int progress) {
        ((TextView) thumbView.findViewById(R.id.tvProgress)).setText(progress + "");

        thumbView.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        Bitmap bitmap = Bitmap.createBitmap(thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        thumbView.layout(0, 0, thumbView.getMeasuredWidth(), thumbView.getMeasuredHeight());
        thumbView.draw(canvas);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onResponse(String response) {
        try{
            JSONObject jsonObject = new JSONObject(response);

            String status = jsonObject.getString("status");
            String message = jsonObject.getString("message");

            showStatusAlert(message);


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void showStatusAlert(String message) {
        searchingDialog.dismiss();

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View dialogView = this.getLayoutInflater().inflate(R.layout.dialog_feedback, null);

        alertDialogBuilder.setView(dialogView);
        alertDialogBuilder.setCancelable(false);

        feedbackDialog = alertDialogBuilder.create();
        feedbackDialog.show();
        feedbackDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        feedbackDialog.getWindow().setLayout(300, 300);

        dialogView.findViewById(R.id.textview_message).setVisibility(GONE);
        dialogView.findViewById(R.id.textview_points).setVisibility(VISIBLE);

        if(message.equals(Constants.CORRECT_ANSWER)){
            totalPoints += 3;
            correctAnswers++;
            ((TextView)dialogView.findViewById(R.id.textview_points)).setText("+3\npoints");
        }
        else if(message.equals(Constants.INCORRECT_ANSWER)){
            totalPoints += 0;
            incorretAnswers ++;
            ((TextView)dialogView.findViewById(R.id.textview_points)).setText("0\npoints");
        }
        else if(message.equals(Constants.BLANK_ANSWER)){
            totalPoints += 1;
            blankAnswers ++;
            ((TextView)dialogView.findViewById(R.id.textview_points)).setText("1\npoints");
        }

        new CountDownTimer(1000, 250) {

            public void onTick(long millisUntilFinished) {

            }

            public void onFinish() {
                feedbackDialog.dismiss();
                nextQuestion();
            }
        }.start();


    }
}
