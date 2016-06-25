package com.example.coupondunia.treebo;


import android.os.Parcel;
import android.os.Parcelable;

public class NotesModel implements Parcelable {

    public String title;
    public String notes;
    public long id;
    public long timeStamp;

    public NotesModel() {
    }

    protected NotesModel(Parcel in) {
        title = in.readString();
        notes = in.readString();
        id = in.readLong();
        timeStamp = in.readLong();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(notes);
        dest.writeLong(id);
        dest.writeLong(timeStamp);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotesModel> CREATOR = new Creator<NotesModel>() {
        @Override
        public NotesModel createFromParcel(Parcel in) {
            return new NotesModel(in);
        }

        @Override
        public NotesModel[] newArray(int size) {
            return new NotesModel[size];
        }
    };

    @Override
    public boolean equals(Object o) {
        return o != null && o instanceof NotesModel && this.id == ((NotesModel) o).id;
    }
}
