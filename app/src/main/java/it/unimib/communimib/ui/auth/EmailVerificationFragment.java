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
import it.unimib.communimib.databinding.FragmentEmailVerificationBinding;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.ui.viewmodels.EmailManagementViewModel;
import it.unimib.communimib.ui.viewmodels.EmailManagementViewModelFactory;
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
            Log.d(this.getClass().getSimpleName(), "FUNZIONA!");
            //gestire la transizione verso la schermata successiva
        });

        emailManagementViewModel.getEmailVerificationSendingResult().observe(getViewLifecycleOwner(), result -> {
            if(!result.isSuccessful()){
                //gestire errore
            }
        });

        fragmentEmailVerificationBinding.fragmentEmailVerificationButtonNewMail.setOnClickListener(v -> {
            emailManagementViewModel.sendEmailVerification();
        });

        emailManagementViewModel.sendEmailVerification();
        emailManagementViewModel.startEmailPolling();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        emailManagementViewModel.stopEmailPolling();
    }

    private void navigateTo(int destination, boolean finishActivity){
        Navigation.findNavController(requireView()).navigate(destination);
        if(finishActivity)
            requireActivity().finish();
    }
}