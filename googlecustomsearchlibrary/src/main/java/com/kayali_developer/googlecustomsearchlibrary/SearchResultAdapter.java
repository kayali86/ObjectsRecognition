package com.kayali_developer.googlecustomsearchlibrary;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.kayali_developer.googlecustomsearchlibrary.data.model.SearchResponse;
import com.squareup.picasso.Picasso;


public class SearchResultAdapter extends RecyclerView.Adapter<SearchResultAdapter.ResultViewHolder> {

    private SearchResponse mSearchResult;
    private final ItemAdapterOnClickHandler mClickHandler;
    private String viewColor = null;
    private String titleColor = null;
    private String linkColor = null;
    private String descriptionColor = null;

    public interface ItemAdapterOnClickHandler {
        void onClick(SearchResponse.Item currentItem);
    }

    public SearchResultAdapter(ItemAdapterOnClickHandler mClickHandler) {
        this.mClickHandler = mClickHandler;
    }

    public SearchResultAdapter(ItemAdapterOnClickHandler mClickHandler, String viewColor, String titleColor, String linkColor, String descriptionColor) {
        this.mClickHandler = mClickHandler;
        this.viewColor = viewColor;
        this.titleColor = titleColor;
        this.linkColor = linkColor;
        this.descriptionColor = descriptionColor;
    }


    public void setSearchResultData(SearchResponse searchResponse) {
        mSearchResult = searchResponse;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ResultViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_search_result;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        // Inflate an item view and return a new viewHolder
        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ResultViewHolder resultViewHolder, int i) {
        SearchResponse.Item currentItem = mSearchResult.getItems().get(i);
        if (viewColor != null && titleColor != null && linkColor != null && descriptionColor != null){
            resultViewHolder.item_layout.setBackgroundColor(Color.parseColor(viewColor));
            resultViewHolder.tv_title.setTextColor(Color.parseColor(titleColor));
            resultViewHolder.tv_link.setTextColor(Color.parseColor(linkColor));
            resultViewHolder.tv_description.setTextColor(Color.parseColor(descriptionColor));
        }
        resultViewHolder.tv_title.setText(currentItem.getTitle());
        resultViewHolder.tv_link.setText(currentItem.getDisplayLink());
        resultViewHolder.tv_description.setText(currentItem.getSnippet());
        String thmbnailUrl;
        if (currentItem.getPagemap().getCse_image() != null && currentItem.getPagemap().getCse_image().size() > 0){
            thmbnailUrl = currentItem.getPagemap().getCse_image().get(0).getSrc();
            Picasso.get()
                    .load(thmbnailUrl)
                    .placeholder(R.drawable.no_image_available)
                    .error(R.drawable.error)
                    .into(resultViewHolder.iv_image);
        }else{
            resultViewHolder.iv_image.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        if (mSearchResult == null || mSearchResult.getItems().size() == 0) {
            return 0;
        }
        return mSearchResult.getItems().size();
    }

    class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        LinearLayout item_layout;
        TextView tv_title;
        TextView tv_link;
        TextView tv_description;
        ImageView iv_image;

        ResultViewHolder(@NonNull View itemView) {
            super(itemView);
            item_layout = itemView.findViewById(R.id.item_layout);
            tv_title = itemView.findViewById(R.id.tv_title);
            tv_link = itemView.findViewById(R.id.tv_link);
            tv_description = itemView.findViewById(R.id.tv_description);
            iv_image = itemView.findViewById(R.id.iv_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            SearchResponse.Item currentItem = mSearchResult.getItems().get(adapterPosition);
            mClickHandler.onClick(currentItem);
        }
    }
}
