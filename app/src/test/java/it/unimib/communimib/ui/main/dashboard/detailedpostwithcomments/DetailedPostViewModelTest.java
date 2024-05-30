package it.unimib.communimib.ui.main.dashboard.detailedpostwithcomments;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import it.unimib.communimib.Callback;
import it.unimib.communimib.LiveDataTestUtil;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
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
    public void readCommentsByPid() throws InterruptedException {
        String pid = "111111111";

        doAnswer(invocation -> {
            Callback addedCallback = invocation.getArgument(1);
            addedCallback.onComplete(new Result.Success());
            Callback changedCallback = invocation.getArgument(2);
            changedCallback.onComplete(new Result.Success());
            Callback removedCallback = invocation.getArgument(3);
            removedCallback.onComplete(new Result.Success());
            Callback cancelledCallback = invocation.getArgument(4);
            cancelledCallback.onComplete(new Result.Success());
            return null;
        }).when(postRepository).readCommentsByPid(eq(pid), any(), any(), any(), any());

        detailedPostViewModel.readCommentsByPid(pid);
        Result addedResult = LiveDataTestUtil.getOrAwaitValue(detailedPostViewModel.getCommentAddedReadResult());
        assertTrue(addedResult instanceof Result.Success);
        Result changedResult = LiveDataTestUtil.getOrAwaitValue(detailedPostViewModel.getCommentChangedReadResult());
        assertTrue(changedResult instanceof Result.Success);
        Result removedResult = LiveDataTestUtil.getOrAwaitValue(detailedPostViewModel.getCommentRemovedReadResult());
        assertTrue(removedResult instanceof Result.Success);
        Result cancelledResult = LiveDataTestUtil.getOrAwaitValue(detailedPostViewModel.getReadCancelledResult());
        assertTrue(cancelledResult instanceof Result.Success);

        verify(postRepository).readCommentsByPid(eq(pid), any(), any(), any(), any());
    }

    @Test
    public void createComment() throws InterruptedException {
        User user = new User();
        String pid = "1111111";
        String text = "commento";

        doReturn(user).when(userRepository).getCurrentUser();

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(3);
            callback.onComplete(new Result.Success());
            return null;
        }).when(postRepository).createComment(eq(pid), eq(user), eq(text), any());

        detailedPostViewModel.createComment(pid, text);
        Result result = LiveDataTestUtil.getOrAwaitValue(detailedPostViewModel.getCommentCreationResult());
        assertTrue(result instanceof Result.Success);

        verify(postRepository).createComment(eq(pid), eq(user), eq(text), any());
        verify(userRepository).getCurrentUser();

    }
}