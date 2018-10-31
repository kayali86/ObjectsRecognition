package com.kayali_developer.onlinetranslationlibrary;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

public class Translator {

    public String translate(Context context, String textToBeTranslated, String languagePair) {
        String translationResult = null;
        if (textToBeTranslated != null && !TextUtils.isEmpty(textToBeTranslated)) {
            TranslatorBackgroundTask translatorBackgroundTask = new TranslatorBackgroundTask(context);
            // Returns the translated text as a String
            try {
                translationResult = translatorBackgroundTask.execute(textToBeTranslated, languagePair).get();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.w("Translation Result", ">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + translationResult); // Logs the result in Android Monitor
        return translationResult;
    }
}
