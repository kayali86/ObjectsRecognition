package com.kayali_developer.googlecustomsearchlibrary.viewmodel;

import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

public class SearchViewModelFactory implements ViewModelProvider.Factory {
    private String googleSearchApiKey;
    private String cx;
    private String searchWord;

    public SearchViewModelFactory(String googleSearchApiKey, String cx, String searchWord) {
        this.googleSearchApiKey = googleSearchApiKey;
        this.cx = cx;
        this.searchWord = searchWord;
    }

    @SuppressWarnings("unchecked")
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new SearchViewModel(googleSearchApiKey, cx, searchWord);
    }
}
