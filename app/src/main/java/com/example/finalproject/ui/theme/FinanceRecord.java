package com.example.finalproject.ui.theme;

import android.os.Parcel;
import android.os.Parcelable;
import java.util.Date;

public class FinanceRecord implements Parcelable {
    public Date time;        // 记录时间
    public int amount;       // 金额（收入为正，支出为负）
    public String amountRange; // 金额范围（如 "100元以下"）
    public boolean isIncome; // 是否为收入
    public String category;  // 消费类别（如 "饮食"）

    // 无参构造函数（必要）
    public FinanceRecord() {}

    // Parcelable 构造函数
    protected FinanceRecord(Parcel in) {
        long tmpTime = in.readLong();
        time = tmpTime != -1 ? new Date(tmpTime) : null;
        amount = in.readInt();
        amountRange = in.readString();
        isIncome = in.readByte() != 0;
        category = in.readString();
    }

    // Parcelable 常量
    public static final Creator<FinanceRecord> CREATOR = new Creator<FinanceRecord>() {
        @Override
        public FinanceRecord createFromParcel(Parcel in) {
            return new FinanceRecord(in);
        }

        @Override
        public FinanceRecord[] newArray(int size) {
            return new FinanceRecord[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(time != null ? time.getTime() : -1);
        dest.writeInt(amount);
        dest.writeString(amountRange);
        dest.writeByte((byte) (isIncome ? 1 : 0));
        dest.writeString(category);

    }

}