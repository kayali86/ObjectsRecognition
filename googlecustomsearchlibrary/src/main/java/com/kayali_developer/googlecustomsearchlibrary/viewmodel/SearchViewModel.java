package com.kayali_developer.googlecustomsearchlibrary.viewmodel;

import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.kayali_developer.googlecustomsearchlibrary.data.model.SearchResponse;
import com.kayali_developer.googlecustomsearchlibrary.data.remote.APIService;
import com.kayali_developer.googlecustomsearchlibrary.data.remote.ApiUtils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchViewModel extends ViewModel {
    public MutableLiveData<SearchResponse> searchResponseLive;

    SearchViewModel(String googleSearchApiKey, String cx, String searchWord) {
        searchResponseLive = new MutableLiveData<>();
        loadSearchResult(googleSearchApiKey, cx, searchWord);
    }

    private void loadSearchResult(String googleSearchApiKey, String cx, String searchWord) {
        final APIService mService = ApiUtils.getAPIService();
        mService.getSearchResults(googleSearchApiKey, cx, searchWord).enqueue(new Callback<SearchResponse>() {
            @Override
            public void onResponse(Call<SearchResponse> call, Response<SearchResponse> response) {
                if (response.isSuccessful()) {
                    searchResponseLive.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<SearchResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}
