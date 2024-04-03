package it.unimib.communimib.ui.auth;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentSigninBinding;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.ui.viewmodels.SigninViewModel;
import it.unimib.communimib.ui.viewmodels.SigninViewModelFactory;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.ServiceLocator;

public class SigninFragment extends Fragment {

    private FragmentSigninBinding fragmentSigninBinding;
    private SigninViewModel signinViewModel;

    public SigninFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        signinViewModel = new ViewModelProvider(requireActivity(),
                new SigninViewModelFactory(userRepository)).get(SigninViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentSigninBinding = FragmentSigninBinding.inflate(inflater, container, false);
        return fragmentSigninBinding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        signinViewModel.getSignInResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                signinViewModel.isEmailVerified();
            } else {
                Snackbar.make(requireView(), ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        signinViewModel.getEmailVerifiedResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                if(((Result.BooleanSuccess) result).getBoolean()){
                    navigateTo(R.id.action_signinFragment_to_mainActivity, false);
                } else {
                    navigateTo(R.id.action_signinFragment_to_emailVerificationFragment, false);
                }
            } else {
                Snackbar.make(requireView(), ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });


        fragmentSigninBinding.fragmentSigninButtonSignup.setOnClickListener(v -> navigateTo(R.id.action_signinFragment_to_signupFragment, false));

        fragmentSigninBinding.fragmentSigninEditTextEmailAddress.setOnFocusChangeListener((v, hasFocus) -> {

            if(!hasFocus){
                String email = String.valueOf(fragmentSigninBinding.fragmentSigninEditTextEmailAddress.getText());
                String result = signinViewModel.checkEmail(email);

                if(!result.equals("ok"))
                    fragmentSigninBinding.fragmentSigninTextViewEmailError.setText(getString(ErrorMapper.getInstance().getErrorMessage(result)));
                else
                    fragmentSigninBinding.fragmentSigninTextViewEmailError.setText("");
            }

        });

        fragmentSigninBinding.fragmentSigninEditTextPassword.setOnFocusChangeListener((v, hasFocus) -> {

            if(!hasFocus){
                String password = String.valueOf(fragmentSigninBinding.fragmentSigninEditTextPassword.getText());

                if(password.isEmpty())
                    fragmentSigninBinding.fragmentSigninTextViewPasswordError.setText(getString(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.EMPTY_FIELD)));
                else
                    fragmentSigninBinding.fragmentSigninTextViewPasswordError.setText("");
            }

        });

        fragmentSigninBinding.fragmentSigninButtonSignin.setOnClickListener(r -> {

            String email = String.valueOf(fragmentSigninBinding.fragmentSigninEditTextEmailAddress.getText());
            String password = String.valueOf(fragmentSigninBinding.fragmentSigninEditTextPassword.getText());

            if(!email.isEmpty() && !password.isEmpty()){
                signinViewModel.signIn(email, password);
            }
        });

        fragmentSigninBinding.fragmentSigninButtonForgottenPassword.setOnClickListener(r -> {
            navigateTo(R.id.action_signinFragment_to_passwordResetFragment, false);
        });
    }

    private void navigateTo(int destination, boolean finishActivity){
        Navigation.findNavController(requireView()).navigate(destination);
        if(finishActivity)
            requireActivity().finish();
    }
}