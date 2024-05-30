package it.unimib.communimib.ui.main.profile;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.net.Uri;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import it.unimib.communimib.Callback;
import it.unimib.communimib.LiveDataTestUtil;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.IReportRepository;
import it.unimib.communimib.repository.IUserRepository;
import it.unimib.communimib.repository.PostRepository;
import it.unimib.communimib.repository.ReportRepository;
import it.unimib.communimib.repository.UserRepository;
import it.unimib.communimib.util.ErrorMapper;

public class CurrentUserProfileViewModelTest {

    private IUserRepository userRepository;
    private IPostRepository postRepository;
    private IReportRepository reportRepository;
    private CurrentUserProfileViewModel currentUserProfileViewModel;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
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
    public void readPostsByUser() throws InterruptedException {

        String uid = "123456789";
        User user = new User();
        user.setUid(uid);

        doAnswer(invocation -> {
            Callback addedcallback = invocation.getArgument(1);
            addedcallback.onComplete(new Result.Success());
            Callback changedCallback = invocation.getArgument(2);
            changedCallback.onComplete(new Result.Success());
            Callback removedCallback = invocation.getArgument(3);
            removedCallback.onComplete(new Result.Success());
            Callback cancelledCallback = invocation.getArgument(4);
            cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
            return null;
        }).when(postRepository).readPostsByUid(eq(uid), any(), any(), any(), any());

        //when(userRepository.getCurrentUser()).thenReturn(user);

        doReturn(user).when(userRepository).getCurrentUser();

        currentUserProfileViewModel.readPostsByUser();
        Result addedResult = LiveDataTestUtil.getOrAwaitValue(currentUserProfileViewModel.getAddedPostResult());
        assertTrue(addedResult instanceof Result.Success);
        Result changedResult = LiveDataTestUtil.getOrAwaitValue(currentUserProfileViewModel.getChangedPostResult());
        assertTrue(changedResult instanceof Result.Success);
        Result removedResult = LiveDataTestUtil.getOrAwaitValue(currentUserProfileViewModel.getRemovedPostResult());
        assertTrue(removedResult instanceof Result.Success);
        Result cancelledResult = LiveDataTestUtil.getOrAwaitValue(currentUserProfileViewModel.getCancelledPostResult());
        assertTrue(cancelledResult instanceof Result.Error);
        assertEquals(((Result.Error) cancelledResult).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
    }

    @Test
    public void readReportsByUser() throws InterruptedException {
        String uid = "123456789";
        User user = new User();
        user.setUid(uid);

        doAnswer(invocation -> {
            Callback addedcallback = invocation.getArgument(1);
            addedcallback.onComplete(new Result.Success());
            Callback changedCallback = invocation.getArgument(2);
            changedCallback.onComplete(new Result.Success());
            Callback removedCallback = invocation.getArgument(3);
            removedCallback.onComplete(new Result.Success());
            Callback cancelledCallback = invocation.getArgument(4);
            cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
            return null;
        }).when(reportRepository).readReportsByUid(eq(uid), any(), any(), any(), any());

        doReturn(user).when(userRepository).getCurrentUser();

        currentUserProfileViewModel.readReportsByUser();
        Result addedResult = LiveDataTestUtil.getOrAwaitValue(currentUserProfileViewModel.getAddedReportResult());
        assertTrue(addedResult instanceof Result.Success);
        Result changedResult = LiveDataTestUtil.getOrAwaitValue(currentUserProfileViewModel.getChangedReportResult());
        assertTrue(changedResult instanceof Result.Success);
        Result removedResult = LiveDataTestUtil.getOrAwaitValue(currentUserProfileViewModel.getRemovedReportResult());
        assertTrue(removedResult instanceof Result.Success);
        Result cancelledResult = LiveDataTestUtil.getOrAwaitValue(currentUserProfileViewModel.getCancelledReportResult());
        assertTrue(cancelledResult instanceof Result.Error);
        assertEquals(((Result.Error) cancelledResult).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);

    }

    @Test
    public void updateUserParameters() throws InterruptedException {
        String uid = "123456789";
        User user = new User();
        user.setUid(uid);
        user.setName("giu");
        user.setSurname("vitale");
        Uri uri = mock(Uri.class);
        String name = "giulia";
        String surname = "vitale";

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.Success());
            return null;
        }).when(userRepository).uploadPropic(eq(uri), any());

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(2);
            callback.onComplete(new Result.Success());
            return null;
        }).when(userRepository).updateUserNameAndSurname(eq(name), eq(surname), any());

        doReturn(user).when(userRepository).getCurrentUser();

        currentUserProfileViewModel.updateUserParameters(uri, name, surname);
        Result propicResult = LiveDataTestUtil.getOrAwaitValue(currentUserProfileViewModel.getUpdateUserPropicResult());
        assertTrue(propicResult instanceof Result.Success);
        Result nameSurnameResult = LiveDataTestUtil.getOrAwaitValue(currentUserProfileViewModel.getUpdateUserNameAndSurnameResult());
        assertTrue(nameSurnameResult instanceof Result.Success);

        verify(userRepository).uploadPropic(eq(uri), any());
        verify(userRepository).updateUserNameAndSurname(eq(name), eq(surname), any());
        verify(userRepository).getCurrentUser();

    }

    @Test
    public void deletePost() {
        Post post = new Post();
        post.setPid("11111");

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.Success());
            return null;
        }).when(postRepository).deletePost(eq(post), any());

        currentUserProfileViewModel.deletePost(post);

        verify(postRepository).deletePost(eq(post), any());
    }

    @Test
    public void undoDeletePost() throws InterruptedException {
        Post post = new Post();
        post.setPid("11111");

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.Success());
            return null;
        }).when(postRepository).undoDeletePost(eq(post), any());

        currentUserProfileViewModel.undoDeletePost(post);
        Result result = LiveDataTestUtil.getOrAwaitValue(currentUserProfileViewModel.getUndoDeletePostResult());
        assertTrue(result instanceof Result.Success);

        verify(postRepository).undoDeletePost(eq(post), any());
    }

    @Test
    public void logout() {
    }
}