package com.kayali_developer.objectsrecognition.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.kayali_developer.googlecustomsearchlibrary.GoogleCustomSearchLibraryConstants;
import com.kayali_developer.objectsrecognition.AppConstants;
import com.kayali_developer.objectsrecognition.R;

import java.util.ArrayList;
import java.util.Collections;

public class ObjectsRecognitionRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private Context mContext;
    private ArrayList<String> mWords;
    private ArrayList<String> mTranslations;

    ObjectsRecognitionRemoteViewsFactory(Context mContext, ArrayList<String> words, ArrayList<String> translations) {
        this.mContext = mContext;
        try {
            Collections.reverse(words);
            Collections.reverse(translations);
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mWords = words;
        this.mTranslations = translations;

    }

    private void loadAllObjectsFromDb() {
        Intent loadIntent = new Intent(mContext, ObjectsWidgetLoadService.class);
        mContext.startService(loadIntent);
    }


    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {
        loadAllObjectsFromDb();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        if (mWords == null) return 0;
        return mWords.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {
        if (mWords == null || mWords.size() == 0 || mTranslations == null || mTranslations.size() == 0)
            return null;
        RemoteViews views = new RemoteViews(mContext.getPackageName(), R.layout.widget_list_item);
        views.setTextViewText(R.id.tv_word_widget, mWords.get(position));
        views.setTextViewText(R.id.tv_translation_widget, String.valueOf(mTranslations.get(position)));

        Intent searchIntent = new Intent();
        searchIntent.setAction(AppConstants.FROM_WIDGET_ACTION);
        searchIntent.putExtra(GoogleCustomSearchLibraryConstants.GOOGLE_CUSTOM_SEARCH_API_KEY, AppConstants.GOOGLE_SEARCH_API_KEY);
        searchIntent.putExtra(GoogleCustomSearchLibraryConstants.CX_KEY, AppConstants.CX);
        searchIntent.putExtra(GoogleCustomSearchLibraryConstants.SEARCH_WORD_KEY, mTranslations.get(position));
        searchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        views.setOnClickFillInIntent(R.id.widget_item_layout, searchIntent);

        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
