package com.kayali_developer.objectsrecognition.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kayali_developer.objectsrecognition.R;
import com.kayali_developer.objectsrecognition.data.model.Object;
import com.kayali_developer.objectsrecognition.utilities.ImageUtils;

import java.util.List;

public class SavedObjectsAdapter extends RecyclerView.Adapter<SavedObjectsAdapter.SavedObjectsAdapterViewHolder> {

    private List<Object> mSavedObjects;
    private SavedObjectsClickHandler mSavedObjectsClickHandler;

    public interface SavedObjectsClickHandler {
        void searchObject(String translation);

        void deleteObjectClicked(long id);
    }

    public SavedObjectsAdapter(SavedObjectsClickHandler savedObjectsClickHandler) {
        this.mSavedObjectsClickHandler = savedObjectsClickHandler;
    }

    @NonNull
    @Override
    public SavedObjectsAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.item_saved_object;
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layoutIdForListItem, viewGroup, false);
        return new SavedObjectsAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final SavedObjectsAdapterViewHolder savedObjectsAdapterViewHolder, int i) {
        final int adapterPosition = savedObjectsAdapterViewHolder.getAdapterPosition();
        Object currentObject = mSavedObjects.get(adapterPosition);
        savedObjectsAdapterViewHolder.iv_saved_object_image.setImageBitmap(ImageUtils.getByteArrayAsBitmap(currentObject.getImage()));
        savedObjectsAdapterViewHolder.tv_word.setText(currentObject.getWord());
        savedObjectsAdapterViewHolder.tv_translation.setText(currentObject.getTranslation());

        savedObjectsAdapterViewHolder.iv_delete_saved_object.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Object currentObject = mSavedObjects.get(adapterPosition);
                mSavedObjectsClickHandler.deleteObjectClicked(currentObject.getId());
            }
        });

        savedObjectsAdapterViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int adapterPosition = savedObjectsAdapterViewHolder.getAdapterPosition();
                Object currentObject = mSavedObjects.get(adapterPosition);
                mSavedObjectsClickHandler.searchObject(currentObject.getTranslation());
            }
        });
    }

    @Override
    public int getItemCount() {
        if (mSavedObjects == null || mSavedObjects.size() <= 0) {
            return 0;
        }
        return mSavedObjects.size();
    }

    public void setSavedObjects(List<Object> savedObjects) {
        mSavedObjects = savedObjects;
        notifyDataSetChanged();
    }

    public int deleteObject(long id) {
        for (int i = 0; i < mSavedObjects.size(); i++) {
            if (mSavedObjects.get(i).getId() == id) {
                mSavedObjects.remove(mSavedObjects.get(i));
                notifyItemRemoved(i);
                notifyDataSetChanged();
                return i;
            }
        }
        return -1;
    }


    public void clearAdapter() {
        if (mSavedObjects != null) {
            mSavedObjects.clear();
            notifyDataSetChanged();
        }
    }

    public Object getObject(int position) {
        return mSavedObjects.get(position);
    }

    class SavedObjectsAdapterViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_saved_object_image;
        TextView tv_word;
        TextView tv_translation;
        ImageView iv_delete_saved_object;

        SavedObjectsAdapterViewHolder(@NonNull final View itemView) {
            super(itemView);
            iv_saved_object_image = itemView.findViewById(R.id.iv_saved_object_image);
            tv_word = itemView.findViewById(R.id.tv_word);
            tv_translation = itemView.findViewById(R.id.tv_translation);
            iv_delete_saved_object = itemView.findViewById(R.id.iv_delete_saved_object);

        }
    }
}
