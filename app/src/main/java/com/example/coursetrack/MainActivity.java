package com.example.coursetrack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class MainActivity extends AppCompatActivity {
    CourseDatabaseHelper courseDatabaseHelper;
    SQLiteDatabase database;
    private class CustomAdapter extends FragmentPagerAdapter {

        public CustomAdapter(@NonNull FragmentManager fm) {
            super(fm);
        }


        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new CoursesFragment();
                case 1:
                    return new FavoritesFragment();
                case 2:
                    return new SearchFragment();
                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return getText(R.string.courseFrag);
                case 1:
                    return getText(R.string.favFrag);
                case 2:
                    return getText(R.string.search);
                default:
                    return null;
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar_main);
        setSupportActionBar(toolbar);
        ViewPager pager = findViewById(R.id.pager);
        CustomAdapter customAdapter = new CustomAdapter(getSupportFragmentManager());
        pager.setAdapter(customAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(pager);
        tabs.getTabAt(0).setIcon(R.drawable.ic_baseline_menu_book_24);
        tabs.getTabAt(1).setIcon(R.drawable.ic_baseline_favorite_border_24);
        tabs.getTabAt(2).setIcon(R.drawable.ic_baseline_search_24);
        FloatingActionButton floatingActionButton = findViewById(R.id.main_FAB);
        floatingActionButton.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            LayoutInflater inflater = getLayoutInflater();
            View view1 = inflater.inflate(R.layout.dialog, null);
            TextInputLayout nameLayout = view1.findViewById(R.id.nameInput);
            TextInputEditText nameText = view1.findViewById(R.id.nameText);
            TextInputLayout categoryInput = view1.findViewById(R.id.categoryInput);
            TextInputEditText categoryText = view1.findViewById(R.id.categoryText);
            TextInputLayout ratingInput = view1.findViewById(R.id.ratingInput);
            TextInputEditText ratingText = view1.findViewById(R.id.ratingText);
            ratingText.setFilters(new InputFilter[]{new MinMaxFilter("1", "10")});
            TextInputLayout descriptionInput = view1.findViewById(R.id.descriptionInput);
            TextInputEditText descriptionText = view1.findViewById(R.id.descriptionText);
            builder.setView(view1)
                    .setTitle(R.string.dialogTitle)
                    .setNegativeButton(R.string.cancel, (dialogInterface, i) -> {
                        dialogInterface.dismiss();
                    })
                    .setPositiveButton(R.string.add, (dialogInterface, i) -> {

                    });
            AlertDialog dialog = builder.create();
            dialog.show();
            int num = AlertDialog.BUTTON_POSITIVE;
            dialog.getButton(num).setOnClickListener(view2 -> {
                if(nameText.getText().toString().equals("")){
                    nameLayout.setError(getText(R.string.error1));
                }
                else if(categoryText.getText().toString().equals("")){
                    categoryInput.setError(getText(R.string.error1));
                }
                else if(ratingText.getText().toString().equals("")){
                    ratingInput.setError(getText(R.string.ratingError));
                }
                else if(descriptionText.getText().toString().equals("")){
                    descriptionInput.setError(getText(R.string.error2));
                }
                else{
                    String name = nameText.getText().toString();
                    String category = categoryText.getText().toString();
                    int rating = Integer.parseInt(ratingText.getText().toString());
                    String description = descriptionText.getText().toString();
                    try{
                        applyValues(name, category, rating, description);
                        dialog.dismiss();
                        Toast.makeText(this, "Course added!", Toast.LENGTH_SHORT).show();
                    }
                    catch (SQLiteException e){
                        throw new SQLiteException(e.getMessage());
                    }
                }

            });
        });
    }
    public void applyValues(String name, String category, int rating, String description){
        courseDatabaseHelper = new CourseDatabaseHelper(this);
        database = courseDatabaseHelper.getWritableDatabase();
        courseDatabaseHelper.addCourse(database, name, category, rating, description);
    }
    public void applyValues(String name, String category, int rating, String description, int imageId){
        courseDatabaseHelper = new CourseDatabaseHelper(this);
        database = courseDatabaseHelper.getWritableDatabase();
        courseDatabaseHelper.addCourse(database, name, category, rating, description, imageId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.about_option){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}