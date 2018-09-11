package com.example.bogdan.githubapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.arlib.floatingsearchview.FloatingSearchView;
import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

// Main Activity

public class SearchActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private FloatingSearchView searchView;

    private ReposAdapter adapter;
    private List<ReposData> reposList = new ArrayList<>();

    private TextView tvNoResults;

    private String query = "";
    private String currentSort = "stars";

    int page = 1; // start from 1st page

    boolean isMore = true;

    int pastVisibleItems, visibleItems, totalItems;

    // this variable controls when to trigger query for next page,
    // indicates number of non visible items for user before end of the list
    int trigger = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        recyclerView = findViewById(R.id.recyclerView);
        searchView = findViewById(R.id.searchView);
        tvNoResults = findViewById(R.id.tvNoResults);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));

        adapter = new ReposAdapter(this, reposList);
        recyclerView.setAdapter(adapter);

        searchView.setOnSearchListener(new FloatingSearchView.OnSearchListener() {

            @Override
            public void onSuggestionClicked(SearchSuggestion searchSuggestion) { }

            @Override
            public void onSearchAction(String currentQuery) {

                Log.v("Current Query", currentQuery);

                if(currentQuery.equals("")){
                    Toast.makeText(getApplicationContext(), "Enter keyword to search bud", Toast.LENGTH_LONG).show();
                }else{

                    query = currentQuery;
                    new SearchForRepos(currentQuery,SearchActivity.this).execute();

                }

            }
        });

        // sorting implemented in menus
        searchView.setOnMenuItemClickListener(new FloatingSearchView.OnMenuItemClickListener() {
            @Override
            public void onActionMenuItemSelected(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.stars:

                        if(currentSort.equals("stars")){
                            Toast.makeText(getApplicationContext(), "Sorted by \"Stars\" now", Toast.LENGTH_LONG).show();
                        }else {
                            new SearchForRepos(query,SearchActivity.this).execute();
                            currentSort = "stars";
                        }

                        break;

                    case R.id.forks:

                        if(currentSort.equals("forks")){
                            Toast.makeText(getApplicationContext(), "Sorted by \"Forks\" now", Toast.LENGTH_LONG).show();
                        }else {
                            new SearchForRepos(query,SearchActivity.this).execute();
                            currentSort = "forks";
                        }

                        break;

                    case R.id.updated:

                        if(currentSort.equals("updated")){
                            Toast.makeText(getApplicationContext(), "Sorted by \"Updated\" now", Toast.LENGTH_LONG).show();
                        }else {
                            new SearchForRepos(query,SearchActivity.this).execute();
                            currentSort = "updated";
                        }

                        break;
                }

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

                // if we scroll down
                if(dy > 0) {

                    // check when owe are close to the end of the list, when we are do another query
                    visibleItems = layoutManager.getChildCount();
                    totalItems = layoutManager.getItemCount();
                    pastVisibleItems = layoutManager.findFirstVisibleItemPosition();

                    // if there's more data
                    if (isMore) {

                        // load more data
                        if ( (visibleItems + pastVisibleItems) >= totalItems - trigger) {

                            isMore = false;
                            page++;
                            new LoadMoreRepos().execute();

                        }
                    }
                }
            }
        });

    }

    private class SearchForRepos extends AsyncTask<String,Void,Void>{

        private ProgressDialog dialog;

        private WeakReference<SearchActivity> searchActivity;

        String query;

        private SearchForRepos(String query, SearchActivity searchActivity) {
            this.query = query;

            this.searchActivity = new WeakReference<>(searchActivity);

            dialog = new ProgressDialog(searchActivity);
        }

        protected void onPreExecute() {
            dialog.setMessage("Searching...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/search/repositories?q="+query+"&sort="+currentSort+"&order=desc&page=" + page)
                    .build();

            try{

                Response response = client.newCall(request).execute();

                JSONObject jsonObject = new JSONObject(response.body().string());

                Log.v("Response Object", String.valueOf(jsonObject));

                JSONArray jsonArray = (jsonObject.getJSONArray("items"));

                Log.v("Response Array", String.valueOf(jsonArray));

                // check if there's data in response,
                // if yes, then imMore = true, we can do another query once user scrolls close to the end of the list,
                // if no, then isMore=false and no more queries
                if(jsonArray.length() != 0){
                    isMore = true;
                }else isMore = false;

                if(jsonArray.length() == 0){

                    reposList.clear();
                    showNoResults();

                }else {

                    if(tvNoResults.getVisibility() == View.VISIBLE) {
                        hideNoResults();
                    }

                    reposList.clear();

                    for(int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObjectItem = jsonArray.getJSONObject(i);

                        ReposData reposData = new ReposData(
                                jsonObjectItem.getString("name"),
                                jsonObjectItem.getJSONObject("owner").getString("login"),
                                jsonObjectItem.getJSONObject("owner").getString("avatar_url"),
                                jsonObjectItem.getInt("watchers_count"),
                                jsonObjectItem.getInt("forks_count"),
                                jsonObjectItem.getInt("open_issues_count"),
                                jsonObjectItem.getInt("id"));

                        reposList.add(reposData);
                    }

                }

            }catch (IOException | JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(searchActivity.get() != null){

                recyclerView.smoothScrollToPosition(0);

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

                adapter.notifyDataSetChanged();

            }

        }
    }

    // load more data
    private class LoadMoreRepos extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/search/repositories?q="+query+"&sort="+currentSort+"&order=desc&page=" + page)
                    .build();

            try{

                Response response = client.newCall(request).execute();

                JSONObject jsonObject = new JSONObject(response.body().string());

                Log.v("Response Object", String.valueOf(jsonObject));

                JSONArray jsonArray = (jsonObject.getJSONArray("items"));

                Log.v("Response Array", String.valueOf(jsonArray));

                // check if there's data in response,
                // if yes, then imMore = true, we can do another query once user scrolls close to the end of the list,
                // if no, then isMore=false and no more queries
                if(jsonArray.length() != 0){

                    isMore = true;

                    for(int i = 0; i < jsonArray.length(); i++) {

                        JSONObject jsonObjectItem = jsonArray.getJSONObject(i);

                        ReposData reposData = new ReposData(
                                jsonObjectItem.getString("name"),
                                jsonObjectItem.getJSONObject("owner").getString("login"),
                                jsonObjectItem.getJSONObject("owner").getString("avatar_url"),
                                jsonObjectItem.getInt("watchers_count"),
                                jsonObjectItem.getInt("forks_count"),
                                jsonObjectItem.getInt("open_issues_count"),
                                jsonObjectItem.getInt("id"));

                        reposList.add(reposData);
                    }

                }else{

                    isMore = false;
                }

            }catch (IOException | JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            adapter.notifyDataSetChanged();

        }
    }

    // just show text view if no results
    public void showNoResults(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvNoResults.setVisibility(View.VISIBLE);
            }
        });

    }

    public void hideNoResults(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tvNoResults.setVisibility(View.INVISIBLE);
            }
        });

    }

}
