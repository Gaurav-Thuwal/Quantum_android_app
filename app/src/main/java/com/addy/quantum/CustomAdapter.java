package com.addy.quantum;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private static final int ACTIVITY_REQUEST_CODE = 1;
    private Context context;
    private Activity activity;

    // Lists to store data, which will be bind with views
    private ArrayList<String> expense_id, expense_name, expense_amount, expense_date;

    public CustomAdapter(Activity activity, Context context, ArrayList expense_id, ArrayList expense_name,
                         ArrayList expense_amount, ArrayList expense_date){
        this.context = context;
        this.activity = activity;
        this.expense_id = expense_id;
        this.expense_name = expense_name;
        this.expense_amount = expense_amount;
        this.expense_date = expense_date;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.my_row, parent, false);
        return new CustomViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        // Bind data with respective views to show, using CustomViewHolder
        holder.expense_id_text.setText(expense_id.get(position));
        holder.expense_name_text.setText(expense_name.get(position));
        holder.expense_amount_text.setText(expense_amount.get(position));
        holder.expense_date_text.setText(expense_date.get(position));
        holder.main_layout.setOnClickListener(new View.OnClickListener() {  // set onclick listener, to open a intent
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UpdateActivity.class);  // Put values from current position
                intent.putExtra("id", expense_id.get(position));
                intent.putExtra("name", expense_name.get(position));
                intent.putExtra("amount", expense_amount.get(position));
                activity.startActivityForResult(intent,ACTIVITY_REQUEST_CODE);
            }
        });
    }

    @Override
    public int getItemCount() {
        // Run adapter expense_id's size time, return size of expense_id list
        return expense_id.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        // Views from my_row layout files
        TextView expense_name_text, expense_amount_text, expense_date_text, expense_id_text;
        LinearLayout main_layout;   // because we want to open update activity on click of an  row

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            expense_id_text = itemView.findViewById(R.id.expense_id_text);
            expense_name_text = itemView.findViewById(R.id.expense_name_text);
            expense_amount_text = itemView.findViewById(R.id.expense_amount_text);
            expense_date_text = itemView.findViewById(R.id.expense_date_text);
            main_layout = itemView.findViewById(R.id.main_layout);
        }
    }
}
