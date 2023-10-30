package com.example.restyledonate.ui.atencion;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AtencionViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public AtencionViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is atencion fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}