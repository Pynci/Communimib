package it.unimib.communimib.repository;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.dashboard.FakeDashboardRemoteDataSource;
import it.unimib.communimib.model.Result;

public class PostRepositoryTest {

    private FakeDashboardRemoteDataSource dashboardRemoteDataSource;
    private IPostRepository postRepository;
    private Result result;

    @Before
    public void setUp() throws Exception {
        dashboardRemoteDataSource = new FakeDashboardRemoteDataSource();
        postRepository = new PostRepository(dashboardRemoteDataSource);
    }

    @Test
    public void readAllPosts() {
        Callback addedCallback = result -> {};
        Callback changedCallback = result -> {};
        Callback removedCallback = result -> {};
        Callback cancelledCallback = result -> {};
        postRepository.readAllPosts(
                addedCallback,
                changedCallback,
                removedCallback,
                cancelledCallback);

        assertEquals(addedCallback, dashboardRemoteDataSource.addedCallback);
        assertEquals(changedCallback, dashboardRemoteDataSource.changedCallback);
        assertEquals(removedCallback, dashboardRemoteDataSource.removedCallback);
        assertEquals(cancelledCallback, dashboardRemoteDataSource.cancelledCallback);
    }

    @Test
    public void readPostsByCategory() {
    }

    @Test
    public void readPostsByTitleOrDescription() {
    }

    @Test
    public void readPostsByTitleOrDescriptionAndCategory() {
    }

    @Test
    public void createPost() {
    }

    @Test
    public void deletePost() {
    }


}