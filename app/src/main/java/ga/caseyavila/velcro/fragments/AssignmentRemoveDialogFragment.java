package ga.caseyavila.velcro.fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import java.util.Objects;
import ga.caseyavila.velcro.Period;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.adapters.CourseViewAdapter;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class AssignmentRemoveDialogFragment extends DialogFragment {

    private Period period;
    private int assignmentIndex;
    private final CourseViewAdapter courseAdapter;

    // Pass card to fragment so we can notify that data set has changed when applied
    public AssignmentRemoveDialogFragment(CourseViewAdapter adapter) {
        this.courseAdapter = adapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme);

        assert getArguments() != null;
        period = casey.getPeriod(getArguments().getInt("period"));
        assignmentIndex = getArguments().getInt("assignment");

        builder.setMessage(R.string.assignment_remove_message)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    period.removeAssignment(assignmentIndex);
                    courseAdapter.notifyDataSetChanged();
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dismiss());

        return builder.create();
    }
}
