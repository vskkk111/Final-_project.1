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

    // 财务计分相关变量
    private int incomeScore = 0;      // 收入分数
    private int expenseScore = 0;     // 支出分数
    private int balance = 0;          // 收支平衡

    // 消费类别统计
    private final Map<String, Integer> categoryStats = new HashMap<>();
    private final String[] categories = {"饮食", "购物", "交通", "娱乐", "学习", "其他"};

    private TextView incomeScoreText;
    private TextView expenseScoreText;
    private TextView balanceText;
    private TextView analysisResultText;
    private TextView suggestionText;
    private Spinner categorySpinner;

    public final List<FinanceRecord> financeRecords = new ArrayList<>();
    public final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.CHINA);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finalhomework);

        incomeScoreText = findViewById(R.id.income_score);
        expenseScoreText = findViewById(R.id.expense_score);
        balanceText = findViewById(R.id.balance);
        analysisResultText = findViewById(R.id.analysis_result);
        suggestionText = findViewById(R.id.suggestion);
        categorySpinner = findViewById(R.id.category_spinner);


        // 初始化消费类别统计
        for (String category : categories) {
            categoryStats.put(category, 0);
        }

        // 初始化分数显示
        updateScoreDisplay();
        updateAnalysisAndSuggestion();

        // 设置消费类别下拉框
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_spinner_item, categories);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // 收入按钮事件
        Button income1Btn = findViewById(R.id.income_1);
        Button income2Btn = findViewById(R.id.income_2);
        Button income3Btn = findViewById(R.id.income_3);

        income1Btn.setOnClickListener(v -> addIncome(1, getString(R.string.income_1)));
        income2Btn.setOnClickListener(v -> addIncome(2, getString(R.string.income_2)));
        income3Btn.setOnClickListener(v -> addIncome(3, getString(R.string.income_3)));

        // 支出按钮事件
        Button expense1Btn = findViewById(R.id.expense_1);
        Button expense2Btn = findViewById(R.id.expense_2);
        Button expense3Btn = findViewById(R.id.expense_3);

        expense1Btn.setOnClickListener(v -> addExpense(1, getString(R.string.expense_1)));
        expense2Btn.setOnClickListener(v -> addExpense(2, getString(R.string.expense_2)));
        expense3Btn.setOnClickListener(v -> addExpense(3, getString(R.string.expense_3)));

        // 历史记录按钮 - 修改为跳转到独立页面
        Button historyBtn = findViewById(R.id.history_btn);
        historyBtn.setOnClickListener(v -> {
            Intent intent = new Intent(finalhomework.this, HistoryActivity.class);
            startActivity(intent);
        });
    }

    // 添加收入记录
    private void addIncome(int score, String amountRange) {
        incomeScore += score;
        int amount = getAmountFromScore(score, true);
        balance += amount;
        saveFinanceRecord(amount, amountRange, true, getSelectedCategory());
        updateScoreDisplay();
        updateAnalysisAndSuggestion();
        Toast.makeText(this, getString(R.string.income_added) + ": " + amountRange, Toast.LENGTH_SHORT).show();
    }

    // 添加支出记录
    private void addExpense(int score, String amountRange) {
        expenseScore += score;
        int amount = getAmountFromScore(score, false);
        balance -= amount;
        saveFinanceRecord(-amount, amountRange, false, getSelectedCategory());
        updateScoreDisplay();
        updateAnalysisAndSuggestion();
        Toast.makeText(this, getString(R.string.expense_added) + ": " + amountRange, Toast.LENGTH_SHORT).show();
    }

    // 根据分数获取实际金额
    private int getAmountFromScore(int score, boolean isIncome) {
        if (isIncome) {
            switch (score) {
                case 1: return 50;   // 100元以下取中间值50
                case 2: return 300;  // 100-500元取中间值300
                case 3: return 1000; // 500元以上取1000
                default: return 0;
            }
        } else {
            switch (score) {
                case 1: return 50;   // 100元以下取中间值50
                case 2: return 300;  // 100-500元取中间值300
                case 3: return 1000; // 500元以上取1000
                default: return 0;
            }
        }
    }

    // 获取选中的消费类别
    private String getSelectedCategory() {
        return categorySpinner.getSelectedItem().toString();
    }

    // 保存财务记录
    private void saveFinanceRecord(int amount, String amountRange, boolean isIncome, String category) {
        FinanceRecord record = new FinanceRecord();
        record.time = new Date();
        record.amount = amount;
        record.amountRange = amountRange;
        record.isIncome = isIncome;
        record.category = category;
        financeRecords.add(record);

        // 更新类别统计
        int current = categoryStats.get(category);
        categoryStats.put(category, current + Math.abs(amount));
    }

    // 更新分数显示
    @SuppressLint("SetTextI18n")
    private void updateScoreDisplay() {
        incomeScoreText.setText(String.valueOf(incomeScore));
        expenseScoreText.setText(String.valueOf(expenseScore));
        balanceText.setText("¥ " + balance);
    }

    // 更新分析和建议
    private void updateAnalysisAndSuggestion() {
        if (balance > 10000) {
            analysisResultText.setText(getString(R.string.analysis_good));
            suggestionText.setText(getString(R.string.suggestion_good));
        } else if (balance > 3000) {
            analysisResultText.setText(getString(R.string.analysis_ok));
            suggestionText.setText(getString(R.string.suggestion_ok));
        } else if (balance > 0) {
            analysisResultText.setText(getString(R.string.analysis_neutral));
            suggestionText.setText(getString(R.string.suggestion_neutral));
        } else if (balance > -3000) {
            analysisResultText.setText(getString(R.string.analysis_warning));
            suggestionText.setText(getString(R.string.suggestion_warning));
        } else {
            analysisResultText.setText(getString(R.string.analysis_bad));
            suggestionText.setText(getString(R.string.suggestion_bad));
        }
    }

    // 财务记录
    public class FinanceRecord {
        public Date time;
        public int amount;
        public String amountRange;
        public boolean isIncome;
        public String category;
    }
}