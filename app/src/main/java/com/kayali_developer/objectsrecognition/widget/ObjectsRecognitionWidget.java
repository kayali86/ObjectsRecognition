package com.kayali_developer.objectsrecognition.widget;

import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.widget.RemoteViews;

import com.kayali_developer.objectsrecognition.R;
import com.kayali_developer.objectsrecognition.activity.MainActivity;
import com.kayali_developer.objectsrecognition.activity.SearchResultActivity;

import java.util.Random;

public class ObjectsRecognitionWidget extends AppWidgetProvider {

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        // Construct the RemoteViews object
        RemoteViews views;
        try {
            views = new RemoteViews(context.getPackageName(), R.layout.objects_recognition_widget);

            Random random = new Random();
            int randomNumber = random.nextInt();
            Intent intent = new Intent(context, WidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
            intent.putExtra("random", randomNumber);
            intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));
            views.setEmptyView(R.id.lv_widget_saved_objects, R.id.widget_empty_view);
            // Bind the remote adapter
            views.setRemoteAdapter(R.id.lv_widget_saved_objects, intent);

            final Intent appIntent = new Intent(context, MainActivity.class);
            final PendingIntent appPendingIntent = PendingIntent.getActivity(context, 0, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setOnClickPendingIntent(R.id.widget_label_layout, appPendingIntent);

            final Intent searchIntent = new Intent(context, SearchResultActivity.class);
            final PendingIntent searchPendingIntent = PendingIntent.getActivity(context, 1, searchIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            views.setPendingIntentTemplate(R.id.lv_widget_saved_objects, searchPendingIntent);

            // Instruct the widget manager to update the widget
            appWidgetManager.updateAppWidget(appWidgetId, views);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        updatePlantWidgets(context, appWidgetManager, appWidgetIds);
    }


    public static void updatePlantWidgets(Context context, AppWidgetManager appWidgetManager,
                                          int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

