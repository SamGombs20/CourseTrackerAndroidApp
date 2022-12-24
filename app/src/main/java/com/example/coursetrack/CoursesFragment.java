package com.example.coursetrack;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import java.util.ArrayList;


public class CoursesFragment extends Fragment {
    Cursor cursor;
    CourseDatabaseHelper courseDatabaseHelper;
    SQLiteDatabase database;
    ArrayList<String> names;
    ArrayList<Integer> imageId;
    public CaptionedImageAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        names = new ArrayList<>();
        imageId = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView)inflater.inflate(R.layout.recycler_view, container, false);
        adapter = new CaptionedImageAdapter(names);
        recyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter.setListener(position -> {
            Intent intent = new Intent(getContext(), CourseDetails.class);
            intent.putExtra(CourseDetails.ID, position);
            startActivity(intent);
        });
        try {
            courseDatabaseHelper = new CourseDatabaseHelper(getContext());
            database = courseDatabaseHelper.getWritableDatabase();
            cursor = database.query("Courses", new String[]{"_id","Name","imageId"}, null, null, null, null, null);
            if(cursor.getCount()==0){
                Toast.makeText(getContext(), "No entries yet", Toast.LENGTH_SHORT).show();
            }
            else{
                while(cursor.moveToNext()){
                    names.add(cursor.getString(1));

                }
            }
        }
        catch (SQLiteException e){
            throw new SQLiteException(e.getMessage());
        }
        return recyclerView;
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        cursor.close();
        database.close();

    }

}