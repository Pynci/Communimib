package it.unimib.communimib.repository;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.dashboard.FakeDashboardRemoteDataSource;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;

public class PostRepositoryTest {

    private FakeDashboardRemoteDataSource dashboardRemoteDataSource;
    private IPostRepository postRepository;
    private Result result;
    private User user;

    @Before
    public void setUp() throws Exception {
        dashboardRemoteDataSource = new FakeDashboardRemoteDataSource();
        postRepository = new PostRepository(dashboardRemoteDataSource);
        this.user = new User("11111", "g.vitale16@campus.unimib.it", "Giulia", "Vitale", false);
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
    public void createPost() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        List<String> pictures = new ArrayList<>();
        pictures.add(
                "https://firebasestorage.googleapis.com/v0/b/communimib.appspot.com/o/user_propics%2FHrHEip8Vx0XdNfgnwMq8wB6ZCat2.png?alt=media&token=b90de28f-bd9e-47d3-a523-b19ec9f576e1");
        pictures.add(
                "https://firebasestorage.googleapis.com/v0/b/communimib.appspot.com/o/user_propics%2FHrHEip8Vx0XdNfgnwMq8wB6ZCat2.png?alt=media&token=b90de28f-bd9e-47d3-a523-b19ec9f576e1");
        postRepository.createPost("title", "description", "category", user, "g.vitale16@campus.unimib.it", "https://link", 1234566, pictures, result1 -> {
            this.result = result1;
            countDownLatch.countDown();
        });
        countDownLatch.await();
        assertTrue(result instanceof Result.Success);
    }

    @Test
    public void deletePostSuccess() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Post post = new Post("title", "description", "category", user, "g.vitale16@campus.unimib.it", "https://link", 1234566, new ArrayList<>());
        post.setPid("12345");
        dashboardRemoteDataSource.posts.put(post.getPid(), post);
        postRepository.deletePost(post,
                result1 -> {
                    this.result = result1;
                    countDownLatch.countDown();
                });
        countDownLatch.await();
        assertTrue(result instanceof Result.Success);
    }

    @Test
    public void deletePostFailure() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        dashboardRemoteDataSource.posts.clear();
        Post post = new Post("title", "description", "category", user, "g.vitale16@campus.unimib.it", "https://link", 1234566, new ArrayList<>());
        post.setPid("12345");
        postRepository.deletePost(post,
                result1 -> {
                    this.result = result1;
                    countDownLatch.countDown();
                });
        countDownLatch.await();
        assertTrue(result instanceof Result.Error);
    }


}