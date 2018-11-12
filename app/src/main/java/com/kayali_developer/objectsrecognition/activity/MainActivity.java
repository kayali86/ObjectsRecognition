package com.kayali_developer.objectsrecognition.activity;

import android.appwidget.AppWidgetManager;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.kayali_developer.googlecustomsearchlibrary.GoogleCustomSearchLibraryConstants;
import com.kayali_developer.objectsrecognition.AppConstants;
import com.kayali_developer.objectsrecognition.ObjectsRecognitionApplication;
import com.kayali_developer.objectsrecognition.R;
import com.kayali_developer.objectsrecognition.data.model.Object;
import com.kayali_developer.objectsrecognition.tensorflow.Classifier;
import com.kayali_developer.objectsrecognition.tensorflow.TensorFlowImageClassifier;
import com.kayali_developer.objectsrecognition.utilities.ImageUtils;
import com.kayali_developer.objectsrecognition.viewmodel.MainViewModel;
import com.kayali_developer.objectsrecognition.widget.ObjectsRecognitionWidget;
import com.wonderkiln.camerakit.CameraKitError;
import com.wonderkiln.camerakit.CameraKitEvent;
import com.wonderkiln.camerakit.CameraKitEventListener;
import com.wonderkiln.camerakit.CameraKitImage;
import com.wonderkiln.camerakit.CameraKitVideo;
import com.wonderkiln.camerakit.CameraView;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    // Model path for TensorFlow recognition
    private static final String MODEL_PATH = "mobilenet_quant_v1_224.tflite";
    private static final String LABEL_PATH = "labels.txt";
    // Image Input size for TensorFlow recognition
    private static final int INPUT_SIZE = 224;

    @BindView(R.id.camera_view)
    CameraView cameraView;
    @BindView(R.id.iv_thumbnail)
    ImageView ivThumbnail;
    @BindView(R.id.tv_translation_label)
    TextView tvTranslationLabel;
    @BindView(R.id.tv_translation)
    TextView tvTranslation;
    @BindView(R.id.tv_result_label)
    TextView tvResultLabel;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.btn_detect)
    FloatingActionButton btnDetect;
    @BindView(R.id.detect_button_container)
    FrameLayout detectButtonContainer;
    @BindView(R.id.snack_bar_layout)
    CoordinatorLayout snackBarLayout;
    Snackbar mSnackBar;
    @BindView(R.id.main_toolbar)
    Toolbar mainToolbar;
    @BindView(R.id.loading_indicator_container)
    FrameLayout loadingIndicatorContainer;
    @BindView(R.id.adView)
    AdView mAdView;
    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();
    private Object mCurrentObject = new Object();

    private MainViewModel mViewModel;
    private Tracker mTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setActionBar();
        // Obtain the shared Tracker instance.
        ObjectsRecognitionApplication application = (ObjectsRecognitionApplication) getApplication();
        mTracker = application.getDefaultTracker();
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        observeObject();

        mViewModel.translatedLive.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(@Nullable Boolean translated) {
                if (translated != null && translated) {
                    hideLoadingIndicator();
                }
            }
        });
        listenToCamera();
        initTensorFlowAndLoadModel();
    }

    private void listenToCamera() {
        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                Bitmap image = cameraKitImage.getBitmap();
                mViewModel.setObjectImage(ImageUtils.getBitmapAsByteArray(image));
                Bitmap scaledImage = Bitmap.createScaledBitmap(image, INPUT_SIZE, INPUT_SIZE, false);
                ivThumbnail.setImageBitmap(scaledImage);
                final List<Classifier.Recognition> results = classifier.recognizeImage(scaledImage);
                String word;
                if (results != null && results.size() > 0) {
                    word = results.get(0).getTitle();
                    mViewModel.setObjectWord(word);
                    mTracker.send(new HitBuilders.EventBuilder()
                            .setCategory("Word")
                            .setAction(word)
                            .build());
                    mTracker.set("Word", word);
                } else {
                    hideLoadingIndicator();
                    showSnackBar("Can't recognize the Objects!");
                }
            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {
            }
        });
    }

    private void observeObject() {
        mViewModel.objectLive.observe(this, new Observer<Object>() {
            @Override
            public void onChanged(@Nullable Object object) {
                if (object != null) {
                    mCurrentObject = object;
                    if (object.getImage() != null) {
                        Bitmap scaledImage = Bitmap.createScaledBitmap(ImageUtils.getByteArrayAsBitmap(object.getImage()), INPUT_SIZE, INPUT_SIZE, false);
                        ivThumbnail.setImageBitmap(scaledImage);
                    }
                    if (object.getWord() != null && !TextUtils.isEmpty(object.getWord())) {
                        tvResult.setText(object.getWord());
                        if (mCurrentObject.getTranslation() == null) {
                            mViewModel.translate(mCurrentObject.getWord());
                        }
                    }
                    if (object.getTranslation() != null && !TextUtils.isEmpty(object.getTranslation())) {
                        tvTranslation.setText(object.getTranslation());
                    }
                }
            }
        });
    }

    private void setActionBar() {
        setSupportActionBar(mainToolbar);
        try {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTracker.setScreenName(getString(R.string.app_name));
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
        cameraView.start();
    }

    @Override
    protected void onPause() {
        cameraView.stop();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                classifier.close();
            }
        });
    }

    private void initTensorFlowAndLoadModel() {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    classifier = TensorFlowImageClassifier.create(
                            getAssets(),
                            MODEL_PATH,
                            LABEL_PATH,
                            INPUT_SIZE);
                    makeButtonVisible();
                } catch (final Exception e) {
                    throw new RuntimeException("Error initializing TensorFlow!", e);
                }
            }
        });
    }

    private void makeButtonVisible() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                detectButtonContainer.setVisibility(View.VISIBLE);
            }
        });
    }

    private void showLoadingIndicator() {
        loadingIndicatorContainer.setVisibility(View.VISIBLE);
    }

    private void hideLoadingIndicator() {
        loadingIndicatorContainer.setVisibility(View.GONE);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {
            if (!TextUtils.isEmpty(mCurrentObject.getTranslation())) {
                Intent searchIntent = new Intent(MainActivity.this, SearchResultActivity.class);
                searchIntent.setAction(AppConstants.FROM_MAIN_ACTIVITY_ACTION);
                searchIntent.putExtra(GoogleCustomSearchLibraryConstants.GOOGLE_CUSTOM_SEARCH_API_KEY, AppConstants.GOOGLE_SEARCH_API_KEY);
                searchIntent.putExtra(GoogleCustomSearchLibraryConstants.CX_KEY, AppConstants.CX);
                searchIntent.putExtra(GoogleCustomSearchLibraryConstants.SEARCH_WORD_KEY, mCurrentObject.getTranslation());
                startActivity(searchIntent);
                MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
            } else {
                showSnackBar(getResources().getString(R.string.scan_object_first));
            }
        }

        if (item.getItemId() == R.id.action_save) {
            if (!TextUtils.isEmpty(mCurrentObject.getWord()) && !TextUtils.isEmpty(mCurrentObject.getTranslation()) && mCurrentObject.getImage() != null) {
                long count = mViewModel.storeObject(mCurrentObject);
                if (count > 0) {
                    updateWidget();
                    showSnackBar(getString(R.string.object_saved));

                } else {
                    showSnackBar(getString(R.string.cannot_save_object));
                }
            } else {
                showSnackBar(getResources().getString(R.string.scan_object_first));
            }
        }
        if (item.getItemId() == R.id.action_saved_objects_list) {
            Intent intent = new Intent(MainActivity.this, SavedObjectsActivity.class);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
                MainActivity.this.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_in);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateWidget() {
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, ObjectsRecognitionWidget.class));
        //Trigger data update to handle the GridView widgets and force a data refresh
        appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.widget_item_layout);
        //Now update all widgets
        ObjectsRecognitionWidget.updatePlantWidgets(this, appWidgetManager, appWidgetIds);
    }

    @OnClick(R.id.btn_detect)
    public void onViewClicked() {
        showLoadingIndicator();
        if (mCurrentObject.getWord() != null && !TextUtils.isEmpty(mCurrentObject.getWord())) {
            tvResult.setText(mCurrentObject.getWord());
            mViewModel.translate(mCurrentObject.getWord());
            tvTranslation.setText(mCurrentObject.getTranslation());
        }
        cameraView.captureImage();
    }
}
