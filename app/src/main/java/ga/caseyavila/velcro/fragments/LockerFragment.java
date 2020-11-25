package ga.caseyavila.velcro.fragments;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.material.textview.MaterialTextView;

import ga.caseyavila.velcro.R;

public class LockerFragment extends Fragment {

    public LockerFragment() {
        // Required empty public constructor
    }

    public static LockerFragment newInstance() {
        LockerFragment fragment = new LockerFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_locker, container, false);
    }

    @Override
    public void onStart() {
        MaterialTextView explanation = requireView().findViewById(R.id.explanation);
        explanation.setText(R.string.explanation);
        explanation.setMovementMethod(LinkMovementMethod.getInstance());
        super.onStart();
    }
}