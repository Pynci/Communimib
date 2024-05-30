package it.unimib.communimib.ui.main.profile;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
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
    public void readPostsByUser() throws InterruptedException {
        String uid = "111111111";

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
        }).when(postRepository).readPostsByUid(eq(uid), any(), any(), any(), any());

        otherUserProfileViewModel.readPostsByUser(uid);
        Result addedResult = LiveDataTestUtil.getOrAwaitValue(otherUserProfileViewModel.getAddedPostResult());
        assertTrue(addedResult instanceof Result.Success);
        Result changedResult = LiveDataTestUtil.getOrAwaitValue(otherUserProfileViewModel.getChangedPostResult());
        assertTrue(changedResult instanceof Result.Success);
        Result removedResult = LiveDataTestUtil.getOrAwaitValue(otherUserProfileViewModel.getRemovedPostResult());
        assertTrue(removedResult instanceof Result.Success);
        Result cancelledResult = LiveDataTestUtil.getOrAwaitValue(otherUserProfileViewModel.getCancelledPostResult());
        assertTrue(cancelledResult instanceof Result.Success);

        verify(postRepository).readPostsByUid(eq(uid), any(), any(), any(), any());
    }

    @Test
    public void readReportsByUser() {
    }
}