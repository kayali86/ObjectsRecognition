package com.kayali_developer.objectsrecognition.activity;

import android.app.AlertDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
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
    Snackbar mSnackBar;
    @BindView(R.id.snack_bar_layout)
    CoordinatorLayout snackBarLayout;
    @BindView(R.id.saved_objects_toolbar)
    Toolbar savedObjectsToolbar;
    @BindView(R.id.empty_view)
    TextView emptyView;
    @BindView(R.id.pb_loading_indicator)
    ProgressBar pbLoadingIndicator;

    private SavedObjectsAdapter mAdapter;
    private Context mContext;
    private SavedObjectsViewModel mViewModel;
    private Tracker mTracker;
    private MenuItem mDeleteAllMenuItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_objects);
        ButterKnife.bind(this);
        mContext = this;
        setActionBar();
        ObjectsRecognitionApplication application = (ObjectsRecognitionApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mViewModel = ViewModelProviders.of(this).get(SavedObjectsViewModel.class);
        observeSavedObjects();
        populateSavedObjects();
        listenToSwipeToDeleteObjects();
    }

    private void listenToSwipeToDeleteObjects() {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, int swipeDir) {
                // Here is where you'll implement swipe to delete
                int position = viewHolder.getAdapterPosition();
                deleteObject(mAdapter.getObject(position).getId());
            }
        }).attachToRecyclerView(rvSavedObjects);
    }

    private void populateSavedObjects() {
        mAdapter = new SavedObjectsAdapter(this);
        RecyclerView.LayoutManager layoutManager;
        int orientation = getResources().getConfiguration().orientation;
        DisplayMetrics dm = getResources().getDisplayMetrics();
        if (orientation == Configuration.ORIENTATION_LANDSCAPE && dm.widthPixels >= 600) {
            layoutManager = new GridLayoutManager(this, AppConstants.SAVED_OBJECTS_GRID_LAYOUT_COLUMN_COUNT_LAND);
        }else if (orientation == Configuration.ORIENTATION_LANDSCAPE || dm.widthPixels >= 600){
            layoutManager = new GridLayoutManager(this, AppConstants.SAVED_OBJECTS_GRID_LAYOUT_COLUMN_COUNT);
        } else {
            layoutManager = new LinearLayoutManager(this);
        }
        rvSavedObjects.setLayoutManager(layoutManager);
        rvSavedObjects.setHasFixedSize(true);
        rvSavedObjects.setAdapter(mAdapter);
    }

    private void observeSavedObjects() {
        mViewModel.savedObjectsLive.observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> savedObjects) {
                if (savedObjects != null && savedObjects.size() > 0) {
                    showContentView();
                    Collections.reverse(savedObjects);
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory(getString(R.string.saved_objects_count))
                            .setValue(savedObjects.size())
                            .build());
                    mAdapter.setSavedObjects(savedObjects);
                } else {
                    showEmptyView(getString(R.string.no_saved_objects));
                }
            }
        });
    }

    private void setActionBar() {
        setSupportActionBar(savedObjectsToolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void search(String translation) {
        if (!TextUtils.isEmpty(translation)) {
            Intent searchIntent = new Intent(SavedObjectsActivity.this, SearchResultActivity.class);
            searchIntent.setAction(AppConstants.FROM_SAVED_OBJECTS_ACTIVITY_ACTION);
            searchIntent.putExtra(GoogleCustomSearchLibraryConstants.GOOGLE_CUSTOM_SEARCH_API_KEY, AppConstants.GOOGLE_SEARCH_API_KEY);
            searchIntent.putExtra(GoogleCustomSearchLibraryConstants.CX_KEY, AppConstants.CX);
            searchIntent.putExtra(GoogleCustomSearchLibraryConstants.SEARCH_WORD_KEY, translation);
            startActivity(searchIntent);
            SavedObjectsActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
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
            showSnackBar(getString(R.string.object_deleted));
        }
    }

    private void deleteAllObjects() {
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int deletedObjectsCount = mViewModel.deleteAllObjects();
                        if (deletedObjectsCount > 0) {
                            mAdapter.clearAdapter();
                            showSnackBar(getString(R.string.all_objects_deleted));
                        }
                    }
                };
        showUpdateConfirmDialog(discardButtonClickListener);
    }

    private void showSnackBar(String message) {
        mSnackBar = Snackbar
                .make(snackBarLayout, message, Snackbar.LENGTH_SHORT);
        View sbView = mSnackBar.getView();
        TextView textView = sbView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        textView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent, null));
        mSnackBar.show();
    }

    // Display an alert dialog
    private void showUpdateConfirmDialog(
            DialogInterface.OnClickListener updateButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons_red on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
        builder.setMessage(getString(R.string.delete_all_objects_warning));
        builder.setPositiveButton(getString(R.string.delete_all_btn), updateButtonClickListener);
        builder.setNegativeButton(getString(R.string.back), new DialogInterface.OnClickListener() {
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

    private void showEmptyView(String message) {
        emptyView.setVisibility(View.VISIBLE);
        rvSavedObjects.setVisibility(View.GONE);
        emptyView.setText(message);
        if (mDeleteAllMenuItem != null) {
            mDeleteAllMenuItem.setVisible(false);
        }
        pbLoadingIndicator.setVisibility(View.GONE);
    }

    private void showContentView() {
        emptyView.setVisibility(View.GONE);
        rvSavedObjects.setVisibility(View.VISIBLE);
        if (mDeleteAllMenuItem != null) {
            mDeleteAllMenuItem.setVisible(true);
        }
        pbLoadingIndicator.setVisibility(View.GONE);
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
        mTracker.setScreenName(getString(R.string.saved_objects_activity_title));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        mDeleteAllMenuItem = menu.findItem(R.id.action_delete_all);
        mViewModel.savedObjectsLive.observe(this, new Observer<List<Object>>() {
            @Override
            public void onChanged(@Nullable List<Object> savedObjects) {
                if (savedObjects != null && savedObjects.size() > 0) {
                    mDeleteAllMenuItem.setVisible(true);
                } else {
                    mDeleteAllMenuItem.setVisible(false);
                }
            }
        });
        return super.onPrepareOptionsMenu(menu);
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
