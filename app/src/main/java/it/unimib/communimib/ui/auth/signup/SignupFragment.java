package it.unimib.communimib.ui.auth.signup;

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

public class SignupFragment extends Fragment {

    private FragmentSignupBinding fragmentSignupBinding;
    private SignupViewModel signupViewModel;

    public SignupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        signupViewModel = new ViewModelProvider(requireActivity(), new SignupViewModelFactory(this.getContext())).get(SignupViewModel.class);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        fragmentSignupBinding = FragmentSignupBinding.inflate(inflater, container, false);
        return fragmentSignupBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState){
        super.onViewCreated(view, savedInstanceState);

        fragmentSignupBinding.fragmentSignupButtonLogin.setOnClickListener(v ->
            getParentFragmentManager().popBackStackImmediate()
        );

        //Controllo in realtime su testo email
        fragmentSignupBinding.fragmentSignupEditTextEmailAddress.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String email = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextEmailAddress.getText());
                String result = signupViewModel.checkEmail(email);
                checkAndPostResult(result, fragmentSignupBinding.fragmentSignupTextViewEmailError);
            }
        });

        //Controllo in realtime sul nome
        fragmentSignupBinding.fragmentSignupEditTextName.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String name = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextName.getText());
                String result = signupViewModel.checkField(name);
                checkAndPostResult(result, fragmentSignupBinding.fragmentSignupTextViewNameError);
            }
        });

        //Controllo in realtime sul congome
        fragmentSignupBinding.fragmentSignupEditTextSurname.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String surname = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextSurname.getText());
                String result = signupViewModel.checkField(surname);
                checkAndPostResult(result, fragmentSignupBinding.fragmentSignupTextViewSurnameError);
            }
        });

        //Controllo in realtime sulla password
        fragmentSignupBinding.fragmentSignupEditTextPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextPassword.getText());
                String result = signupViewModel.checkPassword(password);
                checkAndPostResult(result, fragmentSignupBinding.fragmentSignupTextViewPasswordError);
            }
        });

        //Controllo in realtime sulla conferma della password
        fragmentSignupBinding.fragmentSignupEditTextConfirmPassword.setOnFocusChangeListener((v, hasFocus) -> {
            if(!hasFocus){
                String password = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextPassword.getText());
                String confirmPassword = String.valueOf(fragmentSignupBinding.fragmentSignupEditTextConfirmPassword.getText());
                String result = signupViewModel.checkConfirmPassword(confirmPassword, password);
                checkAndPostResult(result, fragmentSignupBinding.fragmentSignupTextViewConfirmPasswordError);
            }
        });

        //Gestione del pulsante di registrazione
        fragmentSignupBinding.signUpButton.setOnClickListener(v -> {

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
                navigateTo(R.id.action_signupFragment_to_emailVerificationFragment, false);
            }
            else{
                Result.Error errore = (Result.Error) result;
                Snackbar.make(
                        this.getView(),
                        ErrorMapper.getInstance().getErrorMessage(errore.getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });
    }

    private void navigateTo(int destination, boolean finishActivity){
        Navigation.findNavController(requireView()).navigate(destination);
        if(finishActivity)
            requireActivity().finish();
    }

    private void checkAndPostResult(String result, TextView errorMessage) {
        if (result.equals("ok"))
            errorMessage.setText("");
        else
            errorMessage.setText(getString(ErrorMapper.getInstance().getErrorMessage(result)));
    }

    @Override
    public void onDestroyView(){
        super.onDestroyView();
        signupViewModel.cleanViewModel();
    }
}