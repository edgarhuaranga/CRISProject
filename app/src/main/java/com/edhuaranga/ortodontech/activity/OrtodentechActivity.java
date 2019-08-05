package com.edhuaranga.ortodontech.activity;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.edhuaranga.ortodontech.Constants;
import com.edhuaranga.ortodontech.R;
import com.edhuaranga.ortodontech.RESTManager;
import com.edhuaranga.ortodontech.adapters.AnswerAdapter;
import com.edhuaranga.ortodontech.adapters.CategoryAdapter;
import com.edhuaranga.ortodontech.adapters.NewAdapter;
import com.edhuaranga.ortodontech.adapters.QuizAdapter;
import com.edhuaranga.ortodontech.model.Category;
import com.edhuaranga.ortodontech.model.New;
import com.edhuaranga.ortodontech.model.Quiz;
import com.edhuaranga.ortodontech.model.User;
import com.edhuaranga.ortodontech.utils.ObjectBuilderUtil;
import com.edhuaranga.ortodontech.utils.SharedPreferencesUtil;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

public class OrtodentechActivity extends AppCompatActivity implements Response.ErrorListener, Response.Listener<String>, OnChartValueSelectedListener {

    QuizAdapter quizAdapter;
    CategoryAdapter categoryAdapter;
    ArrayList<Quiz> quizes;
    ArrayList<Category> serverCategories;
    RecyclerView recyclerViewCategories;
    User user;

    ScrollView layoutStats;
    private BarChart chart;
    private PieChart chartTopics;
    RecyclerView recyclerViewPastAnswers;
    AnswerAdapter answerAdapter;
    ArrayList<JSONObject> jsonArrayAnswers;

    NewAdapter newsAdapter;
    RecyclerView recyclerViewNews;
    ArrayList<New> posts;

