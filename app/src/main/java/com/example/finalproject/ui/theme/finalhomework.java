package com.example.finalproject.ui.theme;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.finalproject.HistoryActivity;
import com.example.finalproject.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class finalhomework extends AppCompatActivity {

    // 静态变量：财务记录列表
    public static List<FinanceRecord> financeRecords;

    // 财务计分相关变量
    private int incomeScore = 0;
    private int expenseScore = 0;
    private int balance = 0;

    // 消费类别统计
    private final Map<String, Integer> categoryStats = new HashMap<>();
    private final String[] categories = {"饮食", "购物", "交通", "娱乐", "学习", "其他"};

    private TextView incomeScoreText, expenseScoreText, balanceText, analysisResultText, suggestionText;
    private Spinner categorySpinner;
    public final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalhomework);

        // 初始化静态列表
        if (financeRecords == null) {
            financeRecords = new ArrayList<>();
        }

        // 初始化组件
        initViews();
        initCategoryStats();
        updateScoreDisplay();
        setupSpinner();
        setupButtonListeners();
    }

    private void initViews() {
        incomeScoreText = findViewById(R.id.income_score);
        expenseScoreText = findViewById(R.id.expense_score);
        balanceText = findViewById(R.id.balance);
        analysisResultText = findViewById(R.id.analysis_result);
        suggestionText = findViewById(R.id.suggestion);
        categorySpinner = findViewById(R.id.category_spinner);
    }

    private void initCategoryStats() {
        for (String category : categories) {
            categoryStats.put(category, 0);
        }
    }

    private void setupSpinner() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);
    }

    private void setupButtonListeners() {
        // 收入按钮
        Button income1Btn = findViewById(R.id.income_1);
        Button income2Btn = findViewById(R.id.income_2);
        Button income3Btn = findViewById(R.id.income_3);
        income1Btn.setOnClickListener(v -> addIncome(1, getString(R.string.income_1)));
        income2Btn.setOnClickListener(v -> addIncome(2, getString(R.string.income_2)));
        income3Btn.setOnClickListener(v -> addIncome(3, getString(R.string.income_3)));

        // 支出按钮
        Button expense1Btn = findViewById(R.id.expense_1);
        Button expense2Btn = findViewById(R.id.expense_2);
        Button expense3Btn = findViewById(R.id.expense_3);
        expense1Btn.setOnClickListener(v -> addExpense(1, getString(R.string.expense_1)));
        expense2Btn.setOnClickListener(v -> addExpense(2, getString(R.string.expense_2)));
        expense3Btn.setOnClickListener(v -> addExpense(3, getString(R.string.expense_3)));

        // 历史记录按钮
        Button historyBtn = findViewById(R.id.history_btn);
        historyBtn.setOnClickListener(v -> {
            if (financeRecords.isEmpty()) {
                Toast.makeText(this, "无历史记录", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(finalhomework.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    private void addIncome(int score, String amountRange) {
        incomeScore += score;
        int amount = getAmountFromScore(score, true);
        balance += amount;
        saveFinanceRecord(amount, amountRange, true, getSelectedCategory());
        updateUI(true);
    }

    private void addExpense(int score, String amountRange) {
        expenseScore += score;
        int amount = getAmountFromScore(score, false);
        balance -= amount;
        saveFinanceRecord(-amount, amountRange, false, getSelectedCategory());
        updateUI(false);
    }

    private int getAmountFromScore(int score, boolean isIncome) {
        switch (score) {
            case 1: return 50;
            case 2: return 300;
            case 3: return 1000;
            default: return 0;
        }
    }

    private String getSelectedCategory() {
        return categorySpinner.getSelectedItem().toString();
    }

    private void saveFinanceRecord(int amount, String amountRange, boolean isIncome, String category) {
        FinanceRecord record = new FinanceRecord();
        record.time = new Date();
        record.amount = amount;
        record.amountRange = amountRange;
        record.isIncome = isIncome;
        record.category = category;
        financeRecords.add(record);

        // 更新类别统计
        categoryStats.put(category, categoryStats.getOrDefault(category, 0) + Math.abs(amount));
    }

    private void updateUI(boolean isIncome) {
        updateScoreDisplay();
        updateAnalysisAndSuggestion();
        Toast.makeText(this, isIncome ? "收入已添加" : "支出已添加", Toast.LENGTH_SHORT).show();
    }

    private void updateScoreDisplay() {
        incomeScoreText.setText(String.valueOf(incomeScore));
        expenseScoreText.setText(String.valueOf(expenseScore));
        balanceText.setText("¥ " + balance);
    }

    private void updateAnalysisAndSuggestion() {
        if (balance > 10000) {
            analysisResultText.setText("财务状况良好，继续保持！");
            suggestionText.setText("建议增加投资或储蓄");
        } else if (balance > 3000) {
            analysisResultText.setText("收支平衡，状态稳定");
            suggestionText.setText("建议优化消费结构");
        } else if (balance > 0) {
            analysisResultText.setText("略有盈余，注意规划");
            suggestionText.setText("建议控制不必要支出");
        } else if (balance > -3000) {
            analysisResultText.setText("支出较多，需要注意");
            suggestionText.setText("建议减少非必要消费");
        } else {
            analysisResultText.setText("财务状况不佳，需警惕");
            suggestionText.setText("建议立即调整消费习惯");
        }
    }

    // 静态内部类：财务记录（必须为 static，以便在其他类中使用）
    public static class FinanceRecord {
        public Date time;
        public int amount;
        public String amountRange;
        public boolean isIncome;
        public String category;
    }
}