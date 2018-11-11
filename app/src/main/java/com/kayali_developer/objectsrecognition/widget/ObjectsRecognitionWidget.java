package com.kayali_developer.objectsrecognition.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.kayali_developer.objectsrecognition.AppConstants;
import com.kayali_developer.objectsrecognition.R;
import com.kayali_developer.objectsrecognition.activity.MainActivity;
import com.kayali_developer.objectsrecognition.activity.SearchResultActivity;
import com.kayali_developer.objectsrecognition.data.model.Object;

import java.util.ArrayList;
import java.util.List;

public class ObjectsRecognitionWidget extends AppWidgetProvider {
    private static Context mContext;
    private static AppWidgetManager mAppWidgetManager;
    private static int mAppWidgetId;
    private static List<Object> sObjects = new ArrayList<>();

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views;
        try {
            views = new RemoteViews(context.getPackageName(), R.layout.objects_recognition_widget);

            Intent intent = new Intent(context, WidgetService.class);
            ArrayList<String> wordsList = new ArrayList<>();
            ArrayList<String> translationsList = new ArrayList<>();
            for (Object object : sObjects) {
                wordsList.add(object.getWord());
                translationsList.add(object.getTranslation());
            }
            intent.putStringArrayListExtra(AppConstants.ALL_SAVED_WORDS_KEY, wordsList);
            intent.putStringArrayListExtra(AppConstants.ALL_SAVED_TRANSLATIONS_KEY, translationsList);

            // Bind the remote adapter
            views.setRemoteAdapter(R.id.lv_widget_saved_objects, intent);

            final Intent appIntent = new Intent(context, MainActivity.class);
            final PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_label_layout, appPendingIntent);

            final Intent searchIntent = new Intent(context, SearchResultActivity.class);
            final PendingIntent searchPendingIntent = PendingIntent.getActivity(context, 1, searchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.widget_item_layout, searchPendingIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            mContext = context;
            mAppWidgetManager = appWidgetManager;
            mAppWidgetId = appWidgetId;
            Intent loadIntent = new Intent(context, ObjectsWidgetLoadService.class);
            loadIntent.setAction(ObjectsWidgetLoadService.FROM_WIDGET_ACTION);
            context.startService(loadIntent);
        }
    }

    public static void updateWidget() {
        updateAppWidget(mContext, mAppWidgetManager, mAppWidgetId);
    }

    public static void updateObjects(List<Object> objects) {
        sObjects = objects;
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
    }
}

