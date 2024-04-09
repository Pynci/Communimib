package it.unimib.communimib.ui.auth.signin;

import static it.unimib.communimib.util.Constants.EMAIL_ERROR;
import static it.unimib.communimib.util.Constants.PASSWORD_ERROR;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentSigninBinding;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.NavigationHelper;
import it.unimib.communimib.util.ServiceLocator;
import it.unimib.communimib.util.Validation;

public class SigninFragment extends Fragment {

    private FragmentSigninBinding fragmentSigninBinding;
    private SigninViewModel signinViewModel;

    private String emailError = "";
    private String passwordError = "";

    public SigninFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        signinViewModel = new ViewModelProvider(requireActivity(),
                new SigninViewModelFactory(userRepository)).get(SigninViewModel.class);

        if(savedInstanceState != null){
            emailError = savedInstanceState.getString(EMAIL_ERROR);
            passwordError = savedInstanceState.getString(PASSWORD_ERROR);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        fragmentSigninBinding = FragmentSigninBinding.inflate(inflater, container, false);
        return fragmentSigninBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        savedInstanceState = new Bundle();
        super.onViewCreated(view, savedInstanceState);

        fragmentSigninBinding.fragmentSigninTextViewEmailError.setText(emailError);
        fragmentSigninBinding.fragmentSigninTextViewPasswordError.setText(passwordError);

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
                    NavigationHelper.navigateTo(requireActivity(), requireView(), R.id.action_signinFragment_to_mainActivity, false);
                } else {
                    NavigationHelper.navigateTo(requireActivity(), requireView(), R.id.action_signinFragment_to_emailVerificationFragment, false);
                }
            } else {
                Snackbar.make(requireView(), ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });


        fragmentSigninBinding.fragmentSigninButtonSignup.setOnClickListener(v ->
                NavigationHelper.navigateTo(requireActivity(), requireView(),R.id.action_signinFragment_to_signupFragment, false));

        fragmentSigninBinding.fragmentSigninEditTextEmailAddress.setOnFocusChangeListener((v, hasFocus) -> {

            if(!hasFocus){
                String email = String.valueOf(fragmentSigninBinding.fragmentSigninEditTextEmailAddress.getText());
                String result = Validation.checkEmail(email);

                if(!result.equals("ok")){
                        emailError = getString(ErrorMapper.getInstance().getErrorMessage(result));
                        fragmentSigninBinding.fragmentSigninTextViewEmailError.setText(emailError);
                }
            }

        });

        fragmentSigninBinding.fragmentSigninEditTextPassword.setOnFocusChangeListener((v, hasFocus) -> {

            if(!hasFocus){
                String password = String.valueOf(fragmentSigninBinding.fragmentSigninEditTextPassword.getText());

                if(password.isEmpty()){
                        passwordError = getString(ErrorMapper.getInstance().getErrorMessage(ErrorMapper.EMPTY_FIELD));
                        fragmentSigninBinding.fragmentSigninTextViewPasswordError.setText(passwordError);
                }
            }

        });

        onSaveInstanceState(savedInstanceState);

        fragmentSigninBinding.fragmentSigninButtonSignin.setOnClickListener(r -> {

            String email = String.valueOf(fragmentSigninBinding.fragmentSigninEditTextEmailAddress.getText());
            String password = String.valueOf(fragmentSigninBinding.fragmentSigninEditTextPassword.getText());

            signinViewModel.signIn(email, password);

        });

        fragmentSigninBinding.fragmentSigninButtonForgottenPassword.setOnClickListener(r -> {
            if (!getParentFragmentManager().executePendingTransactions()) {
                NavigationHelper.navigateTo(requireActivity(), requireView(), R.id.action_signinFragment_to_passwordResetFragment, false);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        signinViewModel.cleanViewModel();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){
        savedInstanceState.putString(EMAIL_ERROR, emailError);
        savedInstanceState.putString(PASSWORD_ERROR, passwordError);
    }
}