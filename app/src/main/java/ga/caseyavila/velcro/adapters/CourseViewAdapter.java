package ga.caseyavila.velcro.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.R;
import lecho.lib.hellocharts.model.*;
import lecho.lib.hellocharts.view.LineChartView;

import java.util.ArrayList;
import java.util.List;

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
        if (getItemViewType(position) == COURSE_VIEW_TYPES.HEADER) {
            holder.headerTeacher.setText(casey.getTeacher(period));
            LineChartView lineChartView = holder.trendChart;

            String[] xAxisData = {"Jan", "Feb", "Mar", "Apr", "May", "June", "July", "Aug", "Sept", "Oct", "Nov", "Dec"};
            int[] yAxisData = {50, 20, 15, 30, 20, 60, 15, 40, 45, 10, 105, 18};

            List yAxisValues = new ArrayList();
            List axisValues = new ArrayList();


            Line line = new Line(yAxisValues).setColor(ContextCompat.getColor(context, R.color.colorPrimary));
            line.setFilled(true);
            line.setHasPoints(false);

            for (int i = 0; i < xAxisData.length; i++) {
                axisValues.add(i, new AxisValue(i).setLabel(xAxisData[i]));
            }

            for (int i = 0; i < yAxisData.length; i++) {
                yAxisValues.add(new PointValue(i, yAxisData[i]));
            }

            List lines = new ArrayList();
            lines.add(line);

            LineChartData data = new LineChartData();
            data.setLines(lines);

            Axis axis = new Axis();
            axis.setValues(axisValues);
            axis.setTextSize(14);
            axis.setHasLines(true);
            axis.setTextColor(ContextCompat.getColor(context, R.color.textHighEmphasis));
            axis.setLineColor(ContextCompat.getColor(context, R.color.textMediumEmphasis));
            data.setAxisXBottom(axis);

            Axis yAxis = new Axis(Axis.generateAxisFromRange(0, 110, 25));
            yAxis.setHasLines(true);
            yAxis.setTextColor(ContextCompat.getColor(context, R.color.textHighEmphasis));
            yAxis.setLineColor(ContextCompat.getColor(context, R.color.textMediumEmphasis));
            yAxis.setTextSize(14);
            data.setAxisYLeft(yAxis);

            lineChartView.setLineChartData(data);
            lineChartView.setInteractive(false);

        } else {
            //Subtract one due to the offset the header card creates
            holder.assignmentName.setText(casey.getAssignmentName(period, position - 1));
            holder.assignmentCategory.setText(casey.getAssignmentCategory(period, position - 1));
            holder.assignmentScoreEarned.setText(casey.getAssignmentScoreEarned(period, position - 1));
            holder.assignmentScorePossible.setText(casey.getAssignmentScorePossible(period, position - 1));
            holder.assignmentPercentage.setText(casey.getAssignmentPercentage(period, position - 1));
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
        MaterialTextView headerTeacher;
        LineChartView trendChart;

        public ViewHolder(View itemView) {
            super(itemView);

            assignmentName = itemView.findViewById(R.id.assignment_name);
            assignmentCategory = itemView.findViewById(R.id.assignment_category);
            assignmentScoreEarned = itemView.findViewById(R.id.assignment_score_earned);
            assignmentScorePossible = itemView.findViewById(R.id.assignment_score_possible);
            assignmentPercentage = itemView.findViewById(R.id.assignment_percentage);

            headerTeacher = itemView.findViewById(R.id.header_teacher);
            trendChart = itemView.findViewById(R.id.trend_chart);
        }
    }

    private static class COURSE_VIEW_TYPES {
        public static final int HEADER = 0;
        public static final int NORMAL = 1;
    }
}
