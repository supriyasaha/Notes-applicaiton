package com.example.coupondunia.treebo;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.coupondunia.treebo.database.DatabaseHelper;
import com.example.coupondunia.treebo.database.TreeboOfflineDatabase;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    public RecyclerView list;
    public TextView errorText;
    public NotesListAdapter adapter;
    public ArrayList<NotesModel> listTable;
    public static final int REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listTable = new ArrayList<>();
        list = (RecyclerView) findViewById(R.id.listNotes);
        errorText = (TextView) findViewById(R.id.errorText);

        ((FloatingActionButton) findViewById(R.id.btnCreateNote)).setOnClickListener(this);

        RecyclerView.LayoutManager manager = new LinearLayoutManager(MainActivity.this);
        list.setLayoutManager(manager);
        list.addItemDecoration(
                new SimpleItemDecorator(
                        ContextCompat.getDrawable(this, R.drawable.divider_lightgray_4dp),
                        SimpleItemDecorator.VERTICAL_LIST));
        adapter = new NotesListAdapter(this);
        list.setAdapter(adapter);
        new GetNotesFromDatabase().execute();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnCreateNote:
                startActivityForResult(DetailedNotesActivity.getIntent(this, false, null), REQUEST_CODE);
                break;
        }
    }

    public class GetNotesFromDatabase extends AsyncTask<Void, Void, Void> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listTable.clear();
        }

        @Override
        protected Void doInBackground(Void... params) {
            SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();
            try {

                Cursor cursor = db.rawQuery("SELECT * FROM " + TreeboOfflineDatabase.UserNotesTable.TABLE_NAME, null);
                if (cursor != null && cursor.getCount() > 0) {
                    for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext()) {
                        NotesModel notes = new NotesModel();
                        notes.id = cursor.getInt(cursor.getColumnIndexOrThrow(TreeboOfflineDatabase.UserNotesTable.ENTRY_ID));
                        notes.notes = cursor.getString(cursor.getColumnIndexOrThrow(TreeboOfflineDatabase.UserNotesTable.NOTES));
                        notes.title = cursor.getString(cursor.getColumnIndexOrThrow(TreeboOfflineDatabase.UserNotesTable.NOTES_TITLE));
                        notes.timeStamp = cursor.getLong(cursor.getColumnIndexOrThrow(TreeboOfflineDatabase.UserNotesTable.TIMESTAMP));
                        listTable.add(notes);
                    }
                }
                if (cursor != null) {
                    cursor.close();
                }
            }
            catch (SQLiteException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            if (listTable != null && listTable.size() > 0) {
                errorText.setVisibility(View.GONE);
                list.setVisibility(View.VISIBLE);
                adapter.setData(listTable);
                adapter.notifyDataSetChanged();
            }
            else {
                errorText.setVisibility(View.VISIBLE);
                list.setVisibility(View.GONE);
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                Bundle args = data.getExtras();
                if (args != null) {
                    NotesModel notes = args.getParcelable(DetailedNotesActivity.NOTES_OBJECT);
                    boolean delTag = args.getBoolean("deleteTag", false);
                    if (!delTag) {
                        if (notes != null && listTable != null) {
                            if (!listTable.contains(notes)) {
                                listTable.add(notes);
                            }
                            else {
                                listTable.set(listTable.indexOf(notes), notes);
                            }
                            errorText.setVisibility(View.GONE);
                            list.setVisibility(View.VISIBLE);
                            adapter.setData(listTable);
                            adapter.notifyDataSetChanged();
                        }
                    }
                    else {
                        if (notes != null && listTable != null) {
                            listTable.remove(notes);
                            adapter.setData(listTable);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        }
    }

    public interface UpdateListItem {
        public void updateList(NotesModel notes);
    }
}
