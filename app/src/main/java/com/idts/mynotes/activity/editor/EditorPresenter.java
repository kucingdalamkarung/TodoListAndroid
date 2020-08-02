package com.idts.mynotes.activity.editor;

import com.idts.mynotes.api.ApiClient;
import com.idts.mynotes.api.ApiInterface;
import com.idts.mynotes.model.Note;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorPresenter {
    private EditorView view;

    public EditorPresenter(EditorView editorView) {
        this.view = editorView;
    }

    public void saveNote(String title, String note, int color) {
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClinet().create(ApiInterface.class);
        Call<Note> call = apiInterface.saveNote(title, note, color);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                view.hideProgress();

                if(response.isSuccessful() && response.body() != null) {
                    Boolean success = response.body().getSuccess();

                    if(success) {
                        view.onRequestSuccess(response.body().getMessage());
                    } else {
                        view.onRequestError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());
            }
        });
    }

    void updateNote(int id, String title, String note, int color) {
        view.showProgress();

        ApiInterface apiInterface = ApiClient.getApiClinet().create(ApiInterface.class);
        Call<Note> call = apiInterface.updateNote(id, title, note, color);
        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                view.hideProgress();

                if(response.isSuccessful() && response.body() != null) {
                    if(response.body().getSuccess()) {
                        view.onRequestSuccess(response.body().getMessage());
                    } else {
                        view.onRequestError(response.body().getMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());
            }
        });
    }

    void deleteNote(int id) {
        view.showProgress();
        ApiInterface apiInterface = ApiClient.getApiClinet().create(ApiInterface.class);
        Call<Note> call = apiInterface.deleteNote(id);

        call.enqueue(new Callback<Note>() {
            @Override
            public void onResponse(Call<Note> call, Response<Note> response) {
                view.hideProgress();

                if(response.isSuccessful() && response.body() != null) {
                    view.onRequestSuccess(response.body().getMessage());
                } else {
                    view.onRequestError(response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<Note> call, Throwable t) {
                view.hideProgress();
                view.onRequestError(t.getLocalizedMessage());
            }
        });
    }
}
