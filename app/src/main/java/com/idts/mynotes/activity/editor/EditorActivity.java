package com.idts.mynotes.activity.editor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import com.idts.mynotes.R;
import com.idts.mynotes.api.ApiClient;
import com.idts.mynotes.api.ApiInterface;
import com.idts.mynotes.model.Note;
import com.thebluealliance.spectrum.SpectrumPalette;

import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditorActivity extends AppCompatActivity implements EditorView {

    EditText et_title, et_note;
    ProgressDialog progressDialog;
    SpectrumPalette palette;

    ApiInterface apiInterface;

    EditorPresenter presenter;

    int color, id;
    String title, note;

    Menu actionMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        et_title = findViewById(R.id.title);
        et_note = findViewById(R.id.note);
        palette = findViewById(R.id.palette);

        palette.setOnColorSelectedListener(
                clr -> color = clr
        );

        presenter = new EditorPresenter(this);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        title = intent.getStringExtra("title");
        note = intent.getStringExtra("title");
        color = intent.getIntExtra("color", 0);

        setDataFromIntentExtra();
    }

    private void setDataFromIntentExtra() {
        if(id != 0) {
            et_note.setText(note);
            et_title.setText(title);
            palette.setSelectedColor(color);

            getSupportActionBar().setTitle("Update Note");

            readMode();
        } else {
            palette.setSelectedColor(getResources().getColor(R.color.white));
            color = getResources().getColor(R.color.white);
            
            editMode();
        }
    }

    private void readMode() {
        et_title.setFocusableInTouchMode(false);
        et_note.setFocusableInTouchMode(false);
        palette.setEnabled(false);

        et_note.setFocusable(false);
        et_title.setFocusable(false);
    }

    private void editMode() {
        et_title.setFocusableInTouchMode(true);
        et_note.setFocusableInTouchMode(true);
        palette.setEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_editor, menu);
        actionMenu = menu;

        if(id != 0) {
            actionMenu.findItem(R.id.edit).setVisible(true);
            actionMenu.findItem(R.id.delete).setVisible(true);
            actionMenu.findItem(R.id.save).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(false);
        } else {
            actionMenu.findItem(R.id.edit).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(false);
            actionMenu.findItem(R.id.save).setVisible(true);
            actionMenu.findItem(R.id.update).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String title = et_title.getText().toString().trim();
        String note = et_note.getText().toString().trim();
        int color = this.color;

        if (item.getItemId() == R.id.save) {
            if (title.isEmpty()) {
                et_title.setError("Please enter title...");
            } else if (note.isEmpty()) {
                et_note.setError("Please enter note...");
            } else {
                presenter.saveNote(title, note, color);
            }

            return true;
        } else if(item.getItemId() == R.id.edit) {
            editMode();
            actionMenu.findItem(R.id.edit).setVisible(false);
            actionMenu.findItem(R.id.delete).setVisible(false);
            actionMenu.findItem(R.id.save).setVisible(false);
            actionMenu.findItem(R.id.update).setVisible(true);

            return true;
        } else if(item.getItemId() == R.id.update) {
            if (title.isEmpty()) {
                et_title.setError("Please enter title...");
            } else if (note.isEmpty()) {
                et_note.setError("Please enter note...");
            } else {
                presenter.updateNote(id, title, note, color);
            }

            return true;
        } else if(item.getItemId() == R.id.delete) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setTitle("Confirm !");
            alertDialog.setMessage("Are you sure ?");
            alertDialog.setNegativeButton("Yes", ((dialog, which) -> {
                dialog.dismiss();
                presenter.deleteNote(id);
            }));

            alertDialog.setPositiveButton("Cancel", ((dialog, which) -> {
                dialog.dismiss();
            }));


            alertDialog.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showProgress() {
        progressDialog.show();
    }

    @Override
    public void hideProgress() {
        progressDialog.hide();
    }

    @Override
    public void onRequestSuccess(String message) {
        Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
        setResult(RESULT_OK);
        finish();
    }

    @Override
    public void onRequestError(String message) {
        Toast.makeText(EditorActivity.this, message, Toast.LENGTH_SHORT).show();
    }
}
