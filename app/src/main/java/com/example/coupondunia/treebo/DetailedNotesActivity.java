package com.example.coupondunia.treebo;


import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.coupondunia.treebo.database.DatabaseHelper;
import com.example.coupondunia.treebo.database.TreeboOfflineDatabase;

public class DetailedNotesActivity extends AppCompatActivity {


    public static final int TYPE_EDIT = 1, TYPE_NEW = 0;
    public static final String TYPE_BOOLEAN = "editboolean", NOTES_OBJECT = "notes";
    boolean type = false;
    public NotesModel notes;
    public EditText edtNotes, tvTitle;
    public TextView btnSubmit;
    public Context parentContext;

    public static Intent getIntent(Context context, boolean edit, NotesModel notes) {
        Intent intent = new Intent(context, DetailedNotesActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_BOOLEAN, (edit) ? TYPE_EDIT : TYPE_NEW);
        bundle.putParcelable(NOTES_OBJECT, notes);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_notes_activity);
        edtNotes = (EditText) findViewById(R.id.editNotes);
        tvTitle = (EditText) findViewById(R.id.noteTitle);
        btnSubmit = (TextView) findViewById(R.id.submitNotes);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateNotesInDatabase();
            }
        });

        processBundle();
        if (type && notes != null) {
            tvTitle.setText(notes.title);
            edtNotes.setText(notes.notes);
            btnSubmit.setText("EDIT");
        }
        else {
            btnSubmit.setText("DONE");
        }
    }

    public void processBundle() {
        Bundle args = getIntent().getExtras();
        if (args != null) {
            int l = args.getInt(TYPE_BOOLEAN, 0);
            if (l == TYPE_EDIT) {
                type = true;
            }
            else {
                type = false;
            }
            notes = args.getParcelable(NOTES_OBJECT);
        }
    }

    public void updateNotesInDatabase() {
        SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();
        long row = 0;
        String description = TextUtils.isEmpty(edtNotes.getText()) ? null : edtNotes.getText().toString().trim();
        String title = TextUtils.isEmpty(tvTitle.getText()) ? null : tvTitle.getText().toString().trim();
        long time = System.currentTimeMillis();


        ContentValues values = new ContentValues();
        values.put(TreeboOfflineDatabase.UserNotesTable.NOTES, description);
        values.put(TreeboOfflineDatabase.UserNotesTable.NOTES_TITLE, title);
        values.put(TreeboOfflineDatabase.UserNotesTable.TIMESTAMP, time);

        if (type && notes != null) {
            if (!TextUtils.isEmpty(title) ||!TextUtils.isEmpty(description)) {
                row = db.update(TreeboOfflineDatabase.UserNotesTable.TABLE_NAME, values, TreeboOfflineDatabase.UserNotesTable.ENTRY_ID + "=?", new String[]{String.valueOf(notes.id)});
            }
            else {
                deleteNote(notes);
                return;
            }
        }
        else {
            if (!TextUtils.isEmpty(title) ||!TextUtils.isEmpty(description)) {
                row = db.insert(TreeboOfflineDatabase.UserNotesTable.TABLE_NAME, null, values);
                if (notes == null) {
                    notes = new NotesModel();
                }
                notes.id = row;
            }
            else {
                Toast.makeText(this, "please enter a something to save ur note", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        if (row == -1) {
            Toast.makeText(this, "Please try again", Toast.LENGTH_SHORT).show();
        }
        else {
            notes.title = title;
            notes.notes = description != null ? description : null;
            notes.timeStamp = time;

            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable(NOTES_OBJECT, notes);
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    public void deleteNote(final NotesModel notes) {
        final Dialog dialog = new Dialog(this);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setContentView(R.layout.note_del_dialog);
        TextView delBtn = (TextView) dialog.findViewById(R.id.delBtn);
        delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();
                db.delete(TreeboOfflineDatabase.UserNotesTable.TABLE_NAME, TreeboOfflineDatabase.UserNotesTable.ENTRY_ID + "=?", new String[]{String.valueOf(notes.id)});
                dialog.dismiss();
                Bundle bundle = new Bundle();
                bundle.putParcelable(NOTES_OBJECT, notes);
                bundle.putBoolean("deleteTag",true);
                Intent intent = new Intent();
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        dialog.show();
    }
}
