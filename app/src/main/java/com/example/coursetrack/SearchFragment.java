package com.example.coursetrack;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    SQLiteDatabase db;
    CourseDatabaseHelper databaseHelper;
    Cursor cursor;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        ListView listCourses = view.findViewById(R.id.found);
        SearchView searchView = view.findViewById(R.id.search_view);
        searchView.clearFocus();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    databaseHelper = new CourseDatabaseHelper(getContext());
                    db = databaseHelper.getReadableDatabase();
                    cursor = db.rawQuery("SELECT _id,Name FROM Courses WHERE Description like \"%"+query+"%\";", null);
                    if(cursor.getCount()==0){
                        Toast.makeText(getContext(), "No such entries", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        while(cursor.moveToNext()){
                            SimpleCursorAdapter adapter = new SimpleCursorAdapter(getContext(), android.R.layout.simple_list_item_1, cursor, new String[]{"Name"},new int[]{android.R.id.text1});
                            listCourses.setAdapter(adapter);
                            AdapterView.OnItemClickListener itemClickListener = (adapterView, view1, i, l) -> {
                                Intent intent = new Intent(getContext(), CourseDetails2.class);
                                intent.putExtra(CourseDetails2._id, (int) l);
                                startActivity(intent);
                            };
                            listCourses.setOnItemClickListener(itemClickListener);
                        }
                    }
//                    cursor.close();
//                    db.close();
                }
                catch (SQLiteException e){
                    throw new SQLiteException(e.getMessage());
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                return false;
            }
        });
        return view;
    }

}