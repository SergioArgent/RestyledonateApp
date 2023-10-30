package com.example.restyledonate.ui.donaciones;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DonacionesViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public DonacionesViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is donaciones fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}