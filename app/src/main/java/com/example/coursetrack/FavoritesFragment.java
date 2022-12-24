package com.example.coursetrack;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;


public class FavoritesFragment extends Fragment {
    CourseDatabaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favorites, container, false);
        ListView listFav = view.findViewById(R.id.listFavorites);
        try {
            databaseHelper = new CourseDatabaseHelper(getContext());
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("Courses", new String[]{"_id", "Name"}, "Favorite=?", new String[]{Integer.toString(1)}, null, null, null);
            if (cursor.getCount() == 0) {
                Toast.makeText(getContext(), "No favorites!", Toast.LENGTH_SHORT).show();
            } else {
                while (cursor.moveToNext()) {
                    SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_1, cursor, new String[]{"Name"}, new int[]{android.R.id.text1});
                    listFav.setAdapter(adapter);
                    AdapterView.OnItemClickListener itemClickListener = (adapterView, view1, i, l) -> {
                        Intent intent = new Intent(getContext(), CourseDetails2.class);
                        intent.putExtra(CourseDetails2._id, cursor.getInt(0));
                        startActivity(intent);
                    };
                    listFav.setOnItemClickListener(itemClickListener);

                }
            }
        }
        catch (SQLiteException e){
            throw new SQLiteException(e.getMessage());
        }
        return view;
    }
}