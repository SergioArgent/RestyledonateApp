package com.example.restyledonate.ui.educacion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EducacionViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public EducacionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is educacion fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}