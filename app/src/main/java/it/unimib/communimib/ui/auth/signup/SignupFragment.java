package it.unimib.communimib.ui.auth.signup;

import static it.unimib.communimib.util.Constants.CONFIRM_PASSWORD_ERROR;
import static it.unimib.communimib.util.Constants.EMAIL_ERROR;
import static it.unimib.communimib.util.Constants.NAME_ERROR;
import static it.unimib.communimib.util.Constants.PASSWORD_ERROR;
import static it.unimib.communimib.util.Constants.SURNAME_ERROR;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentSignupBinding;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.NavigationHelper;
import it.unimib.communimib.util.Validation;

public class SignupFragment extends Fragment {

    private FragmentSignupBinding fragmentSignupBinding;
    private SignupViewModel signupViewModel;

    private String emailError = "";
    private String passwordError = "";
    private String confirmPasswordError = "";
    private String nameError = "";
    private String surnameError = "";

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signupViewModel = new ViewModelProvider(requireActivity(), new SignupViewModelFactory(this.getContext())).get(SignupViewModel.class);

        if (savedInstanceState != null){
            emailError = savedInstanceState.getString(EMAIL_ERROR);
            passwordError = savedInstanceState.getString(PASSWORD_ERROR);
            confirmPasswordError = savedInstanceState.getString(CONFIRM_PASSWORD_ERROR);
            nameError = savedInstanceState.getString(NAME_ERROR);
            surnameError = savedInstanceState.getString(SURNAME_ERROR);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSignupBinding = FragmentSignupBinding.inflate(inflater, container, false);
        return fragmentSignupBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        savedInstanceState = new Bundle();
        super.onViewCreated(view, savedInstanceState);

        fragmentSignupBinding.fragmentSignupTextViewEmailError.setText(emailError);
        fragmentSignupBinding.fragmentSignupTextViewNameError.setText(nameError);
        fragmentSignupBinding.fragmentSignupTextViewSurnameError.setText(surnameError);
        fragmentSignupBinding.fragmentSignupTextViewPasswordError.setText(passwordError);
        fragmentSignupBinding.fragmentSignupTextViewConfirmPasswordError.setText(confirmPasswordError);

        fragmentSignupBinding.fragmentSignupButtonLogin.setOnClickListener(v ->
            getParentFragmentManager().popBackStackImmediate()
        );

        //Controllo in realtime su testo email
        fragmentSignupBinding.fragmentSignupEditTextEmailAddress.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String email = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextEmailAddress.getText());
                String result = Validation.checkEmail(email);
                if (!result.equals("ok")){
                    emailError = getString(ErrorMapper.getInstance().getErrorMessage(result));
                    fragmentSignupBinding.fragmentSignupTextViewEmailError.setText(emailError);
                }
            }
        });

        //Controllo in realtime sul nome
        fragmentSignupBinding.fragmentSignupEditTextName.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String name = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextName.getText());
                String result = Validation.checkField(name);
                if (!result.equals("ok")){
                    nameError = getString(ErrorMapper.getInstance().getErrorMessage(result));
                    fragmentSignupBinding.fragmentSignupTextViewNameError.setText(nameError);
                }
            }
        });

        //Controllo in realtime sul congnome
        fragmentSignupBinding.fragmentSignupEditTextSurname.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String surname = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextSurname.getText());
                String result = Validation.checkField(surname);
                if (!result.equals("ok")){
                    surnameError = getString(ErrorMapper.getInstance().getErrorMessage(result));
                    fragmentSignupBinding.fragmentSignupTextViewSurnameError.setText(surnameError);
                }
            }
        });

        //Controllo in realtime sulla password
        fragmentSignupBinding.fragmentSignupEditTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextPassword.getText());
                String result = Validation.checkPassword(password);
                if (!result.equals("ok")){
                    passwordError = getString(ErrorMapper.getInstance().getErrorMessage(result));
                    fragmentSignupBinding.fragmentSignupTextViewPasswordError.setText(passwordError);
                }
            }
        });

        //Controllo in realtime sulla conferma della password
        fragmentSignupBinding.fragmentSignupEditTextConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextPassword.getText());
                String confirmPassword = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextConfirmPassword.getText());
                String result = Validation.checkConfirmPassword(confirmPassword, password);
                if (!result.equals("ok")){
                    confirmPasswordError = getString(ErrorMapper.getInstance().getErrorMessage(result));
                    fragmentSignupBinding.fragmentSignupTextViewConfirmPasswordError.setText(confirmPasswordError);
                }
            }
        });

        //Gestione del pulsante di registrazione
        fragmentSignupBinding.fragmentSignupButtonSignup.setOnClickListener(v -> {

            String email = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextEmailAddress.getText());
            String password = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextPassword.getText());
            String confirmPassword = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextConfirmPassword.getText());
            String name = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextName.getText());
            String surname = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextSurname.getText());

            signupViewModel.signUp(email, password, confirmPassword, name, surname);
        });

        //Gestione del risultato della registrazione
        signupViewModel.getSignUpResult().observe(getViewLifecycleOwner(), result -> {

            this.getView().clearFocus();

            if (result.isSuccessful()) {
                NavigationHelper.navigateTo(requireActivity(), requireView(), R.id.action_signupFragment_to_emailVerificationFragment, false);
            }
            else{
                Result.Error errore = (Result.Error) result;
                Snackbar.make(
                        this.getView(),
                        ErrorMapper.getInstance().getErrorMessage(errore.getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        signupViewModel.cleanViewModel();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle saveInstanceState){
        saveInstanceState.putString(EMAIL_ERROR, emailError);
        saveInstanceState.putString(PASSWORD_ERROR, passwordError);
        saveInstanceState.putString(CONFIRM_PASSWORD_ERROR, confirmPasswordError);
        saveInstanceState.putString(NAME_ERROR, nameError);
        saveInstanceState.putString(SURNAME_ERROR, surnameError);
    }
}