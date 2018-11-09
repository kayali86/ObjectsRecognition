package com.kayali_developer.googlecustomsearchlibrary;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kayali_developer.googlecustomsearchlibrary.data.model.SearchResponse;
import com.kayali_developer.googlecustomsearchlibrary.data.remote.APIService;
import com.kayali_developer.googlecustomsearchlibrary.data.remote.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchResultFragment extends Fragment {
    private SearchResultAdapter mSearchResultAdapter;
    private RecyclerView rv_search_result;
    private String viewColor = null;
    private String titleColor = null;
    private String linkColor = null;
    private String descriptionColor = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_result, container, false);
        rv_search_result = rootView.findViewById(R.id.rv_search_result);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext());
        rv_search_result.setLayoutManager(layoutManager);
        rv_search_result.setHasFixedSize(true);
        String googleSearchApiKey = null;
        String cx = null;
        String searchWord = null;
        String backgroundColor;

        if (getArguments() != null){
            try {
                backgroundColor = getArguments().getString(GoogleCustomSearchLibraryConstants.BACKGROUND_COLOR_KEY);
                viewColor = getArguments().getString(GoogleCustomSearchLibraryConstants.VIEW_COLOR_KEY);
                titleColor = getArguments().getString(GoogleCustomSearchLibraryConstants.TITLE_COLOR_KEY);
                linkColor = getArguments().getString(GoogleCustomSearchLibraryConstants.LINK_COLOR_KEY);
                descriptionColor = getArguments().getString(GoogleCustomSearchLibraryConstants.DESCRIPTION_COLOR_KEY);
                googleSearchApiKey = getArguments().getString(GoogleCustomSearchLibraryConstants.GOOGLE_CUSTOM_SEARCH_API_KEY);
                cx = getArguments().getString(GoogleCustomSearchLibraryConstants.CX_KEY);
                searchWord = getArguments().getString(GoogleCustomSearchLibraryConstants.SEARCH_WORD_KEY);
                rootView.setBackgroundColor(Color.parseColor(backgroundColor));
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (googleSearchApiKey != null && cx != null && searchWord != null){
                loadSearchResult(googleSearchApiKey, cx, searchWord, rootView);
            }
        }
        return rootView;
    }

    public void loadSearchResult(String googleSearchApiKey, String cx, String searchWord, final View rootView) {
        final APIService mService = ApiUtils.getAPIService();
        mService.getSearchResults(googleSearchApiKey, cx, searchWord).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    SearchResponse searchResponse = response.body();
                    mSearchResultAdapter = new SearchResultAdapter(itemAdapterOnClickHandler, viewColor, titleColor, linkColor, descriptionColor);
                    mSearchResultAdapter.setSearchResultData(searchResponse);
                    rv_search_result.setAdapter(mSearchResultAdapter);
                    Log.w("SearchResultActivity", ">>>>>>>>>>>>>>>>>>>>>>>>posts loaded from API" + searchResponse.toString());
                } else {
                    int statusCode = response.code();
                    // handle request errors depending on status code
                    Log.w("SearchResultActivity", ">>>>>>>>>>>>>>>>>>>>error loading from API, statusCode: " + statusCode);

                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private SearchResultAdapter.ItemAdapterOnClickHandler itemAdapterOnClickHandler = new SearchResultAdapter.ItemAdapterOnClickHandler() {
        @Override
        public void onClick(SearchResponse.Item currentItem) {
            String url = currentItem.getLink();
            Intent webIntent = new Intent(Intent.ACTION_VIEW);
            webIntent.setData(Uri.parse(url));
            if (getActivity().getPackageManager() != null && webIntent.resolveActivity(getActivity().getPackageManager()) != null){
                startActivity(webIntent);
            }
        }
    };
}
