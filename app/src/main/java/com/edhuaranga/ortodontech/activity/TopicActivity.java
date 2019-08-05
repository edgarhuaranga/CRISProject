package com.edhuaranga.ortodontech.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.edhuaranga.ortodontech.Constants;
import com.edhuaranga.ortodontech.PastAnswersFragment;
import com.edhuaranga.ortodontech.R;
import com.edhuaranga.ortodontech.RESTManager;
import com.edhuaranga.ortodontech.RankingFragment;
import com.edhuaranga.ortodontech.model.Quiz;
import com.edhuaranga.ortodontech.model.User;
import com.edhuaranga.ortodontech.utils.SharedPreferencesUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class TopicActivity extends AppCompatActivity implements Response.Listener<String>, Response.ErrorListener, View.OnClickListener {

    Quiz quiz;
    User user;
    private SectionsPagerAdapter mSectionsPagerAdapter;
    TextView textViewProgress;
    ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        quiz = (Quiz) getIntent().getSerializableExtra(Constants.EXTRAS_QUIZ);
        user = SharedPreferencesUtil.getInstance(this).getCurrentUser();
        toolbar.setTitle(quiz.getTopic());
        setSupportActionBar(toolbar);

        findViewById(R.id.button).setOnClickListener(this);
        ImageView fab = findViewById(R.id.imageview_topic_icon);
        ImageView background = findViewById(R.id.imageview_background);
        textViewProgress = findViewById(R.id.textview_percent);
        progressBar = findViewById(R.id.progressBar);

        Glide.with(this)
                .load(Uri.parse("http://ortodontech.ehuaranga.com:8000/uploads/icons/"+quiz.getIconURL()+".png"))
                .apply(new RequestOptions().circleCrop())
                .into(fab);

        Glide.with(this)
                .load(R.drawable.background_topic)
                .apply(new RequestOptions().centerCrop())
                .into(background);

        requestTopicResume();
    }

    private void requestTopicResume() {
        String url = Constants.BACKEND_URL+"resume/topic?";
        url = url + "&quiz_id="+quiz.getId();
        url = url + "&user_id="+user.getId();

        RESTManager.getInstance(this.getApplication()).
                addStringRequest(Request.Method.GET,
                        Constants.GET_TOPIC_RESUME,
                        url,
                        this,
                        this,
                        null);

    }

    @Override
    public void onResponse(String response) {
        ArrayList<Fragment> paginas = new ArrayList<>();
        try{
            JSONObject jsonObject = new JSONObject(response);

            if(jsonObject.getString("requestType").equalsIgnoreCase(Constants.GET_TOPIC_RESUME)){
                try {

                    JSONArray jsonRanking = jsonObject.getJSONArray("ranking");
                    JSONArray jsonRespuestas = jsonObject.getJSONArray("answers");
                    float progress = (float) jsonObject.getDouble("progress");

                    textViewProgress.setText(String.format("%.1f%%", progress));
                    progressBar.setProgress((int) progress);

                    paginas.add(RankingFragment.newInstance(jsonRanking));
                    paginas.add(PastAnswersFragment.newInstance(jsonRespuestas));

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(), paginas);
            ViewPager mViewPager = findViewById(R.id.pager);
            mViewPager.setAdapter(mSectionsPagerAdapter);

            TabLayout tabLayout = findViewById(R.id.tabDots);
            tabLayout.setupWithViewPager(mViewPager);

            mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
            tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));


        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {

    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, QuizActivity.class);
        intent.putExtra(Constants.EXTRAS_QUIZ, quiz);
        startActivity(intent);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public final String[] titles = new String[]{"Mis respuestas", "Ranking"};
        ArrayList<Fragment> paginas;
        SectionsPagerAdapter(FragmentManager fm, ArrayList<Fragment> paginas) {
            super(fm);
            this.paginas = paginas;
        }

        @Override
        public Fragment getItem(int position) {
            return paginas.get(position);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }
    }
}
