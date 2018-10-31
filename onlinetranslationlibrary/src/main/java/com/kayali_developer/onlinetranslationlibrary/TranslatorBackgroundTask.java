package com.kayali_developer.onlinetranslationlibrary;

import android.content.Context;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.kayali_developer.onlinetranslationlibrary.data.model.TranslationResponse;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TranslatorBackgroundTask extends AsyncTask<String, Void, String> {
    //Declare Context
    Context ctx;
    //Set Context
    TranslatorBackgroundTask(Context ctx){
        this.ctx = ctx;
    }

    @Override
    protected String doInBackground(String... params) {
        //String variables
        String textToBeTranslated = params[0];
        String languagePair = params[1];

        String jsonLine;

        try {
            //Set up the translation call URL
            String yandexKey = "trnsl.1.1.20181017T082856Z.4ae3d175fb83be2f.498a4eea95da56b0e790361ee36d710414795082";
            String yandexUrl = "https://translate.yandex.net/api/v1.5/tr.json/translate?key=" + yandexKey
                    + "&text=" + textToBeTranslated + "&lang=" + languagePair;
            URL yandexTranslateURL = new URL(yandexUrl);

            //Set Http Conncection, Input Stream, and Buffered Reader
            HttpURLConnection httpJsonConnection = (HttpURLConnection) yandexTranslateURL.openConnection();
            InputStream inputStream = httpJsonConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            //Set string builder and insert retrieved JSON result into it
            StringBuilder jsonStringBuilder = new StringBuilder();
            while ((jsonLine = bufferedReader.readLine()) != null) {
                jsonStringBuilder.append(jsonLine + "\n");
            }

            //Close and disconnect
            bufferedReader.close();
            inputStream.close();
            httpJsonConnection.disconnect();

            //Making result human readable
            String jsonString = jsonStringBuilder.toString().trim();
            String translation ="";
            TranslationResponse translationResponse = new TranslationResponse();
            try {
                translationResponse = new Gson().fromJson(jsonString, TranslationResponse.class);
                if (translationResponse.getText().size() > 0){
                    translation = translationResponse.getText().get(0);
                }
            } catch (JsonSyntaxException e) {
                e.printStackTrace();
            }

            Log.d("Translation Result:", translation);
            return translation;

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(String result) {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }
}