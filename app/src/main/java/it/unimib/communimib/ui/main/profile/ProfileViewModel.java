package it.unimib.communimib.ui.main.profile;

import androidx.lifecycle.ViewModel;

import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.IReportRepository;
import it.unimib.communimib.repository.IUserRepository;

public class ProfileViewModel extends ViewModel {

    private final IUserRepository userRepository;
    private final IPostRepository postRepository;
    private final IReportRepository repository;

    public ProfileViewModel(IUserRepository userRepository, IPostRepository postRepository, IReportRepository repository) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.repository = repository;
    }


}
