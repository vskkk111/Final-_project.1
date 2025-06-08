package com.example.finalproject;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.finalproject.ui.theme.finalhomework;
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

        // 通过主 Activity 静态变量获取数据
        List<finalhomework.FinanceRecord> records = finalhomework.financeRecords;

        // 关键：处理 records 为 null 的情况
        if (records == null || records.isEmpty()) {
            historyListView.setVisibility(ListView.GONE);
            noRecordsText.setVisibility(TextView.VISIBLE);
        } else {
            // 设置适配器（需自定义 FinanceAdapter）
            FinanceAdapter adapter = new FinanceAdapter(this, records);
            historyListView.setAdapter(adapter);
        }
    }
}