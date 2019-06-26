package com.example.myclientapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.gson.Gson;

import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class profileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //get all Extras -->

        final String user_token = getIntent().getExtras().get("key_token").toString();
        String key_user_info = getIntent().getExtras().get("key_user_info").toString();

        UserResponse user_info = new Gson().fromJson(key_user_info, UserResponse.class);


        final EditText editText = findViewById(R.id.editText2);

        editText.setText(user_info.data.pretty_name);

        String url = user_info.data.image_url;

        String image = url.substring(url.lastIndexOf('/')+1, url.lastIndexOf('.'));

        // set profile image

        ImageView imageView = findViewById(R.id.imageView);

        Picasso.get().load("http://hujipostpc2019.pythonanywhere.com" + url).into(imageView);

        //now edit pretty_name and update user_profile if necessary-->

        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editUserPrettyName(user_token,editText.getText().toString());
            }
        });


    }


    public void editUserPrettyName(String user_token, String pretty_name_to_update)
    {
        HashMap hashMap = new HashMap();
        hashMap.put("key_token", user_token);
        hashMap.put("key_pretty_name", pretty_name_to_update);
        final ProgressBar simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        simpleProgressBar.setVisibility(View.VISIBLE);

        UUID workTagUniqueId = UUID.randomUUID();
        OneTimeWorkRequest editUserWorker = new OneTimeWorkRequest.Builder(editUserWorker.class)
                .setConstraints(new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build())
                .setInputData(new Data.Builder().putAll(hashMap).build())
                .addTag(workTagUniqueId.toString())
                .build();

        WorkManager.getInstance().enqueue(editUserWorker);

        WorkManager.getInstance().getWorkInfosByTagLiveData(workTagUniqueId.toString()).observe(this, new Observer<List<WorkInfo>>() {
            @Override
            public void onChanged(List<WorkInfo> workInfos) {
                // we know there will be only 1 work info in this list - the 1 work with that specific tag!
                // there might be some time until this worker is finished to work (in the mean team we will get an empty list
                // so check for that
                if (workInfos == null || workInfos.isEmpty())
                    return;

                WorkInfo info = workInfos.get(0);

                // now we can use it
                String userAsJson = info.getOutputData().getString("key_user_info");

                UserResponse user_token = new Gson().fromJson(userAsJson, UserResponse.class);

                simpleProgressBar.setVisibility(View.INVISIBLE);

                // update UI with the user we got

                if (user_token != null)
                {

                    final EditText editText = findViewById(R.id.editText2);

                    editText.setText(user_token.data.pretty_name);


                    String url = user_token.data.image_url;

                    String image = url.substring(url.lastIndexOf('/')+1, url.lastIndexOf('.'));

                    ImageView imageView = findViewById(R.id.imageView);

                    Picasso.get().load("http://hujipostpc2019.pythonanywhere.com" + url).into(imageView);
                }
            }
        });
    }
}
