package com.example.myclientapp;

import android.content.Context;

import com.google.gson.Gson;

import java.io.IOException;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import retrofit2.Response;

public class connectUserWorker extends Worker {

    public connectUserWorker(@NonNull Context context, @NonNull WorkerParameters workerParameters){
        super(context, workerParameters);
    }

    @NonNull
    @Override
    public Result doWork() {

        UserServerInterface userServerInterface = ServerHolder.getInstance().serverInterface;
        String user_name  = getInputData().getString("key_user_name");

        try{

            Response<TokenResponse> response = userServerInterface.connectUser(user_name).execute();
            TokenResponse tokenResponse = response.body();
            String JsonToken = new Gson().toJson(tokenResponse);
            Data outputData = new Data.Builder().putString("key_token", JsonToken).build();

            return Result.success(outputData);

        } catch (IOException e) {
            e.printStackTrace();
            return Result.failure();
        }

        //cont--> todo



    }
}
