package it.unimib.communimib.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentSigninBinding;
import it.unimib.communimib.ui.viewmodels.SigninViewModel;
import it.unimib.communimib.ui.viewmodels.SigninViewModelFactory;
import it.unimib.communimib.util.ErrorMapper;

public class SigninFragment extends Fragment {

    private FragmentSigninBinding fragmentSigninBinding;
    private SigninViewModel signinViewModel;

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
        signinViewModel = new ViewModelProvider(requireActivity(), new SigninViewModelFactory()).get(SigninViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentSigninBinding = FragmentSigninBinding.inflate(inflater, container, false);
        return fragmentSigninBinding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ErrorMapper errorMapper = ErrorMapper.getInstance();

        fragmentSigninBinding.fragmentSigninButtonSignup.setOnClickListener(v -> navigateTo(R.id.action_signinFragment_to_signupFragment, false));

        fragmentSigninBinding.fragmentSigninEditTextEmailAddress.setOnFocusChangeListener((v, hasFocus) -> {

            if(!hasFocus){
                String email = String.valueOf(fragmentSigninBinding.fragmentSigninEditTextEmailAddress.getText());
                String result = signinViewModel.checkEmail(email);

                if(!result.equals("ok"))
                    fragmentSigninBinding.fragmentSigninTextViewEmailError.setText(getString(errorMapper.getErrorMessage(result)));
                else
                    fragmentSigninBinding.fragmentSigninTextViewEmailError.setText("");
            }

        });

        fragmentSigninBinding.fragmentSigninEditTextPassword.setOnFocusChangeListener((v, hasFocus) -> {

            if(!hasFocus){
                String password = String.valueOf(fragmentSigninBinding.fragmentSigninEditTextPassword.getText());

                if(password.isEmpty())
                    fragmentSigninBinding.fragmentSigninTextViewPasswordError.setText(getString(errorMapper.getErrorMessage(ErrorMapper.EMPTY_FIELD)));
                else
                    fragmentSigninBinding.fragmentSigninTextViewPasswordError.setText("");
            }

        });
    }

    private void navigateTo(int destination, boolean finishActivity){
        Navigation.findNavController(requireView()).navigate(destination);
        if(finishActivity)
            requireActivity().finish();
    }
}