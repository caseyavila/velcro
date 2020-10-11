package ga.caseyavila.velcro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.R;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class UpcomingAdapter extends RecyclerView.Adapter<UpcomingAdapter.ViewHolder> {
    private final Context context;

    public UpcomingAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_upcoming, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UpcomingAdapter.ViewHolder holder, int position) {
        holder.title.setText(casey.getUpcoming(position).getTitle());
        holder.course.setText(casey.getUpcoming(position).getCourse());
        holder.points.setText(casey.getUpcoming(position).getPoints());
        holder.dueDate.setText(casey.getUpcoming(position).getDueDate());
    }

    @Override
    public int getItemCount() {
        return casey.getNumberOfUpcoming();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;

        MaterialTextView title;
        MaterialTextView course;
        MaterialTextView points;
        MaterialTextView dueDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cardView = itemView.findViewById(R.id.upcoming_card);

            title = itemView.findViewById(R.id.upcoming_title);
            course = itemView.findViewById(R.id.upcoming_course);
            points = itemView.findViewById(R.id.upcoming_points);
            dueDate = itemView.findViewById(R.id.upcoming_due_date);
        }
    }
}
