package com.example.wattcalc;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wattcalc.DatabaseHelper;
import com.example.wattcalc.BillRecord;

import java.util.List;

public class DetailActivity extends AppCompatActivity {

    TextView textMonth, textUnits, textTotalCharges, textRebate, textFinalCost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        textMonth = findViewById(R.id.textMonth);
        textUnits = findViewById(R.id.textUnits);
        textTotalCharges = findViewById(R.id.textTotalCharges);
        textRebate = findViewById(R.id.textRebate);
        textFinalCost = findViewById(R.id.textFinalCost);

        int recordId = getIntent().getIntExtra("record_id", -1);
        if (recordId == -1) {
            Toast.makeText(this, "Record not found", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        DatabaseHelper dbHelper = new DatabaseHelper(this);
        List<BillRecord> allRecords = dbHelper.getAllBills();

        BillRecord selected = null;
        for (BillRecord record : allRecords) {
            if (record.getId() == recordId) {
                selected = record;
                break;
            }
        }

        if (selected != null) {
            textMonth.setText("Month: " + selected.getMonth());
            textUnits.setText("Units Used: " + selected.getUnits() + " kWh");
            textTotalCharges.setText(String.format("Total Charges: RM %.2f", selected.getTotalCharges()));
            textRebate.setText(String.format("Rebate: %.0f%%", selected.getRebate()));
            textFinalCost.setText(String.format("Final Cost: RM %.2f", selected.getFinalCost()));
        } else {
            Toast.makeText(this, "Record not found", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
}

