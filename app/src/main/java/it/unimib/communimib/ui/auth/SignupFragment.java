package it.unimib.communimib.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentSigninBinding;
import it.unimib.communimib.databinding.FragmentSignupBinding;
import it.unimib.communimib.ui.viewmodels.SignupViewModel;
import it.unimib.communimib.ui.viewmodels.SignupViewModelFactory;
import it.unimib.communimib.util.ErrorMapper;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding fragmentSignupBinding;
    private SignupViewModel signupViewModel;

    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance() {
        SignupFragment fragment = new SignupFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signupViewModel = new ViewModelProvider(requireActivity(), new SignupViewModelFactory()).get(SignupViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSignupBinding = FragmentSignupBinding.inflate(inflater, container, false);
        return fragmentSignupBinding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        ErrorMapper errorMapper = ErrorMapper.getInstance();

        fragmentSignupBinding.fragmentSignupButtonLogin.setOnClickListener(v ->{
            getParentFragmentManager().popBackStackImmediate();
        });

        //Listeners on fields

        fragmentSignupBinding.fragmentSignupEditTextEmailAddress.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String email = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextEmailAddress.getText());
                String result = signupViewModel.checkEmail(email);

                if (result.equals("ok"))
                    fragmentSignupBinding.fragmentSignupTextViewEmailError.setText("");
                else{
                    fragmentSignupBinding.fragmentSignupTextViewEmailError.setText(getString(errorMapper.getErrorMessage(result)));
                }


            }
        });

        fragmentSignupBinding.fragmentSignupEditTextName.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String name = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextName.getText());
                String result = signupViewModel.checkField(name);

                if (result.equals("ok"))
                    fragmentSignupBinding.fragmentSignupTextViewNameError.setText("");
                else
                    fragmentSignupBinding.fragmentSignupTextViewNameError.setText(getString(errorMapper.getErrorMessage(result)));
            }
        });

        fragmentSignupBinding.fragmentSignupEditTextSurname.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String surname = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextSurname.getText());
                String result = signupViewModel.checkField(surname);

                if (result.equals("ok"))
                    fragmentSignupBinding.fragmentSignupTextViewSurnameError.setText("");
                else
                    fragmentSignupBinding.fragmentSignupTextViewSurnameError.setText(getString(errorMapper.getErrorMessage(result)));
            }
        });

        fragmentSignupBinding.fragmentSignupEditTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextPassword.getText());
                String result = signupViewModel.checkPassword(password);

                if (result.equals("ok"))
                    fragmentSignupBinding.fragmentSignupTextViewPasswordError.setText("");
                else
                    fragmentSignupBinding.fragmentSignupTextViewPasswordError.setText(getString(errorMapper.getErrorMessage(result)));
            }
        });

        fragmentSignupBinding.fragmentSignupEditTextConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextPassword.getText());
                String confirmPassword = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextConfirmPassword.getText());
                String result = signupViewModel.checkConfirmPassword(confirmPassword, password);

                if (result.equals("ok"))
                    fragmentSignupBinding.fragmentSignupTextViewConfirmPasswordError.setText("");
                else
                    fragmentSignupBinding.fragmentSignupTextViewConfirmPasswordError.setText(getString(errorMapper.getErrorMessage(result)));
            }
        });
    }
}