    ConstraintLayout layoutProfile;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    Log.d("Mainactivity", "Show questions");
                    recyclerViewCategories.setVisibility(View.VISIBLE);
                    layoutStats.setVisibility(View.GONE);
                    recyclerViewNews.setVisibility(View.GONE);
                    layoutProfile.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    Log.d("Mainactivity", "Show posts");
                    recyclerViewCategories.setVisibility(View.GONE);
                    layoutStats.setVisibility(View.GONE);
                    recyclerViewNews.setVisibility(View.VISIBLE);
                    layoutProfile.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_stats:
                    Log.d("Mainactivity", "Show Ranking");
                    recyclerViewCategories.setVisibility(View.GONE);
                    layoutStats.setVisibility(View.VISIBLE);
                    recyclerViewNews.setVisibility(View.GONE);
                    layoutProfile.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_profile:
                    Log.d("Mainactivity", "Show profile");
                    recyclerViewCategories.setVisibility(View.GONE);
                    layoutStats.setVisibility(View.GONE);
                    recyclerViewNews.setVisibility(View.GONE);
                    layoutProfile.setVisibility(View.VISIBLE);
                    return true;
            }
            return true;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ortodentech);

        getSupportActionBar().hide();

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        user = SharedPreferencesUtil.getInstance(getApplicationContext()).getCurrentUser();

        recyclerViewCategories = findViewById(R.id.recycler_questions);
        recyclerViewPastAnswers = findViewById(R.id.recycler_past_answers);
        recyclerViewNews = findViewById(R.id.recycler_news);
        layoutProfile = findViewById(R.id.layout_profile);
        layoutStats = findViewById(R.id.layout_stats);

        ((TextView) findViewById(R.id.textview_profile_username)).setText(user.getName());
        ((TextView) findViewById(R.id.textview_username)).setText(user.getName());

        chart = findViewById(R.id.historical_chart);
        chartTopics = findViewById(R.id.toptopics_chart);

        serverCategories = new ArrayList<>();

        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(this));
        categoryAdapter = new CategoryAdapter(this, serverCategories);
        recyclerViewCategories.setAdapter(categoryAdapter);

        recyclerViewPastAnswers.setLayoutManager(new LinearLayoutManager(this));
        jsonArrayAnswers = new ArrayList<>();
        answerAdapter = new AnswerAdapter(this, jsonArrayAnswers);
        recyclerViewPastAnswers.setAdapter(answerAdapter);

        /*recyclerViewNews.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        recyclerViewNews.setLayoutManager(new LinearLayoutManager(this));
        posts = new ArrayList<>();
        newsAdapter = new NewAdapter(this, posts);
        recyclerViewNews.setAdapter(newsAdapter);*/


        requestCategories();
        requestStats();
        //requestAnswers();
        requestPosts();
        requestProfile();
    }

    private void requestStats() {
        //String url = Constants.BACKEND_URL+user.getId()+"/stats?";
        String url = Constants.BACKEND_URL+"2/stats?";
        Log.d("OrtodentechActivity", url);
        RESTManager.getInstance(this.getApplication()).
                addStringRequest( Request.Method.GET,
                        Constants.GET_USER_STATS,
                        url,
                        this,
                        this,
                        null);
    }

    private void requestCategories(){
        String url = Constants.BACKEND_URL+"quizzes?";

        RESTManager.getInstance(this.getApplication()).
                addStringRequest( Request.Method.GET,
                        Constants.GET_QUIZZES_GROUPEDBY_CATEGORY,
                        url,
                        this,
                        this,
                        null);
    }

    private void requestProfile() {
        String url = Constants.BACKEND_URL+user.getId()+"/profile?";

        RESTManager.getInstance(this.getApplication()).
                addStringRequest( Request.Method.GET,
                        Constants.GET_USER_PROFILE,
                        url,
                        this,
                        this,
                        null);
    }

    private void requestPosts() {
        String url = Constants.BACKEND_URL+"posts?";

        RESTManager.getInstance(this.getApplication()).
                addStringRequest( Request.Method.GET,
                        Constants.GET_POSTS,
                        url,
                        this,
                        this,
                        null);
    }

    private void updateRecycler(ArrayList<Quiz> serverQuizes) {
        quizes.clear();
        Log.d("HomeFragment", serverQuizes.size()+" added");
        quizes.addAll(serverQuizes);
        quizAdapter.notifyDataSetChanged();
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Log.d("CheckActivity", error.toString());
    }

    @Override
    public void onResponse(String response) {
        try{
            JSONObject jsonObject = new JSONObject(response);

            if(jsonObject.getString("requestType").equalsIgnoreCase(Constants.GET_QUIZZES_GROUPEDBY_CATEGORY)){
                handleCategoriesResponse(jsonObject);
            }

            if(jsonObject.getString("requestType").equalsIgnoreCase(Constants.GET_USER_PROFILE)){
                handleProfileResponse(jsonObject);
            }

            if(jsonObject.getString("requestType").equalsIgnoreCase(Constants.GET_USER_STATS)){
                handleStats(jsonObject);
            }

            if(jsonObject.getString("requestType").equalsIgnoreCase(Constants.GET_POSTS)){
                handlePostsResponse(jsonObject);
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleStats(JSONObject jsonObject) {
        Log.d("OrtodentechActivity", jsonObject.toString());

        chart.setOnChartValueSelectedListener(this);

        chart.getDescription().setEnabled(false);

        chart.setMaxVisibleValueCount(40);

        chart.setPinchZoom(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);
        chart.setDrawValueAboveBar(false);
        chart.setHighlightFullBarEnabled(false);


        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setAxisMinimum(0f);
        chart.getAxisRight().setEnabled(false);

        XAxis xLabels = chart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.BOTTOM);

        ArrayList<String> labelsX = new ArrayList<>();
        try {
            JSONArray jsonArrayHistoric = jsonObject.getJSONArray("history");
            ArrayList<BarEntry> values = new ArrayList<>();

            for (int i = 0; i < jsonArrayHistoric.length(); i++) {
                float val1 = (float) jsonArrayHistoric.getJSONObject(i).getInt("correctas");
                float val2 = (float) jsonArrayHistoric.getJSONObject(i).getInt("incorrectas");
                float val3 = (float) jsonArrayHistoric.getJSONObject(i).getInt("blancas");
                labelsX.add(jsonArrayHistoric.getJSONObject(i).getString("fecha"));

                values.add(new BarEntry(i, new float[]{val1, val2, val3}, getResources().getDrawable(R.drawable.ic_tooth)));
            }

            chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labelsX));
            chart.getXAxis().setLabelRotationAngle(-45);
            chart.getXAxis().setTextColor(Color.BLACK);
            chart.getXAxis().setXOffset(10f);
            BarDataSet set1;

            if (chart.getData() != null &&
                    chart.getData().getDataSetCount() > 0) {
                set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
                set1.setValues(values);
                chart.getData().notifyDataChanged();
                chart.notifyDataSetChanged();
            } else {
                set1 = new BarDataSet(values, "");
                set1.setDrawIcons(false);
                set1.setColors(new int[]{Color.GREEN, Color.RED, Color.DKGRAY});
                set1.setStackLabels(new String[]{"Correctas", "Incorrectas", "En blanco"});

                ArrayList<IBarDataSet> dataSets = new ArrayList<>();
                dataSets.add(set1);

                BarData data = new BarData(dataSets);
                data.setValueFormatter(new StackedValueFormatter(false, "", 1));
                data.setValueTextColor(Color.WHITE);

                chart.setData(data);
            }

            chart.setFitBars(true);
            chart.invalidate();
            chart.setVisibleXRangeMaximum(7); // allow 20 values to be displayed at once on the x-axis, not more
            chart.moveViewToX(chart.getData().getEntryCount());
            chart.setBackgroundColor(Color.argb(0, 47, 47, 47));


        } catch (JSONException e) {
            e.printStackTrace();
        }


        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(true);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXOffset(30f);
        l.setXEntrySpace(6f);


        chartTopics.setUsePercentValues(true);
        chartTopics.getDescription().setEnabled(false);
        chartTopics.setExtraOffsets(5, 5, 5, 5);


        chartTopics.setDragDecelerationFrictionCoef(0.95f);

        chartTopics.setDrawHoleEnabled(true);
        chartTopics.setHoleColor(Color.WHITE);

        chartTopics.setTransparentCircleColor(Color.WHITE);
        chartTopics.setTransparentCircleAlpha(110);

        chartTopics.setHoleRadius(38f);
        chartTopics.setTransparentCircleRadius(41f);

        chartTopics.setDrawCenterText(false);

        chartTopics.setRotationAngle(0);
        // enable rotation of the chartTopics by touch
        chartTopics.setRotationEnabled(true);
        chartTopics.setHighlightPerTapEnabled(true);

        // chartTopics.setUnit(" €");
        // chartTopics.setDrawUnitsInchartTopics(true);

        // add a selection listener
        chartTopics.setOnChartValueSelectedListener(this);

        ///////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////
        try {
            JSONArray jsonArrayHistoric = jsonObject.getJSONArray("topTopics");
            ArrayList<PieEntry> entries = new ArrayList<>();

            for (int i = 0; i < jsonArrayHistoric.length(); i++) {
                float val1 = (float) jsonArrayHistoric.getJSONObject(i).getInt("avance");
                String label = jsonArrayHistoric.getJSONObject(i).getString("topic");

                entries.add(new PieEntry(val1, label,
                        getResources().getDrawable(R.drawable.ic_tooth)));
            }

            ((TextView)findViewById(R.id.textview_puntos)).setText(String.format("%d", jsonObject.getInt("puntos")));
            ((TextView)findViewById(R.id.textview_correctas)).setText(String.format("%d", jsonObject.getInt("q_correctos")));

            PieDataSet dataSet = new PieDataSet(entries, "");
            dataSet.setDrawIcons(false);

            dataSet.setSliceSpace(3f);
            dataSet.setSelectionShift(5f);
            dataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);


            ArrayList<Integer> colors = new ArrayList<>();

            for (int c : ColorTemplate.COLORFUL_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.LIBERTY_COLORS)
                colors.add(c);

            for (int c : ColorTemplate.PASTEL_COLORS)
                colors.add(c);

            colors.add(ColorTemplate.getHoloBlue());

            dataSet.setColors(colors);
            //dataSet.setSelectionShift(0f);

            PieData data = new PieData(dataSet);
            data.setValueFormatter(new PercentFormatter(chartTopics));
            data.setValueTextSize(11f);
            data.setValueTextColor(Color.BLACK);
            chartTopics.setData(data);

            // undo all highlights
            chart.highlightValues(null);

            chart.invalidate();



        } catch (JSONException e) {
            e.printStackTrace();
        }

        ///////////////////////////////////////////////////////////////////////////////////////////
        ///////////////////////////////////////////////////////////////////////////////////////////

        chartTopics.animateY(1400, Easing.EaseInOutQuad);
        //chartTopics.setBackgroundColor(Color.BLACK);
        // chartTopics.spin(2000, 0, 360);

        Legend l1 = chartTopics.getLegend();
        l1.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l1.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l1.setOrientation(Legend.LegendOrientation.VERTICAL);
        l1.setDrawInside(false);
        l1.setXEntrySpace(0f);
        l1.setYEntrySpace(0f);
        l1.setYOffset(0f);

        // entry label styling
        chartTopics.setEntryLabelColor(Color.WHITE);
        chartTopics.setEntryLabelTextSize(12f);



    }

    private void handlePostsResponse(JSONObject jsonObject) {
        ArrayList<New> serverPosts= new ArrayList<>();
        try{
            JSONArray jsonArrayPosts = jsonObject.getJSONArray("data");

            for(int i=0; i<jsonArrayPosts.length(); i++){
                JSONObject jsonObjectPost = jsonArrayPosts.getJSONObject(i);
                New post = new New();
                post.setTitle(jsonObjectPost.getString("title"));
                post.setContent(jsonObjectPost.getString("content"));
                post.setPhotoUrl(jsonObjectPost.getString("url_photo"));
                post.setDatePosted(jsonObjectPost.getJSONObject("updated_at").getString("date"));

                serverPosts.add(post);
            }
            posts.clear();
            posts.addAll(serverPosts);
            newsAdapter.notifyDataSetChanged();
        }catch (Exception e){

        }
    }

    private void handleProfileResponse(JSONObject jsonObject) {
        try{
            ArrayList<JSONObject> jsonObjects = new ArrayList<>();
            JSONArray jsonArrayPastAnswers = jsonObject.getJSONArray("answers");

            for(int i=0; i<jsonArrayPastAnswers.length(); i++)
                jsonObjects.add(jsonArrayPastAnswers.getJSONObject(i));
            //updateRecycler(jsonObjects);
            jsonArrayAnswers.clear();
            jsonArrayAnswers.addAll(jsonObjects);
            answerAdapter.notifyDataSetChanged();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    private void handleCategoriesResponse(JSONObject jsonObjectCategories){
        try {
            ArrayList<Category> categories = new ArrayList<>();
            JSONArray jsonCategories = jsonObjectCategories.getJSONArray("data");

            for(int i=0; i<jsonCategories.length(); i++){
                JSONObject jsonObject = jsonCategories.getJSONObject(i);
                Log.d("JSONQuiz", jsonObject.toString());
                Category category = ObjectBuilderUtil.buildCategoryFromJSON(jsonObject);
                Log.d("Category", category.toString());
                categories.add(category);
            }

            updateRecyclerCategories(categories);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void updateRecyclerCategories(ArrayList<Category> categories) {
        serverCategories.clear();
        serverCategories.addAll(categories);
        categoryAdapter.notifyDataSetChanged();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Entry selected", e.toString());

        chart.centerViewToAnimated(e.getX(), e.getY(), chart.getData().getDataSetByIndex(h.getDataSetIndex())
                .getAxisDependency(), 500);
    }

    @Override
    public void onNothingSelected() {

    }
}
