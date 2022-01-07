package com.addy.quantum;

public class Expense {

    //private values field
    private String _id, expense_amount, expense_date, expense_name;

    // getter setter for fields
    public void set_id(String _id){
        this._id = _id;
    }

    public String get_id(){
        return _id;
    }
    public void setExpense_amount(String expense_amount){
        this.expense_amount = expense_amount;
    }

    public String getExpense_amount(){
        return expense_amount;
    }

    public void setExpense_date(String expense_date){
        this.expense_date = expense_date;
    }

    public String getExpense_date(){
        return expense_date;
    }
    public void setExpense_name(String expense_name){
        this.expense_name = expense_name;
    }

    public String getExpense_name(){
        return expense_name;
    }

}
