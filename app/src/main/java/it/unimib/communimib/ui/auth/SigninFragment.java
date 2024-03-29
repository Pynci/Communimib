package it.unimib.communimib.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentSigninBinding;

public class SigninFragment extends Fragment {

    private FragmentSigninBinding fragmentSigninBinding;

    public SigninFragment() {
        // Required empty public constructor
    }

    public static SigninFragment newInstance() {
        SigninFragment fragment = new SigninFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentSigninBinding = FragmentSigninBinding.inflate(inflater, container, false);
        return fragmentSigninBinding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

    }
}