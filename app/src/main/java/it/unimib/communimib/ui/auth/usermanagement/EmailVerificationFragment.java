package it.unimib.communimib.ui.auth.usermanagement;

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
import it.unimib.communimib.databinding.FragmentEmailVerificationBinding;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.NavigationHelper;
import it.unimib.communimib.util.ServiceLocator;

public class EmailVerificationFragment extends Fragment {

    private FragmentEmailVerificationBinding fragmentEmailVerificationBinding;
    private EmailManagementViewModel emailManagementViewModel;

    public EmailVerificationFragment() {
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
        fragmentEmailVerificationBinding = FragmentEmailVerificationBinding.inflate(inflater, container, false);
        return fragmentEmailVerificationBinding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        emailManagementViewModel.getEmailVerificationResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                NavigationHelper.navigateTo(requireActivity(), requireView(), R.id.action_emailVerificationFragment_to_mainActivity, true);
            }
            else{
                Snackbar.make(requireView(), ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        emailManagementViewModel.getEmailVerificationSendingResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Snackbar.make(requireView(), R.string.email_sent, BaseTransientBottomBar.LENGTH_SHORT).show();
            }
            else{
                Snackbar.make(requireView(), ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()), BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        fragmentEmailVerificationBinding.fragmentEmailVerificationButtonNewMail.setOnClickListener(v -> emailManagementViewModel.sendEmailVerification());

        emailManagementViewModel.sendEmailVerification();
        emailManagementViewModel.startEmailPolling();

    }

    @Override
    public void onPause() {
        super.onPause();
        emailManagementViewModel.stopEmailPolling();
        emailManagementViewModel.cleanViewModel();
    }

    @Override
    public void onResume() {
        super.onResume();
        emailManagementViewModel.startEmailPolling();
    }
}