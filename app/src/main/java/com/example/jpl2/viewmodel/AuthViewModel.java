package com.example.jpl2.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jpl2.model.LoginResponse;
import com.example.jpl2.repository.AuthRepository;

public class AuthViewModel extends ViewModel {
    private MutableLiveData<LoginResponse> loginResult = new MutableLiveData<>();
    private AuthRepository repository = new AuthRepository();

    public LiveData<LoginResponse> getLoginResult(){
        return loginResult;
    }

    public void login(String email, String password){
        repository.login(email, password, loginResult);
    }
}
