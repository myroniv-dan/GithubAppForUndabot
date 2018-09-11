package com.example.bogdan.githubapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class RepoDetailsActivity extends AppCompatActivity{

    List<ReposDeatailsData> reposDeatailsDataList = new ArrayList<>();

    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repodetails);

        Intent intent = getIntent();
        int id = intent.getIntExtra("id",0);

        Bundle extras = getIntent().getExtras();
        bmp = extras.getParcelable("imagebitmap");

        Log.v("Received ID", String.valueOf(id));

        new LoadRepoDetails(id,RepoDetailsActivity.this).execute();

    }

    public class LoadRepoDetails extends AsyncTask<Integer,Void,Void>{

        private ProgressDialog dialog;
        private WeakReference<RepoDetailsActivity> repoDetailsActivity;

        int id;

        public LoadRepoDetails(int id, RepoDetailsActivity repoDetailsActivity) {
            this.id = id;
            this.repoDetailsActivity = new WeakReference<>(repoDetailsActivity);
            dialog = new ProgressDialog(repoDetailsActivity);
        }

        @Override
        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(Integer... integers) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/repositories/" + id)
                    .build();

            try {

                Response response = client.newCall(request).execute();

                JSONObject jsonObject = new JSONObject(response.body().string());

                Log.v("Response Object", String.valueOf(jsonObject));

                ReposDeatailsData reposDeatailsData = new ReposDeatailsData(
                        jsonObject.getString("name"),
                        jsonObject.getJSONObject("owner").getString("login"),
                        jsonObject.getJSONObject("owner").getString("avatar_url"),
                        jsonObject.getString("html_url"),
                        jsonObject.getString("description"),
                        jsonObject.getString("created_at"),
                        jsonObject.getString("updated_at"),
                        jsonObject.getString("language"),
                        jsonObject.getInt("watchers_count"),
                        jsonObject.getInt("forks_count"),
                        jsonObject.getInt("open_issues_count"));

                reposDeatailsDataList.add(reposDeatailsData);

            }catch (IOException | JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if(repoDetailsActivity.get() != null){

                setupUI();

                if (dialog.isShowing()) {
                    dialog.dismiss();
                }

            }

        }

    }

    public void setupUI(){

        TextView tvRepoName, tvAuthorName, tvRepoWatches, tvRepoIssues, tvRepoForks,
                tvRepoDescription, tvRepoLanguage, tvRepoCrateDate, tvRepoUpdateDate;

        ImageView imageViewRepo = findViewById(R.id.imageViewD);

        Button buttonGoWebVersion;

        tvRepoName = findViewById(R.id.tvRepoNameD);
        tvAuthorName = findViewById(R.id.tvAuthorNameD);
        tvRepoWatches = findViewById(R.id.tvRepoWatches);
        tvRepoForks = findViewById(R.id.tvRepoForks);
        tvRepoIssues = findViewById(R.id.tvRepoissues);
        tvRepoDescription = findViewById(R.id.tvRepoDescription);
        tvRepoLanguage = findViewById(R.id.tvRepoLanguage);
        tvRepoCrateDate = findViewById(R.id.tvDateCreated);
        tvRepoUpdateDate = findViewById(R.id.tvDateUpdated);

        imageViewRepo.setImageBitmap(bmp);

        buttonGoWebVersion = findViewById(R.id.button);

        tvRepoName.setText(reposDeatailsDataList.get(0).getRepoName());
        tvAuthorName.setText(reposDeatailsDataList.get(0).getRepoAuthor());
        tvRepoWatches.setText(String.valueOf(reposDeatailsDataList.get(0).getRepoWatches()));
        tvRepoForks.setText(String.valueOf(reposDeatailsDataList.get(0).getRepoForks()));
        tvRepoIssues.setText(String.valueOf(reposDeatailsDataList.get(0).getRepoIssues()));
        tvRepoDescription.setText(reposDeatailsDataList.get(0).getRepoDescription());
        tvRepoLanguage.setText(reposDeatailsDataList.get(0).getRepoLanguage());
        tvRepoCrateDate.setText(reposDeatailsDataList.get(0).getRepoCreatedDate().substring(0,10));
        tvRepoUpdateDate.setText(reposDeatailsDataList.get(0).getRepoUpdatedDate().substring(0,10));

        Picasso.get().load(reposDeatailsDataList.get(0).getRepoImgLink()).into(imageViewRepo);

        imageViewRepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), UserDetailsActivity.class);
                intent.putExtra("login", reposDeatailsDataList.get(0).getRepoAuthor());

                Bundle extras = new Bundle();
                extras.putParcelable("imagebitmap", bmp);
                intent.putExtras(extras);

                getApplication().startActivity(intent);

            }
        });

        buttonGoWebVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(reposDeatailsDataList.get(0).getRepoOutLink()));
                startActivity(intent);
            }
        });

    }

}
