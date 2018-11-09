package com.kayali_developer.objectsrecognition.activity;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kayali_developer.googlecustomsearchlibrary.GoogleCustomSearchLibraryConstants;
import com.kayali_developer.objectsrecognition.AppConstants;
import com.kayali_developer.objectsrecognition.ObjectsRecognitionApplication;
import com.kayali_developer.objectsrecognition.R;
import com.kayali_developer.objectsrecognition.adapter.SavedObjectsAdapter;
import com.kayali_developer.objectsrecognition.data.model.Object;
import com.kayali_developer.objectsrecognition.viewmodel.SavedObjectsViewModel;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SavedObjectsActivity extends AppCompatActivity implements SavedObjectsAdapter.SavedObjectsClickHandler {
    @BindView(R.id.rv_saved_objects)
    RecyclerView rvSavedObjects;
    Snackbar mSnackbar;
    @BindView(R.id.snackbar_layout)
    CoordinatorLayout snackbarLayout;
    private SavedObjectsAdapter mAdapter;

    private Context mContext;
    private SavedObjectsViewModel mViewModel;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_objects);
        ButterKnife.bind(this);
        mContext = this;
        // Obtain the shared Tracker instance.
        ObjectsRecognitionApplication application = (ObjectsRecognitionApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mViewModel = ViewModelProviders.of(this).get(SavedObjectsViewModel.class);
        mViewModel.savedObjectsLive.observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> savedObjects) {
                if (savedObjects != null && savedObjects.size() > 0) {
                    Collections.reverse(savedObjects);
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Saved Objects count")
                            .setValue(savedObjects.size())
                            .build());
                    mAdapter.setSavedObjects(savedObjects);
                }
            }
        });

        mAdapter = new SavedObjectsAdapter(this);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this);
        rvSavedObjects.setLayoutManager(layoutManager);
        rvSavedObjects.setHasFixedSize(true);
        rvSavedObjects.setAdapter(mAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            // Called when a user swipes left or right on a ViewHolder
            @Override
            public void onSwiped(final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                int position = viewHolder.getAdapterPosition();
                deleteObject(mAdapter.getObject(position).getId());
            }
        }).attachToRecyclerView(rvSavedObjects);
    }

    private void search(String translation) {
        if (!TextUtils.isEmpty(translation)) {
            Intent searchIntent = new Intent(SavedObjectsActivity.this, SearchResultActivity.class);
            searchIntent.setAction(AppConstants.FROM_SAVED_OBJECTS_ACTIVITY_ACTION);
            searchIntent.putExtra(GoogleCustomSearchLibraryConstants.GOOGLE_CUSTOM_SEARCH_API_KEY, AppConstants.GOOGLE_SEARCH_API_KEY);
            searchIntent.putExtra(GoogleCustomSearchLibraryConstants.CX_KEY, AppConstants.CX);
            searchIntent.putExtra(GoogleCustomSearchLibraryConstants.SEARCH_WORD_KEY, translation);
            startActivity(searchIntent);
        } else {
            showSnackBar(getResources().getString(R.string.scan_object_first));
        }
    }

    private void deleteObject(long id) {
        int deletedObjectsCount = mViewModel.deleteObjectById(id);
        if (deletedObjectsCount > 0) {
            int index = mAdapter.deleteObject(id);
            if (index >= 0) {
                rvSavedObjects.removeViewAt(index);
            }
            showSnackBar("Object deleted" + deletedObjectsCount);
        }
    }

    private void deleteAllObjects(){
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int deletedObjectsCount = mViewModel.deleteAllObjects();
                        if (deletedObjectsCount > 0) {
                            mAdapter.clearAdapter();
                            showSnackBar("All Objects deleted" + deletedObjectsCount);
                        }
                    }
                };
        // Show a dialog that notifies the user they have unsaved changes
        showUpdateConfirmDialog(discardButtonClickListener);
    }

    private void showSnackBar(String message) {
        mSnackbar = Snackbar
                .make(snackbarLayout, message, Snackbar.LENGTH_SHORT);
        View sbView = mSnackbar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        mSnackbar.show();
    }

    // Display an alert dialog
    private void showUpdateConfirmDialog(
            DialogInterface.OnClickListener updateButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons_red on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage("Do you want to delete all saved Objects?");
        builder.setPositiveButton("Delete all", updateButtonClickListener);
        builder.setNegativeButton("Back", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });
        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }



    @Override
    public void searchObject(String translation) {
        search(translation);
    }

    @Override
    public void deleteObjectClicked(long id) {
        deleteObject(id);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName("SavedObjectsActivity");
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.saved_objects_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_delete_all) {
            deleteAllObjects();
        }
        return super.onOptionsItemSelected(item);
    }

}
