package it.unimib.communimib.ui.auth;

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

import it.unimib.communimib.databinding.FragmentPasswordResetBinding;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.ui.viewmodels.EmailManagementViewModel;
import it.unimib.communimib.ui.viewmodels.EmailManagementViewModelFactory;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.ServiceLocator;

public class PasswordResetFragment extends Fragment {

    private FragmentPasswordResetBinding fragmentPasswordResetBinding;
    private EmailManagementViewModel emailManagementViewModel;

    public PasswordResetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        IUserRepository userRepository = ServiceLocator.getInstance().getUserRepository(requireActivity().getApplication());
        emailManagementViewModel = new ViewModelProvider(requireActivity(),
                new EmailManagementViewModelFactory(userRepository)).get(EmailManagementViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentPasswordResetBinding = FragmentPasswordResetBinding.inflate(inflater, container, false);
        return fragmentPasswordResetBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailManagementViewModel.getResetPasswordSendingResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                getParentFragmentManager().popBackStackImmediate();
            }
            else{
                Snackbar.make(requireView(), ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        fragmentPasswordResetBinding.fragmentPasswordResetButtonSendEmail.setOnClickListener(v -> {
            String email = String.valueOf(fragmentPasswordResetBinding.fragmentPasswordResetEditTextEmail.getText());
            emailManagementViewModel.resetPassword(email);
        });
    }
}