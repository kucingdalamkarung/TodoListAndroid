package com.idts.mynotes.activity.main;

import com.idts.mynotes.model.Note;

import java.util.List;

public interface MainView {
    void showDialog();
    void hideDialog();
    void onGetResult(List<Note> notes);
    void onErrorLoading(String message);
}
