package ga.caseyavila.velcro.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;
import com.google.android.material.textfield.TextInputEditText;
import ga.caseyavila.velcro.Assignment;
import ga.caseyavila.velcro.Period;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.adapters.CourseViewAdapter;

import static ga.caseyavila.velcro.activities.LoginActivity.casey;

public class AssignmentEditDialogFragment extends DialogFragment {

    private Period period;
    private Assignment assignment;
    private Spinner spinner;
    private TextInputEditText scoreEarnedInput;
    private TextInputEditText scorePossibleInput;
    private final CourseViewAdapter courseAdapter;

    // Pass card to fragment so we can notify that data set has changed when applied
    public AssignmentEditDialogFragment(CourseViewAdapter adapter) {
        this.courseAdapter = adapter;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireActivity(), R.style.AlertDialogTheme);

        View view = View.inflate(requireContext(), R.layout.fragment_dialog_edit_assignment, null);
        spinner = view.findViewById(R.id.spinner);
        scoreEarnedInput = view.findViewById(R.id.score_earned_input);
        scorePossibleInput = view.findViewById(R.id.score_possible_input);

        assert getArguments() != null;
        period = casey.getPeriod(getArguments().getInt("period"));
        assignment = period.getAssignment(getArguments().getInt("assignment"));

        builder.setTitle(assignment.getName());
        builder.setPositiveButton(R.string.apply, (dialog, which) -> {
            assignment.setScoreEarned(scoreEarnedInput.getText().toString());
            assignment.setScorePossible(scorePossibleInput.getText().toString());
            assignment.setCategory(spinner.getSelectedItem().toString());
            courseAdapter.notifyDataSetChanged();
        });
        builder.setNegativeButton(R.string.cancel, (dialog, which) -> {});

        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), R.layout.support_simple_spinner_dropdown_item, period.getCategories());
        spinner.setAdapter(adapter);
        spinner.setSelection(adapter.getPosition(assignment.getCategory()));

        scoreEarnedInput.setText(assignment.getScoreEarned());
        scorePossibleInput.setText(assignment.getScorePossible());

        builder.setView(view);

        return builder.create();
    }
}
