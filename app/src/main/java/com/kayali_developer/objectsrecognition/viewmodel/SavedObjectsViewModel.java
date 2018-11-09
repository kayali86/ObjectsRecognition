package com.kayali_developer.objectsrecognition.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import com.kayali_developer.objectsrecognition.data.AppDatabase;
import com.kayali_developer.objectsrecognition.data.model.Object;

import java.util.List;

import timber.log.Timber;

public class SavedObjectsViewModel extends AndroidViewModel {
    private AppDatabase mDb;
    public MutableLiveData<List<Object>> savedObjectsLive;

    public SavedObjectsViewModel(@NonNull Application application) {
        super(application);
        Timber.e("SavedObjectsViewModel ");
        mDb = AppDatabase.getInstance(this.getApplication());
        savedObjectsLive = new MutableLiveData<>();
        loadAllSavedObjects();
    }

    private void loadAllSavedObjects(){
        savedObjectsLive.setValue(mDb.objectDao().loadAllObjects());
    }


    public int deleteObjectById(long id){
        int deletedObjectsCount = mDb.objectDao().deleteObjectById(id);
        loadAllSavedObjects();
        return deletedObjectsCount;
    }


    public int deleteAllObjects(){
        int deletedObjectsCount = mDb.objectDao().deleteAllObjects();
        loadAllSavedObjects();
        return deletedObjectsCount;
    }

}
