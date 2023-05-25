package com.example.plogging2;

public class dataBoxItems {
    String StepCount, wasteCount, Date;

    public String getStepCount() {
        return StepCount;
    }

    public String getWasteCount() {
        return wasteCount;
    }

    public String getDate() {
        return Date;
    }

    public void setStepCount(String stepCount) {
        StepCount = stepCount;
    }

    public void setWasteCount(String wasteCount) {
        this.wasteCount = wasteCount;
    }

    public void setDate(String date) {
        Date = date;
    }

    public dataBoxItems(String date, String stepCount, String wasteCount) {
        StepCount = stepCount;
        this.wasteCount = wasteCount;
        Date = date;
    }
}
