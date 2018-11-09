package com.kayali_developer.objectsrecognition.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

import com.kayali_developer.objectsrecognition.AppConstants;

import java.util.ArrayList;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        ArrayList<String> words = intent.getStringArrayListExtra(AppConstants.ALL_SAVED_WORDS_KEY);
        ArrayList<String> translations = intent.getStringArrayListExtra(AppConstants.ALL_SAVED_TRANSLATIONS_KEY);
        return new ObjectsRecognitionRemoteViewsFactory(this.getApplicationContext(), words, translations);
    }
}
