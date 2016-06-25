package com.example.coupondunia.treebo;


import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.coupondunia.treebo.database.DatabaseHelper;
import com.example.coupondunia.treebo.database.TreeboOfflineDatabase;

import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesListAdapter.ViewHolder> {


    private final Context context;
    private List<NotesModel> notesList;

    public NotesListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<NotesModel> notesList) {
        this.notesList = notesList;
        if (notesList != null) {
            notifyDataSetChanged();
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final NotesModel notes = notesList.get(position);
        holder.itemView.setTag(notes);
        holder.notes.setText(notes.notes);
        holder.title.setText(notes.title);
        holder.delIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SQLiteDatabase db = DatabaseHelper.getInstance().getDatabseManipulater();
                db.delete(TreeboOfflineDatabase.UserNotesTable.TABLE_NAME,
                        TreeboOfflineDatabase.UserNotesTable.ENTRY_ID + "=?", new String[]{String.valueOf((notes.id))});
                notesList.remove(notes);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        int size = notesList != null ? notesList.size() : 0;
        return size;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView notes, title;
        ImageView delIcon;

        public ViewHolder(final View itemView) {
            super(itemView);
            notes = (TextView) itemView.findViewById(R.id.description);
            title = (TextView) itemView.findViewById(R.id.title);
            delIcon = (ImageView) itemView.findViewById(R.id.delIcon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((Activity)context).startActivityForResult(DetailedNotesActivity.getIntent(context, true, (NotesModel) itemView.getTag()), MainActivity.REQUEST_CODE);
                }
            });

        }
    }

}
