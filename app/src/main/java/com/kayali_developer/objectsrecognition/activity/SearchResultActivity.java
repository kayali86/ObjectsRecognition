package com.kayali_developer.objectsrecognition.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kayali_developer.googlecustomsearchlibrary.GoogleCustomSearchLibraryConstants;
import com.kayali_developer.googlecustomsearchlibrary.SearchResultFragment;
import com.kayali_developer.objectsrecognition.AppConstants;
import com.kayali_developer.objectsrecognition.ObjectsRecognitionApplication;
import com.kayali_developer.objectsrecognition.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchResultActivity extends AppCompatActivity {
    @BindView(R.id.search_toolbar)
    Toolbar searchToolbar;
    private boolean mFromSavedObjectsActivity = false;
    private boolean mFromWidget = false;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        setActionBar();
        // Obtain the shared Tracker instance.
        ObjectsRecognitionApplication application = (ObjectsRecognitionApplication) getApplication();
        mTracker = application.getDefaultTracker();
        handleAppropriateAction();
        getValuesFromIntentAndInitializeResultsFragment();
    }

    private void getValuesFromIntentAndInitializeResultsFragment() {
        String googleSearchApiKey;
        String cx;
        String searchWord;

        if (getIntent() != null) {
            googleSearchApiKey = getIntent().getStringExtra(GoogleCustomSearchLibraryConstants.GOOGLE_CUSTOM_SEARCH_API_KEY);
            cx = getIntent().getStringExtra(GoogleCustomSearchLibraryConstants.CX_KEY);
            searchWord = getIntent().getStringExtra(GoogleCustomSearchLibraryConstants.SEARCH_WORD_KEY);
            SearchResultFragment searchResultFragment = new SearchResultFragment();
            Bundle bundle = new Bundle();
            bundle.putString(GoogleCustomSearchLibraryConstants.GOOGLE_CUSTOM_SEARCH_API_KEY, googleSearchApiKey);
            bundle.putString(GoogleCustomSearchLibraryConstants.CX_KEY, cx);
            bundle.putString(GoogleCustomSearchLibraryConstants.SEARCH_WORD_KEY, searchWord);
            bundle.putString(GoogleCustomSearchLibraryConstants.BACKGROUND_COLOR_KEY, retrieveColor(R.color.backgroundColor));
            bundle.putString(GoogleCustomSearchLibraryConstants.VIEW_COLOR_KEY, retrieveColor(R.color.searchViewColor));
            bundle.putString(GoogleCustomSearchLibraryConstants.TITLE_COLOR_KEY, retrieveColor(R.color.colorAccent));
            bundle.putString(GoogleCustomSearchLibraryConstants.LINK_COLOR_KEY, retrieveColor(R.color.colorPrimaryDark));
            bundle.putString(GoogleCustomSearchLibraryConstants.DESCRIPTION_COLOR_KEY, retrieveColor(R.color.secondaryTextColor));
            searchResultFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchResultFragment).commit();
        }
    }

    private void handleAppropriateAction() {
        if (getIntent() != null && getIntent().getAction() != null) {
            switch (getIntent().getAction()) {
                case AppConstants.FROM_MAIN_ACTIVITY_ACTION: {
                    mFromSavedObjectsActivity = false;
                    break;
                }
                case AppConstants.FROM_SAVED_OBJECTS_ACTIVITY_ACTION: {
                    mFromSavedObjectsActivity = true;
                    break;
                }
                case AppConstants.FROM_WIDGET_ACTION: {
                    mFromSavedObjectsActivity = false;
                    mFromWidget = true;
                    break;
                }
            }
        }
    }

    private void setActionBar() {
        setSupportActionBar(searchToolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String retrieveColor(int color) {
        int intColor = ResourcesCompat.getColor(getResources(), color, null);
        return convertColorHex(intColor);
    }

    private String convertColorHex(int intColor) {
        return String.format("#%06X", (0xFFFFFF & intColor));
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(AppConstants.FROM_SAVED_OBJECTS_ACTIVITY_BOOLEAN_KEY, mFromSavedObjectsActivity);
        outState.putBoolean(AppConstants.FROM_WIDGET_BOOLEAN_KEY, mFromWidget);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        mFromSavedObjectsActivity = savedInstanceState.getBoolean(AppConstants.FROM_SAVED_OBJECTS_ACTIVITY_BOOLEAN_KEY);
        mFromSavedObjectsActivity = savedInstanceState.getBoolean(AppConstants.FROM_WIDGET_BOOLEAN_KEY);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mFromWidget) {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName(getString(R.string.search_activity_title));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public Intent getParentActivityIntent() {
        if (mFromSavedObjectsActivity) {
            return new Intent(this, SavedObjectsActivity.class);
        } else {
            return super.getParentActivityIntent();
        }
    }
}
