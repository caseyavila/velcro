package ga.caseyavila.velcro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textview.MaterialTextView;

import static ga.caseyavila.velcro.User.assignmentNames;

public class AssignmentViewAdapter extends RecyclerView.Adapter<AssignmentViewAdapter.ViewHolder> {


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_assignment, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.assignmentName.setText(assignmentNames.get(position));
    }

    @Override
    public int getItemCount() {
        return assignmentNames.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView assignmentName;

        public ViewHolder(View itemView) {
            super(itemView);

            assignmentName = itemView.findViewById(R.id.assignment_name);
        }
    }
}
