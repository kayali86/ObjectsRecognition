package com.kayali_developer.objectsrecognition.widget;

import android.app.IntentService;
import android.content.Intent;

import com.kayali_developer.objectsrecognition.data.AppDatabase;

public class ObjectsWidgetLoadService extends IntentService {
    private AppDatabase mDb;

    public ObjectsWidgetLoadService() {
        super("ObjectsWidgetLoadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        mDb = AppDatabase.getInstance(this);
        ObjectsRecognitionWidget.updateObjects(mDb.objectDao().loadAllObjects());
        ObjectsRecognitionWidget.updateWidget();
    }
}
