package ga.caseyavila.velcro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.R;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;


public class CourseViewAdapter extends RecyclerView.Adapter<CourseViewAdapter.ViewHolder> {

    private Context context;
    private int period;

    public CourseViewAdapter(Context ctx, int prd) {
        context = ctx;
        period = prd;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (viewType == COURSE_VIEW_TYPES.HEADER) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_course_header, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_assignment, parent, false);
        }
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == COURSE_VIEW_TYPES.NORMAL) {
            //Subtract one due to the offset the header card creates
            holder.assignmentName.setText(casey.getAssignmentName(period, position - 1));
            holder.assignmentCategory.setText(casey.getAssignmentCategory(period, position - 1));
            holder.assignmentScoreEarned.setText(casey.getAssignmentScoreEarned(period, position - 1));
            holder.assignmentScorePossible.setText(casey.getAssignmentScorePossible(period, position - 1));
            holder.assignmentPercentage.setText(casey.getAssignmentPercentage(period, position - 1));
        } else {
            holder.headerTitle.setText(casey.getCourseName(period));
        }
    }

    @Override
    public int getItemCount() {
        return casey.getNumberOfAssignments(period) + 1;  //Add one for header card
    }

    @Override
    public int getItemViewType(int position) {
        //Set the first card to a header
        if (position == 0) {
            return COURSE_VIEW_TYPES.HEADER;
        } else {
            return COURSE_VIEW_TYPES.NORMAL;
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //Views for assignment card
        MaterialTextView assignmentName;
        MaterialTextView assignmentCategory;
        MaterialTextView assignmentScoreEarned;
        MaterialTextView assignmentScorePossible;
        MaterialTextView assignmentPercentage;

        //Views for header card
        MaterialTextView headerTitle;

        public ViewHolder(View itemView) {
            super(itemView);

            assignmentName = itemView.findViewById(R.id.assignment_name);
            assignmentCategory = itemView.findViewById(R.id.assignment_category);
            assignmentScoreEarned = itemView.findViewById(R.id.assignment_score_earned);
            assignmentScorePossible = itemView.findViewById(R.id.assignment_score_possible);
            assignmentPercentage = itemView.findViewById(R.id.assignment_percentage);

            headerTitle = itemView.findViewById(R.id.header_title);
        }
    }

    private static class COURSE_VIEW_TYPES {
        public static final int HEADER = 0;
        public static final int NORMAL = 1;
    }
}
