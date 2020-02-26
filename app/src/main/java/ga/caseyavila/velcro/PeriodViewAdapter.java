package ga.caseyavila.velcro;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;

import static ga.caseyavila.velcro.User.*;

public class PeriodViewAdapter extends RecyclerView.Adapter<PeriodViewAdapter.ViewHolder> {

    Context context;

    PeriodViewAdapter(Context ctx) {
        context = ctx;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_period, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.periodNumber.setText(String.valueOf(position + 1));  // Add one since "position" starts at 0
        holder.teacher.setText(teacherMap.get(position).toUpperCase());
        holder.grade.setText(gradeMap.get(position));
        holder.className.setText(classMap.get(position));
        holder.percentage.setText(percentageMap.get(position));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PeriodActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("period", position);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return numberOfPeriods;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;

        MaterialTextView periodNumber;
        MaterialTextView teacher;
        MaterialTextView grade;
        MaterialTextView className;
        MaterialTextView percentage;

        public ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.period_card);

            periodNumber = itemView.findViewById(R.id.period_number);
            teacher = itemView.findViewById(R.id.teacher);
            grade = itemView.findViewById(R.id.grade);
            className = itemView.findViewById(R.id.class_name);
            percentage = itemView.findViewById(R.id.percentage);
        }
    }
}
