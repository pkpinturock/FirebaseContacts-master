package com.gagandeep.nuvococontacts;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static com.gagandeep.nuvococontacts.FavouriteContract.Favourite.COLUMN_NAME_TITLE;
import static com.gagandeep.nuvococontacts.HelperClass.initiateCallTo;
import static com.gagandeep.nuvococontacts.HelperClass.sendMailTo;
import static com.gagandeep.nuvococontacts.HelperClass.sendMessageTo;
import static com.gagandeep.nuvococontacts.HelperClass.sendWhatsAppMessageTo;
import static com.gagandeep.nuvococontacts.LoginActivity.isAdmin;

public class UserInfoActivity extends AppCompatActivity {
    String firstName, designation, department, email_1, email_2, phone_1, phone_2, phone_3, location, profileUri, lastName, desknumber, employeeid, emergencyNumber, sapid;
    LinearLayout phoneLayout, messageLayout, whatsappLayout, emailLayout, phoneno_1Layout, phoneno_2Layout, phoneno_3Layout, email_1Layout, email_2Layout, whatsapp_1Layout, whatsapp_2Layout, whatsapp_3Layout, emergencyPhoneLayout, emergencyWhatsAppLayout;
    TextView phoneno_1TextView, phoneno_2TextView, phoneno_3TextView, email_1TextView, email_2TextView, whatsapp_1TextView, whatsapp_2TextView, whatsapp_3TextView, emergencyPhoneTextView, emergencyWhatsAppTextView, locationTextView, sapIdTextView, deskNumberTextView, employeeIdTextView, departmentTextView, designationTextView;
    ImageView profileImageView;

    //Database vriable
    SQLiteDatabase db;
    FavouriteDbHelper mDbHelper;
    List<FavouriteItem> itemIds;
    String[] projection = {
            BaseColumns._ID,
            COLUMN_NAME_TITLE,
            FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE
    };

