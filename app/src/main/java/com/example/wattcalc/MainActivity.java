package com.example.wattcalc;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.example.wattcalc.DatabaseHelper;
import com.example.wattcalc.BillRecord;

public class MainActivity extends AppCompatActivity {

    Spinner spinnerMonth, spinnerRebate;
    EditText editTextUnits;
    Button btnCalculate, btnSave;
    TextView textTotalCharges, textFinalCost;
    String selectedMonth;
    int unitsUsed;
    double rebatePercent;
    double totalCharges;
    double finalCost;

    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("WattCalc");
        setSupportActionBar(toolbar);

        // Hubungkan UI komponen
        spinnerMonth = findViewById(R.id.spinnerMonth);
        spinnerRebate = findViewById(R.id.spinnerRebate);
        editTextUnits = findViewById(R.id.editTextUnits);
        btnCalculate = findViewById(R.id.btnCalculate);
        btnSave = findViewById(R.id.btnSave);
        textTotalCharges = findViewById(R.id.textTotalCharges);
        textFinalCost = findViewById(R.id.textFinalCost);

        dbHelper = new DatabaseHelper(this);

        // Setup Spinner Month
        String[] months = getResources().getStringArray(R.array.months_array);
        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, months);
        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMonth.setAdapter(monthAdapter);

        // Setup Spinner Rebate
        String[] rebates = getResources().getStringArray(R.array.rebate_array);
        ArrayAdapter<String> rebateAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, rebates);
        rebateAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRebate.setAdapter(rebateAdapter);

        // Butang Calculate
        btnCalculate.setOnClickListener(v -> {
            if (validateInput()) {
                calculateCharges();
                displayResult();
            }
        });

        // Butang Save
        btnSave.setOnClickListener(v -> {
            if (totalCharges == 0) {
                Toast.makeText(this, "Please calculate charges first", Toast.LENGTH_SHORT).show();
                return;
            }

            BillRecord record = new BillRecord(
                    selectedMonth,
                    unitsUsed,
                    totalCharges,
                    rebatePercent,
                    finalCost
            );

            dbHelper.insertBill(record);
            Toast.makeText(this, "Record saved successfully", Toast.LENGTH_SHORT).show();
        });
    }

    private boolean validateInput() {
        selectedMonth = spinnerMonth.getSelectedItem().toString();
        if (selectedMonth.equals("Select Month")) {
            Toast.makeText(this, "Please select a month", Toast.LENGTH_SHORT).show();
            return false;
        }

        String unitStr = editTextUnits.getText().toString();
        if (unitStr.isEmpty()) {
            editTextUnits.setError("Please enter electricity usage in kWh");
            return false;
        }

        try {
            unitsUsed = Integer.parseInt(unitStr);
        } catch (NumberFormatException e) {
            editTextUnits.setError("Invalid number");
            return false;
        }

        String rebateStr = spinnerRebate.getSelectedItem().toString();
        if (rebateStr.equals("Select Rebate")) {
            Toast.makeText(this, "Please select a rebate", Toast.LENGTH_SHORT).show();
            return false;
        }

        rebateStr = rebateStr.replace("%", "");
        rebatePercent = Double.parseDouble(rebateStr);

        return true;
    }

    private void calculateCharges() {
        int units = unitsUsed;
        double total = 0;

        if (units <= 200) {
            total = units * 0.218;
        } else if (units <= 300) {
            int block1 = 200;
            int block2 = units - 200;
            total = (block1 * 0.218) + (block2 * 0.334);
        } else if (units <= 600) {
            int block1 = 200;
            int block2 = 100;
            int block3 = units - 300;
            total = (block1 * 0.218) + (block2 * 0.334) + (block3 * 0.516);
        } else if (units <= 900) {
            int block1 = 200;
            int block2 = 100;
            int block3 = 300;
            int block4 = units - 600;
            total = (block1 * 0.218) + (block2 * 0.334) + (block3 * 0.516) + (block4 * 0.546);
        } else {
            int block1 = 200;
            int block2 = 100;
            int block3 = 300;
            int block4 = 300;
            int block5 = units - 900;
            total = (block1 * 0.218) + (block2 * 0.334) + (block3 * 0.516)
                    + (block4 * 0.546) + (block5 * 0.571);
        }

        totalCharges = total;
        finalCost = totalCharges - (totalCharges * (rebatePercent / 100));
    }

    private void displayResult() {
        textTotalCharges.setText(String.format("Total Charges: RM %.2f", totalCharges));
        textFinalCost.setText(String.format("Final Cost after rebate: RM %.2f", finalCost));

        textFinalCost.setAlpha(0f);
        textFinalCost.setScaleX(0.8f);
        textFinalCost.setScaleY(0.8f);
        textFinalCost.animate().alpha(1f).scaleX(1f).scaleY(1f).setDuration(500).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menu_history) {
            startActivity(new Intent(this, HistoryActivity.class));
            return true;
        } else if (id == R.id.menu_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
