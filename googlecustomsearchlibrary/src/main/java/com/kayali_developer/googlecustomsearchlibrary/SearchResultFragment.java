package com.kayali_developer.googlecustomsearchlibrary;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kayali_developer.googlecustomsearchlibrary.data.model.SearchResponse;
import com.kayali_developer.googlecustomsearchlibrary.viewmodel.SearchViewModel;
import com.kayali_developer.googlecustomsearchlibrary.viewmodel.SearchViewModelFactory;

public class SearchResultFragment extends Fragment {
    private SearchResultAdapter mSearchResultAdapter;
    private RecyclerView rv_search_result;
    private String viewColor = null;
    private String titleColor = null;
    private String linkColor = null;
    private String descriptionColor = null;
    private TextView emptyView;
    private ProgressBar pbLoadingIndicator;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_search_result, container, false);
        rv_search_result = rootView.findViewById(R.id.rv_search_result);
        emptyView = rootView.findViewById(R.id.empty_view);
        pbLoadingIndicator = rootView.findViewById(R.id.pb_loading_indicator);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext());
        rv_search_result.setLayoutManager(layoutManager);
        rv_search_result.setHasFixedSize(true);
        String googleSearchApiKey = null;
        String cx = null;
        String searchWord = null;
        String backgroundColor;

        if (getArguments() != null) {
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
            if (googleSearchApiKey != null && cx != null && searchWord != null) {
                SearchViewModel mViewModel = ViewModelProviders.of(this, new SearchViewModelFactory(googleSearchApiKey, cx, searchWord)).get(SearchViewModel.class);
                mViewModel.searchResponseLive.observe(this, new Observer<SearchResponse>() {
                    @Override
                    public void onChanged(@Nullable SearchResponse searchResponse) {
                        if (searchResponse != null) {
                            showContentView();
                            mSearchResultAdapter = new SearchResultAdapter(itemAdapterOnClickHandler, viewColor, titleColor, linkColor, descriptionColor);
                            mSearchResultAdapter.setSearchResultData(searchResponse);
                            RecyclerView.LayoutManager layoutManager;
                            int orientation = getResources().getConfiguration().orientation;
                            int sw = getResources().getConfiguration().smallestScreenWidthDp;
                            if (orientation == Configuration.ORIENTATION_LANDSCAPE && sw >= 600) {
                                layoutManager = new GridLayoutManager(getActivity(), 3);
                            }else if (orientation == Configuration.ORIENTATION_LANDSCAPE || sw >= 600){
                                layoutManager = new GridLayoutManager(getActivity(), 2);
                            } else {
                                layoutManager = new LinearLayoutManager(getActivity());
                            }

                            rv_search_result.setLayoutManager(layoutManager);
                            rv_search_result.hasFixedSize();
                            rv_search_result.setAdapter(mSearchResultAdapter);
                        } else {
                            showEmptyView(getString(R.string.no_search_results));
                        }
                    }
                });
            }
        }
        return rootView;
    }

    private SearchResultAdapter.ItemAdapterOnClickHandler itemAdapterOnClickHandler = new SearchResultAdapter.ItemAdapterOnClickHandler() {
        @Override
        public void onClick(SearchResponse.Item currentItem) {
            String url = currentItem.getLink();
            Intent webIntent = new Intent(Intent.ACTION_VIEW);
            webIntent.setData(Uri.parse(url));
            if (getActivity().getPackageManager() != null && webIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivity(webIntent);
            }
        }
    };

    private void showEmptyView(String message) {
        emptyView.setVisibility(View.VISIBLE);
        rv_search_result.setVisibility(View.GONE);
        emptyView.setText(message);
        pbLoadingIndicator.setVisibility(View.GONE);
    }

    private void showContentView() {
        emptyView.setVisibility(View.GONE);
        rv_search_result.setVisibility(View.VISIBLE);
        pbLoadingIndicator.setVisibility(View.GONE);
    }
}
