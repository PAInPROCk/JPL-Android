package com.example.jpl2.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jpl2.model.AuthCheckResponse;
import com.example.jpl2.model.LoginResponse;
import com.example.jpl2.repository.AuthRepository;

public class AuthViewModel extends ViewModel {
    private MutableLiveData<LoginResponse> loginResult = new MutableLiveData<>();
    private AuthRepository repository = new AuthRepository();

    public LiveData<LoginResponse> getLoginResult(){
        return loginResult;
    }

    public void login(Context context,String email, String password){
        repository.login(context,email, password, loginResult);
    }

    private MutableLiveData<AuthCheckResponse> authResult = new MutableLiveData<>();

    public LiveData<AuthCheckResponse> getAuthResult(){
        return authResult;
    }

    public void checkAuth(Context context){
        repository.checkAuth(context, authResult);
    }


}
