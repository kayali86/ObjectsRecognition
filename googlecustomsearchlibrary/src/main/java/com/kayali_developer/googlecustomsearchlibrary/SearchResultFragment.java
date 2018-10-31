package com.kayali_developer.googlecustomsearchlibrary;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_result, container, false);
        rv_search_result = rootView.findViewById(R.id.rv_search_result);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext());
        rv_search_result.setLayoutManager(layoutManager);
        rv_search_result.setHasFixedSize(true);
        String googleSearchApiKey;
        String cx;
        String searchWord;

        if (getArguments() != null){
            googleSearchApiKey = getArguments().getString(GoogleCustomSearchLibraryConstants.GOOGLE_CUSTOM_SEARCH_API_KEY);
            cx = getArguments().getString(GoogleCustomSearchLibraryConstants.CX_KEY);
            searchWord = getArguments().getString(GoogleCustomSearchLibraryConstants.SEARCH_WORD_KEY);
            loadSearchResult(googleSearchApiKey, cx, searchWord, rootView);
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
                    mSearchResultAdapter = new SearchResultAdapter();
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
}
