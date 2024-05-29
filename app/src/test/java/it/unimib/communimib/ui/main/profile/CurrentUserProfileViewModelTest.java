package it.unimib.communimib.ui.main.profile;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.IReportRepository;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.repository.PostRepository;
import it.unimib.communimib.repository.ReportRepository;
import it.unimib.communimib.repository.UserRepository;

public class CurrentUserProfileViewModelTest {

    private IUserRepository userRepository;
    private IPostRepository postRepository;
    private IReportRepository reportRepository;
    private CurrentUserProfileViewModel currentUserProfileViewModel;

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        postRepository = mock(PostRepository.class);
        reportRepository = mock(ReportRepository.class);

        currentUserProfileViewModel = new CurrentUserProfileViewModel(
                userRepository,
                postRepository,
                reportRepository);
    }

    @Test
    public void readPostsByUser() {
    }

    @Test
    public void readReportsByUser() {
    }

    @Test
    public void updateUserParameters() {
    }

    @Test
    public void deletePost() {
    }

    @Test
    public void undoDeletePost() {
    }

    @Test
    public void logout() {
    }
}