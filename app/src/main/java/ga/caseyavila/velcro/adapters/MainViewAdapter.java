package ga.caseyavila.velcro.adapters;

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
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.activities.CourseActivity;
import ga.caseyavila.velcro.activities.MainActivity;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class MainViewAdapter extends RecyclerView.Adapter<MainViewAdapter.ViewHolder> {

    private final Context context;

    public MainViewAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.periodNumber.setText(String.valueOf(position + 1));  // Add one since "position" starts at 0
        holder.teacher.setText(casey.getPeriod(position).getTeacher().toUpperCase());
        holder.grade.setText(casey.getPeriod(position).getGrade());
        holder.courseName.setText(casey.getPeriod(position).getCourseName());
        holder.score.setText(casey.getPeriod(position).getScore());
        holder.cardView.setOnClickListener(view -> {
            Intent intent = new Intent(context, CourseActivity.class);
            Bundle bundle = new Bundle();
            bundle.putInt("period", position);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return casey.getNumberOfPeriods();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;
        MaterialTextView periodNumber;
        MaterialTextView teacher;
        MaterialTextView grade;
        MaterialTextView courseName;
        MaterialTextView score;


        public ViewHolder(View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.period_card);
            periodNumber = itemView.findViewById(R.id.period_number);
            teacher = itemView.findViewById(R.id.teacher);
            grade = itemView.findViewById(R.id.grade);
            courseName = itemView.findViewById(R.id.course_name);
            score = itemView.findViewById(R.id.score);
        }
    }
}
