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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserDetailsActivity extends AppCompatActivity {

    List<UserDeatailsData> userDeatailsDataList = new ArrayList<>();

    Bitmap bmp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userdetails);

        Intent intent = getIntent();
        String login = intent.getStringExtra("login");

        Bundle extras = getIntent().getExtras();
        bmp = extras.getParcelable("imagebitmap");

        Log.v("Received ID", login);

        new loadUserDetails(login,UserDetailsActivity.this).execute();
    }

    public class loadUserDetails extends AsyncTask<String,Void,Void>{

        private ProgressDialog dialog;

        String login;

        private loadUserDetails(String login, UserDetailsActivity userDetailsActivity) {
            this.login = login;
            dialog = new ProgressDialog(userDetailsActivity);
        }

        protected void onPreExecute() {
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected Void doInBackground(String... strings) {

            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.github.com/users/" + login)
                    .build();

            try {

                Response response = client.newCall(request).execute();

                JSONObject jsonObject = new JSONObject(response.body().string());

                Log.v("Response Object", String.valueOf(jsonObject));

                UserDeatailsData userDeatailsData = new UserDeatailsData(
                        jsonObject.getString("html_url"),
                        jsonObject.getString("name"),
                        jsonObject.getString("company"),
                        jsonObject.getString("location"),
                        jsonObject.getString("bio"),
                        jsonObject.getInt("public_repos"),
                        jsonObject.getInt("followers"));

                userDeatailsDataList.add(userDeatailsData);


            }catch (IOException | JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            setupUI();

            if (dialog.isShowing()) {
                dialog.dismiss();
            }

        }
    }

    private void setupUI() {

        ImageView imageViewUser = findViewById(R.id.imageViewUser);
        Button button = findViewById(R.id.buttonUser);

        TextView tvUserName, tvUserCompany, tvUserLocation, tvUserBio, tvRepoNum, tvFollowNum;

        tvUserName = findViewById(R.id.tvName);
        tvUserCompany = findViewById(R.id.tvCompany);
        tvUserLocation = findViewById(R.id.tvLocation);
        tvUserBio = findViewById(R.id.tvBio);
        tvRepoNum = findViewById(R.id.tvRepoNums);
        tvFollowNum = findViewById(R.id.tvFollowsNum);

        imageViewUser.setImageBitmap(bmp);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(userDeatailsDataList.get(0).getUserOutURL()));
                startActivity(intent);
            }
        });

        tvUserName.setText(userDeatailsDataList.get(0).getUserName());

        tvUserCompany.setText(userDeatailsDataList.get(0).getUserCompany());
        tvUserLocation.setText(userDeatailsDataList.get(0).getUserLocation());
        tvUserBio.setText(userDeatailsDataList.get(0).getUserBio());
        tvFollowNum.setText(String.valueOf(userDeatailsDataList.get(0).getUserFollowersNUmber()));
        tvRepoNum.setText(String.valueOf(userDeatailsDataList.get(0).getUserPublicReposNumber()));

    }
}
