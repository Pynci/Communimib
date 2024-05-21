package it.unimib.communimib.ui.main.dashboard.newdashboardpost;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.LiveDataTestUtil;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.repository.PostRepository;
import it.unimib.communimib.repository.UserRepository;

public class NewDashboardPostViewModelTest {

    private IUserRepository userRepository;
    private IPostRepository postRepository;
    private NewDashboardPostViewModel newDashboardPostViewModel;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        postRepository = mock(PostRepository.class);
        newDashboardPostViewModel = new NewDashboardPostViewModel(postRepository, userRepository);
    }

    @Test
    public void getCurrentUser() {
        User user = new User("test");
        when(userRepository.getCurrentUser()).thenReturn(user);
        assertEquals(newDashboardPostViewModel.getCurrentUser(), user);
    }

    @Test
    public void createPost() throws InterruptedException {
        User user = new User("11111", "g.vitale16@campus.unimib.it", "Giulia", "Vitale", false);
        String title = "title";
        String description = "description";
        String category = "Eventi";
        String email = "g.vitale16@campus.unimib.it";
        String link = "https://link";
        List<String> pictures = new ArrayList<>();

        doAnswer(invocation ->{
            Callback callback = invocation.getArgument(7);
            callback.onComplete(new Result.Success());
            return null;
        }).when(postRepository).createPost(
                eq(title),
                eq(description),
                eq(category),
                any(),
                eq(email),
                eq(link),
                eq(pictures),
                any());

        newDashboardPostViewModel.createPost(title, description, category, user, email, link, pictures);
        Result result = LiveDataTestUtil.getOrAwaitValue(newDashboardPostViewModel.getPostCreationResult());
        assertTrue(result instanceof Result.Success);
    }
}