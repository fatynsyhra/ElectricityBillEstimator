package com.example.wattcalc;

public class BillRecord {
    private int id;
    private String month;
    private int units;
    private double totalCharges;
    private double rebate;
    private double finalCost;

    public BillRecord() {}

    public BillRecord(String month, int units, double totalCharges, double rebate, double finalCost) {
        this.month = month;
        this.units = units;
        this.totalCharges = totalCharges;
        this.rebate = rebate;
        this.finalCost = finalCost;
    }

    // Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getMonth() { return month; }
    public void setMonth(String month) { this.month = month; }

    public int getUnits() { return units; }
    public void setUnits(int units) { this.units = units; }

    public double getTotalCharges() { return totalCharges; }
    public void setTotalCharges(double totalCharges) { this.totalCharges = totalCharges; }

    public double getRebate() { return rebate; }
    public void setRebate(double rebate) { this.rebate = rebate; }

    public double getFinalCost() { return finalCost; }
    public void setFinalCost(double finalCost) { this.finalCost = finalCost; }
}

