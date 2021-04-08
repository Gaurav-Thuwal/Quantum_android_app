package com.addy.quantum;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

    private Context context;

    public CustomAdapter(Context context){
        this.context = context;
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

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {

        // Views from my_row layout files
        TextView expense_name_text, expense_amount_text, expense_date_text, expense_id_text;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            expense_id_text = itemView.findViewById(R.id.expense_id_text);
            expense_name_text = itemView.findViewById(R.id.expense_name_text);
            expense_amount_text = itemView.findViewById(R.id.expense_amount_text);
            expense_date_text = itemView.findViewById(R.id.expense_date_text);
        }
    }
}
