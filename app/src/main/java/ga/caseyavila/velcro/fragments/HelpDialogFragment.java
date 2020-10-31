package ga.caseyavila.velcro.fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

import ga.caseyavila.velcro.R;

public class HelpDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme);
        builder.setMessage(R.string.subdomain_explanation)
                .setPositiveButton("Got it", (dialogInterface, i) -> {
                    // Do nothing
                });
        return builder.create();
    }
}
