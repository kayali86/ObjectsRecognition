package com.kayali_developer.objectsrecognition;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.kayali_developer.googlecustomsearchlibrary.GoogleCustomSearchLibraryConstants;
import com.kayali_developer.googlecustomsearchlibrary.SearchResultFragment;

public class SearchResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);
        String googleSearchApiKey;
        String cx;
        String searchWord;

        if (getIntent() != null){
            googleSearchApiKey = getIntent().getStringExtra(GoogleCustomSearchLibraryConstants.GOOGLE_CUSTOM_SEARCH_API_KEY);
            cx = getIntent().getStringExtra(GoogleCustomSearchLibraryConstants.CX_KEY);
            searchWord = getIntent().getStringExtra(GoogleCustomSearchLibraryConstants.SEARCH_WORD_KEY);
            SearchResultFragment searchResultFragment = new SearchResultFragment();
            Bundle bundle = new Bundle();
            bundle.putString(GoogleCustomSearchLibraryConstants.GOOGLE_CUSTOM_SEARCH_API_KEY, googleSearchApiKey);
            bundle.putString(GoogleCustomSearchLibraryConstants.CX_KEY, cx);
            bundle.putString(GoogleCustomSearchLibraryConstants.SEARCH_WORD_KEY, searchWord);
            searchResultFragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, searchResultFragment).commit();
        }
    }
}
