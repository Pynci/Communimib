package it.unimib.communimib.ui.main.profile;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.IReportRepository;
import it.unimib.communimib.repository.PostRepository;
import it.unimib.communimib.repository.ReportRepository;

public class OtherUserProfileViewModelTest {

    private IPostRepository postRepository;
    private IReportRepository reportRepository;
    private OtherUserProfileViewModel otherUserProfileViewModel;
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        postRepository = mock(PostRepository.class);
        reportRepository = mock(ReportRepository.class);

        otherUserProfileViewModel = new OtherUserProfileViewModel(postRepository, reportRepository);
    }

    @Test
    public void readPostsByUser() {
    }

    @Test
    public void readReportsByUser() {
    }
}