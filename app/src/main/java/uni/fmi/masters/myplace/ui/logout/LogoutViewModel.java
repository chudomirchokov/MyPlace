package uni.fmi.masters.myplace.ui.logout;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LogoutViewModel extends ViewModel {

    private MutableLiveData<String> mText;


    public LogoutViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("ARE YOU SURE, YOU WANT TO LEAVE!!!");
    }

    public LiveData<String> getText() {
        return mText;
    }

}

