package com.example.myclientapp;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Response;

public class getInfoWorker extends Worker {

    public getInfoWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters){
        super(context, workerParameters);
    }

    @NonNull
    @Override
    public Result doWork() {
        UserServerInterface userServerInterface = ServerHolder.getInstance().serverInterface;
        String user_token  = getInputData().getString("key_token");

        try{
            String newStr = "token" + " " + user_token;

            Response<UserResponse> response = userServerInterface.getUserInfo(newStr).execute();
            UserResponse tokenResponse = response.body();
            String JsonToken = new Gson().toJson( tokenResponse);
            Data outputData = new Data.Builder().putString("key_user_info", JsonToken).build();

            return Result.success(outputData);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.failure();
        }


    }


}
