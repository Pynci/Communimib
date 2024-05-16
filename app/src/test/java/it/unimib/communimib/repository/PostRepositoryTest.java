package it.unimib.communimib.repository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.dashboard.FakeDashboardRemoteDataSource;
import it.unimib.communimib.datasource.post.IPostRemoteDataSource;
import it.unimib.communimib.datasource.post.PostRemoteDataSource;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.ErrorMapper;

public class PostRepositoryTest {

    //private FakeDashboardRemoteDataSource dashboardRemoteDataSource;
    private IPostRemoteDataSource postRemoteDataSource;
    private IPostRepository postRepository;
    private Result result;
    private User user;

    @Before
    public void setUp() throws Exception {
        postRemoteDataSource = mock(PostRemoteDataSource.class);
        postRepository = new PostRepository(postRemoteDataSource);
        this.user = new User("11111", "g.vitale16@campus.unimib.it", "Giulia", "Vitale", false);
    }

    @Test
    public void readAllPosts() {
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
        }).when(postRemoteDataSource).readAllPosts(any(), any(), any(), any());

        postRepository.readAllPosts(
                result -> {
                    assertTrue(result instanceof Result.Success);
                },
                result -> {
                    assertTrue(result instanceof Result.Success);
                },
                result -> {
                    assertTrue(result instanceof Result.Success);
                },
                result -> {
                    assertTrue(result instanceof Result.Error);
                    assertEquals(((Result.Error) result).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
                });
    }

    @Test
    public void readPostsByCategory() {
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
        }).when(postRemoteDataSource).readPostsByCategory(eq(category), any(), any(), any(), any());

        postRepository.readPostsByCategory(
                category,
                result -> {
                    assertTrue(result instanceof Result.Success);
                },
                result -> {
                    assertTrue(result instanceof Result.Success);
                },
                result -> {
                    assertTrue(result instanceof Result.Success);
                },
                result -> {
                    assertTrue(result instanceof Result.Error);
                    assertEquals(((Result.Error) result).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
                });

    }

    @Test
    public void readPostsByTitleOrDescription() {
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
        }).when(postRemoteDataSource).readPostsByTitleOrDescription(eq(keyword), any(), any(), any(), any());

        postRepository.readPostsByTitleOrDescription(
                keyword,
                result -> {
                    assertTrue(result instanceof Result.Success);
                },
                result -> {
                    assertTrue(result instanceof Result.Success);
                },
                result -> {
                    assertTrue(result instanceof Result.Success);
                },
                result -> {
                    assertTrue(result instanceof Result.Error);
                    assertEquals(((Result.Error) result).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
                });
    }

    @Test
    public void readPostsByTitleOrDescriptionAndCategory() {
        String category = "Eventi";
        String keyword = "keyword";

        doAnswer(invocation -> {
            Callback addedCallback = invocation.getArgument(2);
            addedCallback.onComplete(new Result.Success());
            Callback changedCallback = invocation.getArgument(3);
            changedCallback.onComplete(new Result.Success());
            Callback removedCallback = invocation.getArgument(4);
            removedCallback.onComplete(new Result.Success());
            Callback cancelledCallback = invocation.getArgument(5);
            cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
            return null;
        }).when(postRemoteDataSource).readPostsByTitleOrDescriptionAndCategory(eq(keyword), eq(category), any(), any(), any(), any());

        postRepository.readPostsByTitleOrDescriptionAndCategory(
                keyword,
                category,
                result -> {
                    assertTrue(result instanceof Result.Success);
                },
                result -> {
                    assertTrue(result instanceof Result.Success);
                },
                result -> {
                    assertTrue(result instanceof Result.Success);
                },
                result -> {
                    assertTrue(result instanceof Result.Error);
                    assertEquals(((Result.Error) result).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
                });
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
        //dashboardRemoteDataSource.posts.put(post.getPid(), post);
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
        //dashboardRemoteDataSource.posts.clear();
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