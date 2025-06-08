package com.example.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.finalproject.ui.theme.finalhomework;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class FinanceAdapter extends ArrayAdapter<finalhomework.FinanceRecord> {
    private final Context context;
    private final SimpleDateFormat dateFormat;

    public FinanceAdapter(@NonNull Context context, List<finalhomework.FinanceRecord> records) {
        super(context, R.layout.item_finance_record, records);
        this.context = context;
        dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.item_finance_record, parent, false);
        }

        finalhomework.FinanceRecord record = getItem(position);
        if (record != null) {
            TextView timeText = view.findViewById(R.id.record_time);
            TextView typeText = view.findViewById(R.id.record_type);
            TextView amountText = view.findViewById(R.id.record_amount);
            TextView categoryText = view.findViewById(R.id.record_category);

            timeText.setText(dateFormat.format(record.time));
            typeText.setText(record.isIncome ? "收入" : "支出");
            amountText.setText("¥" + Math.abs(record.amount) + " (" + record.amountRange + ")");
            categoryText.setText("分类：" + record.category);
        }

        return view;
    }
}