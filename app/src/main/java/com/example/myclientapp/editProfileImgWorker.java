package com.example.myclientapp;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.ListenableWorker;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Response;

public class editProfileImgWorker extends Worker {

    public editProfileImgWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters){
        super(context, workerParameters);
    }

    @NonNull
    @Override
    public ListenableWorker.Result doWork() {





        UserServerInterface userServerInterface = ServerHolder.getInstance().serverInterface;
        String user_token  = getInputData().getString("key_token");

        String image_url = getInputData().getString("key_image_url");
        setUserProfileImgReq setUserProfileImgReq = new setUserProfileImgReq(image_url);

        try{
            String newToken =  "token" + " " + user_token;

            Response<UserResponse> response = userServerInterface.EditUserProfileImage(newToken,setUserProfileImgReq).execute();
            UserResponse tokenResponse = response.body();
            String JsonToken = new Gson().toJson(tokenResponse);
            Data outputData = new Data.Builder().putString("key_user_info", JsonToken).build();

            return Result.success(outputData);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.failure();
        }







    }
}
