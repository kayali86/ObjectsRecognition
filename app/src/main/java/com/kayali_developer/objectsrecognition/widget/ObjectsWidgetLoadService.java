package com.kayali_developer.objectsrecognition.widget;

import android.app.IntentService;
import android.content.Intent;

import com.kayali_developer.objectsrecognition.data.AppDatabase;
import com.kayali_developer.objectsrecognition.data.model.Object;

import java.util.List;

public class ObjectsWidgetLoadService extends IntentService {
    public static final String FROM_WIDGET_ACTION = "from_widget";
    public static final String FROM_FACTORY_ACTION = "from_factory";
    public ObjectsWidgetLoadService() {
        super("ObjectsWidgetLoadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        AppDatabase mDb = AppDatabase.getInstance(this);
        List<Object> objects = mDb.objectDao().loadAllObjects();
        if (intent.getAction() != null){
            if (intent.getAction().equals(FROM_WIDGET_ACTION)){
                ObjectsRecognitionWidget.updateObjects(objects);
                ObjectsRecognitionWidget.updateWidget();
            }else if (intent.getAction().equals(FROM_FACTORY_ACTION)){
                ObjectsRecognitionRemoteViewsFactory.setWordsTranslations(objects);
                ObjectsRecognitionWidget.updateWidget();
            }
        }

    }
}
