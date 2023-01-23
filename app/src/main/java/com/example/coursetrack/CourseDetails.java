package com.example.coursetrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.appcompat.widget.Toolbar;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class CourseDetails extends AppCompatActivity {
    public static final String ID = "";
    Cursor cursor;
    CourseDatabaseHelper databaseHelper;
    SQLiteDatabase db;
    TextView nameText, categoryText,ratingText, descriptionText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_details);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        assert actionBar!=null;
        actionBar.setDisplayHomeAsUpEnabled(true);
        int id = (Integer) getIntent().getExtras().get(ID)+1;
        AppCompatCheckBox checkBox = findViewById(R.id.checkbox_fav);
        nameText = findViewById(R.id.name_val);
        categoryText = findViewById(R.id.category_val);
        ratingText = findViewById(R.id.rating_val);
        descriptionText =  findViewById(R.id.description_val);
        try{
            databaseHelper = new CourseDatabaseHelper(this);
            db = databaseHelper.getReadableDatabase();
            cursor = db.query("Courses", new String[]{"Name", "Category", "Rating", "Description", "Favorite"},"_id=?", new String[]{Integer.toString(id)}, null, null, null);
            if(cursor.moveToFirst()){
                nameText.setText(cursor.getString(0));
                categoryText.setText(cursor.getString(1));
                ratingText.setText(cursor.getString(2));
                descriptionText.setText(cursor.getString(3));
                checkBox.setChecked(cursor.getInt(4)==1);
            }
            cursor.close();
            db.close();
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT).show();
        }
    }
    public void onFavoriteClicked(View view){
        int id = (Integer) getIntent().getExtras().get(ID)+1;
        AppCompatCheckBox checkBox = findViewById(R.id.checkbox_fav);
        boolean isChecked = checkBox.isChecked();
        try{
            databaseHelper = new CourseDatabaseHelper(this);
            db = databaseHelper.getWritableDatabase();
            ContentValues courseVal = new ContentValues();
            courseVal.put("Favorite", isChecked);
            db.update("Courses", courseVal,"_id=?",new String[]{Integer.toString(id)});
        }
        catch (SQLiteException e){
            Toast.makeText(this, "Error accessing the database", Toast.LENGTH_SHORT).show();
        }
    }

    public void onEditClicked(){
        int id = (Integer) getIntent().getExtras().get(ID)+1;
        String nameVal = nameText.getText().toString();
        String categoryVal = categoryText.getText().toString();
        String ratingVal = ratingText.getText().toString();
        String descriptionVal = descriptionText.getText().toString();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View view1 = inflater.inflate(R.layout.dialog, null);
        TextInputLayout nameLayout = view1.findViewById(R.id.nameInput);
        TextInputEditText nameText = view1.findViewById(R.id.nameText);
        TextInputLayout categoryInput = view1.findViewById(R.id.categoryInput);
        TextInputEditText categoryText = view1.findViewById(R.id.categoryText);
        TextInputLayout ratingInput = view1.findViewById(R.id.ratingInput);
        TextInputEditText ratingText = view1.findViewById(R.id.ratingText);
        TextInputLayout descriptionInput = view1.findViewById(R.id.descriptionInput);
        TextInputEditText descriptionText = view1.findViewById(R.id.descriptionText);
        nameText.setText(nameVal);
        categoryText.setText(categoryVal);
        ratingText.setText(ratingVal);
        descriptionText.setText(descriptionVal);
        builder.setView(view1)
                .setTitle(R.string.edit)
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton(R.string.save, (dialogInterface, i) -> {

                });
        AlertDialog dialog = builder.create();
        dialog.show();
        int num = AlertDialog.BUTTON_POSITIVE;
        dialog.getButton(num).setOnClickListener(view2 -> {
            if (nameText.getText().toString().equals("")) {
                nameLayout.setError(getText(R.string.error1));
            } else if (categoryText.getText().toString().equals("")) {
                categoryInput.setError(getText(R.string.error1));
            } else if (ratingText.getText().toString().equals("")) {
                ratingInput.setError(getText(R.string.ratingError));
            } else if (descriptionText.getText().toString().equals("")) {
                descriptionInput.setError(getText(R.string.error2));
            } else {
                String name = nameText.getText().toString();
                String category = categoryText.getText().toString();
                int rating = Integer.parseInt(ratingText.getText().toString());
                String description = descriptionText.getText().toString();
                try{
                    updateValues(name, category, rating, description, id);
                    dialog.dismiss();
                    Toast.makeText(this, "Course edited!", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }
                catch (SQLiteException e){
                    Toast.makeText(this, "Error updating the details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void updateValues(String name, String category, int rating, String description, int id){
        databaseHelper = new CourseDatabaseHelper(this);
        db = databaseHelper.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("Name", name);
        cv.put("Category", category);
        cv.put("Rating", rating);
        cv.put("Description", description);
        db.update("Courses", cv, "_id=?", new String[]{Integer.toString(id)});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.edit_option:
                onEditClicked();
                return true;
            default:
                return false;
        }
    }
}