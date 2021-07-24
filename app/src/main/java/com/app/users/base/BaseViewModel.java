package com.app.users.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {
    public BaseRepository mRepository;

    public void setRepository(BaseRepository baseRepository) {
        mRepository = baseRepository;
    }

    public <T extends BaseRepository> BaseRepository getRepository() {
        return mRepository;
    }

    public MutableLiveData<String> showToastEvent = new MutableLiveData<>();


    public void showToastMessage(String message) {
        showToastEvent.setValue(message);
    }
}