    String selection = COLUMN_NAME_TITLE + " = ?";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            firstName = extras.getString("firstname");
            designation = extras.getString("designation");
            location = extras.getString("location");
            department = extras.getString("department");
            phone_1 = extras.getString("phoneno_1");
            phone_2 = extras.getString("phoneno_2");
            phone_3 = extras.getString("phoneno_3");
            email_1 = extras.getString("email_1");
            email_2 = extras.getString("email_2");
            profileUri = extras.getString("profileuri");
            employeeid = extras.getString("employeeid");
            desknumber = extras.getString("desknumber");
            lastName = extras.getString("lastname");
            sapid = extras.getString("sapid");
            emergencyNumber = extras.getString("emergencynumber");
            setTitle(firstName + " " + lastName);
        }
        findViews();
        onClickListeners();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    void findViews() {
        phoneLayout = findViewById(R.id.phoneLayout);
        messageLayout = findViewById(R.id.messageLayout);
        whatsappLayout = findViewById(R.id.whatsappLayout);
        emailLayout = findViewById(R.id.emailLayout);
        phoneno_1Layout = findViewById(R.id.phoneno_1Layout);
        phoneno_2Layout = findViewById(R.id.phoneno_2Layout);
        phoneno_3Layout = findViewById(R.id.phoneno_3Layout);
        email_1Layout = findViewById(R.id.email_1Layout);
        email_2Layout = findViewById(R.id.email_2Layout);
        whatsapp_1Layout = findViewById(R.id.whatsapp_1Layout);
        whatsapp_2Layout = findViewById(R.id.whatsapp_2Layout);
        whatsapp_3Layout = findViewById(R.id.whatsapp_3Layout);
        phoneno_1TextView = findViewById(R.id.phoneno_1TextView);
        phoneno_2TextView = findViewById(R.id.phoneno_2TextView);
        phoneno_3TextView = findViewById(R.id.phoneno_3TextView);
        email_1TextView = findViewById(R.id.email_1TextView);
        email_2TextView = findViewById(R.id.email_2TextView);
        whatsapp_1TextView = findViewById(R.id.whatsapp_1TextView);
        whatsapp_2TextView = findViewById(R.id.whatsapp_2TextView);
        whatsapp_3TextView = findViewById(R.id.whatsapp_3TextView);
        emergencyPhoneLayout = findViewById(R.id.emergencyPhoneLayout);
        emergencyPhoneTextView = findViewById(R.id.emergencyPhoneTextView);
        emergencyWhatsAppLayout = findViewById(R.id.emergencyWhatsAppLayout);
        emergencyWhatsAppTextView = findViewById(R.id.emergencyWhatsAppTextView);
        locationTextView = findViewById(R.id.locationTextView);
        departmentTextView = findViewById(R.id.departmentTextView);
        designationTextView = findViewById(R.id.designationTextView);
        deskNumberTextView = findViewById(R.id.deskNumberTextView);
        sapIdTextView = findViewById(R.id.sapIdTextView);
        employeeIdTextView = findViewById(R.id.employeeIdTextView);


        profileImageView = findViewById(R.id.profileImageView);

        mDbHelper = new FavouriteDbHelper(this);
        itemIds = new ArrayList<>();

        phoneno_1TextView.setText(phone_1);
        email_1TextView.setText(email_1);
        locationTextView.append(location);
        designationTextView.append("" + designation);
        departmentTextView.append("" + department);
        sapIdTextView.append("" + sapid);
        employeeIdTextView.append("" + employeeid);

        whatsapp_1TextView.setText("WhatsApp to +91" + phone_1);

        if (isAdmin) {
            emergencyPhoneLayout.setVisibility(View.VISIBLE);
            emergencyPhoneTextView.setText("Emergency Call to " + emergencyNumber);
            emergencyWhatsAppLayout.setVisibility(View.VISIBLE);
            emergencyWhatsAppTextView.setText("Emergency text to +91" + emergencyNumber);
        }

        if (TextUtils.isEmpty(phone_2)) {
            phoneno_2Layout.setVisibility(View.GONE);
            whatsapp_2Layout.setVisibility(View.GONE);
        } else {
            phoneno_2TextView.setText(phone_2);
            whatsapp_2TextView.setText("WhatsApp to +91" + phone_2);
        }

        if (TextUtils.isEmpty(phone_3)) {
            phoneno_3Layout.setVisibility(View.GONE);
            whatsapp_3Layout.setVisibility(View.GONE);
        } else {
            phoneno_3TextView.setText(phone_3);
            whatsapp_3TextView.setText("WhatsApp to +91" + phone_3);
        }

        if (TextUtils.isEmpty(email_2))
            email_2Layout.setVisibility(View.GONE);
        else
            email_2TextView.setText(email_2);

        if (!TextUtils.isEmpty(profileUri)) {
            Picasso.get().load(profileUri).error(R.drawable.bg_placeholder).into(profileImageView);

        }
    }



    void onClickListeners() {
        phoneLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateCallTo(phone_1, UserInfoActivity.this);
            }
        });

        messageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sendMessageTo(phone_1, UserInfoActivity.this);
            }
        });

        whatsappLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                whatsappFunction(v);
            }
        });

        emailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                emailFunction(v);
            }
        });
    }

    public void emailFunction(View view) {
        switch (view.getId()) {
            case R.id.email_1Layout:
                sendMailTo(email_1, UserInfoActivity.this);
                break;
            case R.id.email_2Layout:
                sendMailTo(email_2, UserInfoActivity.this);
                break;
            case R.id.emailLayout:
                sendMailTo(email_1, UserInfoActivity.this);
                break;
        }



    }

    public void callFunction(View view) {
        switch (view.getId()) {
            case R.id.phoneno_1Layout:
                initiateCallTo(phone_1, UserInfoActivity.this);
                break;
            case R.id.phoneno_2Layout:
                initiateCallTo(phone_2, UserInfoActivity.this);
                break;
            case R.id.phoneno_3Layout:
                initiateCallTo(phone_3, UserInfoActivity.this);
                break;
            case R.id.emergencyPhoneLayout:
                initiateCallTo(emergencyNumber, UserInfoActivity.this);
                break;
        }
    }

    public void messageFunction(View view) {
        switch (view.getId()) {
            case R.id.message_1ImageView:
                sendMessageTo(phone_1, UserInfoActivity.this);
                break;
            case R.id.message_2ImageView:
                sendMessageTo(phone_2, UserInfoActivity.this);
                break;
            case R.id.message_3ImageView:
                sendMessageTo(phone_3, UserInfoActivity.this);
                break;
            case R.id.emergencyPhoneImageView:
                sendMessageTo(emergencyNumber, UserInfoActivity.this);
                break;
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
                FavouriteContract.Favourite.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                null               // The sort order
        );
        itemIds.clear();

        while (cursor.moveToNext()) {
            int itemId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FavouriteContract.Favourite._ID));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_TITLE));
            String itemPhone = cursor.getString(cursor.getColumnIndexOrThrow(FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE));
            itemIds.add(new FavouriteItem(itemId, itemName, itemPhone));
        }
        cursor.close();
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            case R.id.action_favourite:
                int counter = 0;
                if (itemIds.isEmpty())
                    addToFavourites(item);
                for (int i = 0; i < itemIds.size(); i++) {
                    if (itemIds.get(i).getName().contains(firstName))
                        counter = 1;
                }

                if (counter == 1) {
                    deleteFavourites(item);
                    counter = 0;
                } else
                    addToFavourites(item);

                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return super.onOptionsItemSelected(item);
    }


    private void deleteFavourites(MenuItem item) {
// Define 'where' part of query.
        String selection = COLUMN_NAME_TITLE + " LIKE ?";
// Specify arguments in placeholder order.
        String[] selectionArgs = {firstName};
// Issue SQL statement.
        int deletedRows = db.delete(FavouriteContract.Favourite.TABLE_NAME, selection, selectionArgs);
        Toast.makeText(this, "Removed", Toast.LENGTH_SHORT).show();
        item.setIcon(R.drawable.ic_star);
    }

    private void addToFavourites(MenuItem item) {
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME_TITLE, firstName);
        values.put(FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE, phone_1);

// Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(FavouriteContract.Favourite.TABLE_NAME, null, values);
        item.setIcon(ContextCompat.getDrawable(this, R.drawable.ic_star_filled));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_user_info, menu);    // Define a projection that specifies which columns from the database
// you will actually use after this query.
        String[] selectionArgs = {firstName};

        String whereNotNull = COLUMN_NAME_TITLE + "= ?";
        String whereNull = COLUMN_NAME_TITLE + " IS NULL";
        String[] whereArgs = {firstName};


        // How you want the results sorted in the resulting Cursor
        String sortOrder =
                FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE + " DESC";

        db = mDbHelper.getReadableDatabase();
        Cursor cursor = whereArgs == null
                ? db.query(
                FavouriteContract.Favourite.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                whereNull,              // The columns for the WHERE clause
                whereArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder)              // The sort order
                : db.query(
                FavouriteContract.Favourite.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get all)
                whereNotNull,              // The columns for the WHERE clause
                whereArgs,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder
        );


        while (cursor.moveToNext()) {
            int itemId = cursor.getInt(
                    cursor.getColumnIndexOrThrow(FavouriteContract.Favourite._ID));
            String itemName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_NAME_TITLE));
            String itemPhone = cursor.getString(cursor.getColumnIndexOrThrow(FavouriteContract.Favourite.COLUMN_NAME_SUBTITLE));
            itemIds.add(new FavouriteItem(itemId, itemName, itemPhone));
        }
        cursor.close();
        for (int i = 0; i < itemIds.size(); i++) {
            if (itemIds.get(i).getName().contains(firstName)) {
                menu.getItem(0).setIcon(R.drawable.ic_star_filled);
                break;
            }
        }
        return super.onCreateOptionsMenu(menu);
    }


    public void whatsappFunction(View view) {

        String smsNumber = ""; //without '+'

        switch (view.getId()) {
            case R.id.whatsapp_1Layout:
                smsNumber = phone_1;
                break;
            case R.id.whatsapp_2Layout:
                smsNumber = phone_2;
                break;
            case R.id.whatsapp_3Layout:
                smsNumber = phone_3;
                break;
            case R.id.whatsappLayout:
                smsNumber = phone_1;
                break;
            case R.id.emergencyWhatsAppLayout:
                smsNumber = phone_1;
                break;
        }
        sendWhatsAppMessageTo(smsNumber, UserInfoActivity.this);
    }
}
