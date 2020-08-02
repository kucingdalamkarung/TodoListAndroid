package com.idts.mynotes.activity.main;

import com.idts.mynotes.api.ApiClient;
import com.idts.mynotes.api.ApiInterface;
import com.idts.mynotes.model.Note;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainPresenter {

    private MainView view;

    public MainPresenter(MainView mainView) {
        this.view = mainView;
    }

    void getData() {
        view.showDialog();

        ApiInterface apiInterface = ApiClient.getApiClinet().create(ApiInterface.class);
        Call<List<Note>> call = apiInterface.getNotes();
        call.enqueue(new Callback<List<Note>>() {
            @Override
            public void onResponse(Call<List<Note>> call, Response<List<Note>> response) {
                view.hideDialog();
                if(response.isSuccessful() && response.body() != null) {
                    view.onGetResult(response.body());
                }
            }

            @Override
            public void onFailure(Call<List<Note>> call, Throwable t) {
                view.hideDialog();
                view.onErrorLoading(t.getLocalizedMessage());
            }
        });
    }
}
