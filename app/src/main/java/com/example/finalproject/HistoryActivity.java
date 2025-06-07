package com.example.finalproject;

import android.os.Bundle;
import android.os.Parcelable;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.ui.theme.finalhomework;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private ListView historyListView;
    private TextView noRecordsText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        historyListView = findViewById(R.id.history_list_view);
        noRecordsText = findViewById(R.id.no_records_text);

        // 从 Intent 中获取数据
        ArrayList<Parcelable> records =
                getIntent().getParcelableArrayListExtra("records");

        if (records == null || records.isEmpty()) {
            historyListView.setVisibility(ListView.GONE);
            noRecordsText.setVisibility(TextView.VISIBLE);
        } else {
            // 设置适配器
            FinanceAdapter adapter = new FinanceAdapter(this, records);
            historyListView.setAdapter(adapter);
        }
    }
}