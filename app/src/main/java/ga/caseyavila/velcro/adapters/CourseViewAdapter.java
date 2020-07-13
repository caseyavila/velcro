package ga.caseyavila.velcro.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;
import ga.caseyavila.velcro.R;
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
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
        if (getItemViewType(position) == COURSE_VIEW_TYPES.HEADER) {  //If the card is a header
            holder.headerTeacher.setText(casey.getTeacher(period));

            if (casey.hasTrends(period)) {  //If class has trends (grades posted more than once)
                LineChartView lineChartView = holder.trendChart;

                List<PointValue> values = new ArrayList<PointValue>();
                for (int i = 0; i < casey.xTrendValues(period).size(); i++) {
                    values.add(new PointValue(casey.xTrendValues(period).get(i), casey.yTrendValues(period).get(i)));
                }

                Line line = new Line(values).setColor(ContextCompat.getColor(context, R.color.colorPrimary));
                line.setFilled(true);
                line.setHasPoints(false);

                List<Line> lines = new ArrayList<Line>();
                lines.add(line);

                LineChartData data = new LineChartData();
                data.setLines(lines);

                Axis axis = new Axis(casey.dateLabels(period));
                axis.setTextSize(14);
                axis.setTypeface(ResourcesCompat.getFont(context, R.font.manrope_medium));
                axis.setHasLines(true);
                axis.setTextColor(ContextCompat.getColor(context, R.color.textHighEmphasis));
                axis.setLineColor(ContextCompat.getColor(context, R.color.textMediumEmphasis));
                data.setAxisXBottom(axis);

                Axis yAxis = new Axis();
                yAxis.setFormatter(new SimpleAxisValueFormatter(1));  //One place after decimals
                yAxis.setTextSize(14);
                yAxis.setTypeface(ResourcesCompat.getFont(context, R.font.manrope_medium));
                yAxis.setHasLines(true);
                yAxis.setTextColor(ContextCompat.getColor(context, R.color.textHighEmphasis));
                yAxis.setLineColor(ContextCompat.getColor(context, R.color.textMediumEmphasis));
                yAxis.setMaxLabelChars(4);
                data.setAxisYLeft(yAxis);

                lineChartView.setLineChartData(data);
                lineChartView.setInteractive(true);

                Viewport viewport = lineChartView.getCurrentViewport();
                viewport.top = casey.getTrendMax(period) + (float) 0.25 * casey.getTrendRange(period);  //Have 1/4 of the graph space as top padding
                viewport.bottom = casey.getTrendMin(period) - (float) 0.25 * casey.getTrendRange(period);  //Have 1/4 of the graph space as bottom padding
                lineChartView.setMaximumViewport(viewport);
            } else {
                holder.trendChart.setVisibility(View.GONE);  //Don't show graph if trends do not exist
            }

            holder.headerGrade.setText(casey.getGrade(period));
            holder.headerPercentage.setText(casey.getScore(period));
            holder.gradeUpdateDate.setText(context.getString(R.string.grade_last_updated) + casey.getGradeUpdateDate(period));

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
        MaterialTextView headerGrade;
        MaterialTextView headerPercentage;
        MaterialTextView gradeUpdateDate;
        LineChartView trendChart;

        public ViewHolder(View itemView) {
            super(itemView);

            assignmentName = itemView.findViewById(R.id.assignment_name);
            assignmentCategory = itemView.findViewById(R.id.assignment_category);
            assignmentScoreEarned = itemView.findViewById(R.id.assignment_score_earned);
            assignmentScorePossible = itemView.findViewById(R.id.assignment_score_possible);
            assignmentPercentage = itemView.findViewById(R.id.assignment_percentage);

            headerTeacher = itemView.findViewById(R.id.header_teacher);
            headerGrade = itemView.findViewById(R.id.header_grade);
            headerPercentage = itemView.findViewById(R.id.header_percentage);
            gradeUpdateDate = itemView.findViewById(R.id.grade_update_date);
            trendChart = itemView.findViewById(R.id.trend_chart);
        }
    }

    private static class COURSE_VIEW_TYPES {
        public static final int HEADER = 0;
        public static final int NORMAL = 1;
    }
}
