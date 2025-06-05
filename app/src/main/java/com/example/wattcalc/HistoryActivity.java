package com.example.wattcalc;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import androidx.appcompat.app.AppCompatActivity;
import com.example.wattcalc.DatabaseHelper;
import com.example.wattcalc.BillRecord;

import java.util.List;

public class HistoryActivity extends AppCompatActivity {

    ListView listViewBills;
    List<BillRecord> billList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        listViewBills = findViewById(R.id.listViewBills);

        // Dapatkan semua rekod dari DB
        DatabaseHelper dbHelper = new DatabaseHelper(this);
        billList = dbHelper.getAllBills();

        // Set adapter
        ArrayAdapter<BillRecord> adapter = new ArrayAdapter<BillRecord>(this,
                R.layout.item_bill, R.id.textMonth, billList) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textMonth = view.findViewById(R.id.textMonth);
                TextView textFinalCost = view.findViewById(R.id.textFinalCost);

                BillRecord record = billList.get(position);
                textMonth.setText(record.getMonth());
                textFinalCost.setText(String.format("Final Cost: RM %.2f", record.getFinalCost()));

                return view;
            }
        };

        listViewBills.setAdapter(adapter);

        // Bila klik senarai, boleh buka DetailActivity (optional)
        listViewBills.setOnItemClickListener((parent, view, position, id) -> {
            BillRecord selected = billList.get(position);
            Intent intent = new Intent(HistoryActivity.this, DetailActivity.class);
            intent.putExtra("record_id", selected.getId());
            startActivity(intent);
        });
    }
}
