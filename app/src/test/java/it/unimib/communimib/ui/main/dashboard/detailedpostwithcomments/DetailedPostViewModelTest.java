package it.unimib.communimib.ui.main.dashboard.detailedpostwithcomments;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.repository.PostRepository;
import it.unimib.communimib.repository.UserRepository;

public class DetailedPostViewModelTest {

    private IPostRepository postRepository;
    private IUserRepository userRepository;
    private DetailedPostViewModel detailedPostViewModel;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        postRepository = mock(PostRepository.class);
        userRepository = mock(UserRepository.class);

        detailedPostViewModel = new DetailedPostViewModel(postRepository, userRepository);
    }

    @Test
    public void readCommentsByPid() {

    }

    @Test
    public void createComment() {
    }
}