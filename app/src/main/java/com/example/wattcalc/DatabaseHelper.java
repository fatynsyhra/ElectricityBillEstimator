package com.example.wattcalc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.wattcalc.BillRecord;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bill_db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "bills";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_MONTH = "month";
    private static final String COLUMN_UNITS = "units";
    private static final String COLUMN_TOTAL = "total_charges";
    private static final String COLUMN_REBATE = "rebate";
    private static final String COLUMN_FINAL = "final_cost";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_MONTH + " TEXT,"
                + COLUMN_UNITS + " INTEGER,"
                + COLUMN_TOTAL + " REAL,"
                + COLUMN_REBATE + " REAL,"
                + COLUMN_FINAL + " REAL"
                + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Insert bill record
    public void insertBill(BillRecord record) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_MONTH, record.getMonth());
        values.put(COLUMN_UNITS, record.getUnits());
        values.put(COLUMN_TOTAL, record.getTotalCharges());
        values.put(COLUMN_REBATE, record.getRebate());
        values.put(COLUMN_FINAL, record.getFinalCost());
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    // Get all bill records
    public List<BillRecord> getAllBills() {
        List<BillRecord> list = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY id DESC", null);

        if (cursor.moveToFirst()) {
            do {
                BillRecord record = new BillRecord();
                record.setId(cursor.getInt(0));
                record.setMonth(cursor.getString(1));
                record.setUnits(cursor.getInt(2));
                record.setTotalCharges(cursor.getDouble(3));
                record.setRebate(cursor.getDouble(4));
                record.setFinalCost(cursor.getDouble(5));
                list.add(record);
            } while (cursor.moveToNext());
        }

        cursor.close();
        db.close();
        return list;
    }
}
