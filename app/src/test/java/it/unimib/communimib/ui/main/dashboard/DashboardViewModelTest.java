package it.unimib.communimib.ui.main.dashboard;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import it.unimib.communimib.Callback;
import it.unimib.communimib.LiveDataTestUtil;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.IPostRepository;
import it.unimib.communimib.repository.PostRepository;
import it.unimib.communimib.util.ErrorMapper;

public class DashboardViewModelTest {

    private IPostRepository postRepository;
    private DashboardViewModel dashboardViewModel;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Before
    public void setUp() throws Exception {
        postRepository = mock(PostRepository.class);
        dashboardViewModel = new DashboardViewModel(postRepository);
    }

    @Test
    public void readAllPosts() throws InterruptedException {
        doAnswer(invocation -> {
            Callback addedCallback = invocation.getArgument(0);
            addedCallback.onComplete(new Result.Success());
            Callback changedCallback = invocation.getArgument(1);
            changedCallback.onComplete(new Result.Success());
            Callback removedCallback = invocation.getArgument(2);
            removedCallback.onComplete(new Result.Success());
            Callback cancelledCallback = invocation.getArgument(3);
            cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
            return null;
        }).when(postRepository).readAllPosts(any(), any(), any(), any());

        dashboardViewModel.readAllPosts();
        Result addedResult = LiveDataTestUtil.getOrAwaitValue(dashboardViewModel.getPostAddedReadResult());
        assertTrue(addedResult instanceof Result.Success);
        Result changedResult = LiveDataTestUtil.getOrAwaitValue(dashboardViewModel.getPostChangedReadResult());
        assertTrue(changedResult instanceof Result.Success);
        Result removedResult = LiveDataTestUtil.getOrAwaitValue(dashboardViewModel.getPostRemovedReadResult());
        assertTrue(removedResult instanceof Result.Success);
        Result cancelledResult = LiveDataTestUtil.getOrAwaitValue(dashboardViewModel.getReadCancelledResult());
        assertTrue(cancelledResult instanceof Result.Error);
        assertEquals(ErrorMapper.REMOTEDB_GET_ERROR, ((Result.Error) cancelledResult).getMessage());
    }

    @Test
    public void readPostsByCategory() throws InterruptedException {
        String category = "Eventi";

        doAnswer(invocation -> {
            Callback addedCallback = invocation.getArgument(1);
            addedCallback.onComplete(new Result.Success());
            Callback changedCallback = invocation.getArgument(2);
            changedCallback.onComplete(new Result.Success());
            Callback removedCallback = invocation.getArgument(3);
            removedCallback.onComplete(new Result.Success());
            Callback cancelledCallback = invocation.getArgument(4);
            cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
            return null;
        }).when(postRepository).readPostsByCategory(eq(category), any(), any(), any(), any());

        dashboardViewModel.readPostsByCategory(category);

        Result addedResult = LiveDataTestUtil.getOrAwaitValue(dashboardViewModel.getPostAddedReadResult());
        assertTrue(addedResult instanceof Result.Success);
        Result changedResult = LiveDataTestUtil.getOrAwaitValue(dashboardViewModel.getPostChangedReadResult());
        assertTrue(changedResult instanceof Result.Success);
        Result removedResult = LiveDataTestUtil.getOrAwaitValue(dashboardViewModel.getPostRemovedReadResult());
        assertTrue(removedResult instanceof Result.Success);
        Result cancelledResult = LiveDataTestUtil.getOrAwaitValue(dashboardViewModel.getReadCancelledResult());
        assertTrue(cancelledResult instanceof Result.Error);
        assertEquals(ErrorMapper.REMOTEDB_GET_ERROR, ((Result.Error) cancelledResult).getMessage());
    }

    @Test
    public void readPostsByTitleOrDescription() throws InterruptedException {
        String keyword = "keyword";

        doAnswer(invocation -> {
            Callback addedCallback = invocation.getArgument(1);
            addedCallback.onComplete(new Result.Success());
            Callback changedCallback = invocation.getArgument(2);
            changedCallback.onComplete(new Result.Success());
            Callback removedCallback = invocation.getArgument(3);
            removedCallback.onComplete(new Result.Success());
            Callback cancelledCallback = invocation.getArgument(4);
            cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
            return null;
        }).when(postRepository).readPostsByCategory(eq(keyword), any(), any(), any(), any());

        dashboardViewModel.readPostsByCategory(keyword);

        Result addedResult = LiveDataTestUtil.getOrAwaitValue(dashboardViewModel.getPostAddedReadResult());
        assertTrue(addedResult instanceof Result.Success);
        Result changedResult = LiveDataTestUtil.getOrAwaitValue(dashboardViewModel.getPostChangedReadResult());
        assertTrue(changedResult instanceof Result.Success);
        Result removedResult = LiveDataTestUtil.getOrAwaitValue(dashboardViewModel.getPostRemovedReadResult());
        assertTrue(removedResult instanceof Result.Success);
        Result cancelledResult = LiveDataTestUtil.getOrAwaitValue(dashboardViewModel.getReadCancelledResult());
        assertTrue(cancelledResult instanceof Result.Error);
        assertEquals(ErrorMapper.REMOTEDB_GET_ERROR, ((Result.Error) cancelledResult).getMessage());
    }


}