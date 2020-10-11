package ga.caseyavila.velcro.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import ga.caseyavila.velcro.R;
import ga.caseyavila.velcro.activities.LoginActivity;

import java.util.Objects;

import static ga.caseyavila.velcro.activities.LoginActivity.sharedPreferences;

public class LogoutDialogFragment extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Objects.requireNonNull(getActivity()), R.style.AlertDialogTheme);
        builder.setMessage(R.string.logout_dialog_message)
                .setPositiveButton(R.string.yes, (dialogInterface, i) -> {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();

                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    startActivity(intent);
                    Objects.requireNonNull(getActivity()).finish();
                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dismiss());
        return builder.create();
    }
}
