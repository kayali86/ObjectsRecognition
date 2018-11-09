package com.kayali_developer.objectsrecognition.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.kayali_developer.objectsrecognition.data.AppDatabase;
import com.kayali_developer.objectsrecognition.data.model.Object;
import com.kayali_developer.onlinetranslationlibrary.Translator;

import timber.log.Timber;

public class MainViewModel extends AndroidViewModel {
    private AppDatabase mDb;
    private Object mObject = new Object();
    public MutableLiveData<Object> objectLive;

    public MainViewModel(@NonNull Application application) {
        super(application);
        Timber.e("MainViewModel");
        mDb = AppDatabase.getInstance(this.getApplication());
        objectLive = new MutableLiveData<>();
    }

    public long storeObject(Object object){
        Timber.e("storeObject %s", object);
        return mDb.objectDao().insertObject(object);
    }

    public void setObjectWord(String word){
        mObject.setWord(word);
        objectLive.setValue(mObject);
    }
    public void setObjectTranslation(String translation){
        mObject.setTranslation(translation);
        objectLive.setValue(mObject);
    }

    public void setObjectImage(byte[] image){
        mObject.setImage(image);
        objectLive.setValue(mObject);
    }

    public void translate(String word){
        if (word != null && !TextUtils.isEmpty(word)) {
            String languagePair = "en-fr";
            Translator translator = new Translator();
            setObjectTranslation(translator.translate(this.getApplication(), word, languagePair));
        }
    }
}
