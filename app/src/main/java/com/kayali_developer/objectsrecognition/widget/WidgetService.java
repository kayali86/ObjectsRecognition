package com.kayali_developer.objectsrecognition.widget;

import android.content.Intent;
import android.widget.RemoteViewsService;

public class WidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new ObjectsRecognitionRemoteViewsFactory(this.getApplicationContext());
    }
}
