package com.kayali_developer.googlecustomsearchlibrary.data.remote;

import com.kayali_developer.googlecustomsearchlibrary.data.model.SearchResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface APIService {
    @GET("customsearch/v1?")
    Call<SearchResponse> getSearchResults
            (@Query("key") String GOOGLE_SEARCH_API_KEY,
             @Query("cx") String CX,
             @Query("q") String searchWord);

}