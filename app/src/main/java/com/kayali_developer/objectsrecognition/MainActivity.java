package com.kayali_developer.objectsrecognition;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kayali_developer.googlecustomsearchlibrary.GoogleCustomSearchLibraryConstants;
import com.kayali_developer.onlinetranslationlibrary.Translator;
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

public class MainActivity extends AppCompatActivity {
    private Context mContext;
    private static final String MODEL_PATH = "mobilenet_quant_v1_224.tflite";
    private static final String LABEL_PATH = "labels.txt";
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

    private Classifier classifier;
    private Executor executor = Executors.newSingleThreadExecutor();
    private String mWord = "";
    private String mTranslation = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        mContext = this;

        cameraView.addCameraKitListener(new CameraKitEventListener() {
            @Override
            public void onEvent(CameraKitEvent cameraKitEvent) {

            }

            @Override
            public void onError(CameraKitError cameraKitError) {

            }

            @Override
            public void onImage(CameraKitImage cameraKitImage) {

                Bitmap bitmap = cameraKitImage.getBitmap();

                bitmap = Bitmap.createScaledBitmap(bitmap, INPUT_SIZE, INPUT_SIZE, false);

                ivThumbnail.setImageBitmap(bitmap);

                final List<Classifier.Recognition> results = classifier.recognizeImage(bitmap);
                mWord = results.get(0).getTitle();
                if (mWord != null && !TextUtils.isEmpty(mWord)) {
                    tvResult.setText(mWord);
                    String languagePair = "en-fr";
                    Translator translator = new Translator();
                    mTranslation = translator.translate(mContext, mWord, languagePair);
                    tvTranslation.setText(mTranslation);
                }


                /*
                Intent intent = new Intent(MainActivity.this, SearchActivity.class);
                intent.putExtra(AppConstants.SEARCH_TOPIC_KEY, results.toString());
                if (intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
                */

            }

            @Override
            public void onVideo(CameraKitVideo cameraKitVideo) {

            }
        });

        btnDetect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String word = "Mouse";
                if (word != null && !TextUtils.isEmpty(word)) {
                    tvResult.setText(word);
                    String languagePair = "en-fr";
                    Translator translator = new Translator();
                    mTranslation = translator.translate(mContext, word, languagePair);
                    tvTranslation.setText(mTranslation);
                }

                //cameraView.captureImage();
            }
        });

        initTensorFlowAndLoadModel();
    }


    @Override
    protected void onResume() {
        super.onResume();
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

    private void showToastMessage(String message){
        Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search){
            if (!TextUtils.isEmpty(mTranslation)){
                Intent searchIntent = new Intent(MainActivity.this, SearchResultActivity.class);
                searchIntent.putExtra(GoogleCustomSearchLibraryConstants.GOOGLE_CUSTOM_SEARCH_API_KEY, AppConstants.GOOGLE_SEARCH_API_KEY);
                searchIntent.putExtra(GoogleCustomSearchLibraryConstants.CX_KEY, AppConstants.CX);
                searchIntent.putExtra(GoogleCustomSearchLibraryConstants.SEARCH_WORD_KEY, mTranslation);
                startActivity(searchIntent);
            }else{
                showToastMessage(getResources().getString(R.string.scan_object_first));
            }
        }

        if (item.getItemId() == R.id.action_save){

        }
        return super.onOptionsItemSelected(item);
    }
}
