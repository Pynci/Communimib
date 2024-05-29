package it.unimib.communimib.repository;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import it.unimib.communimib.Callback;
import it.unimib.communimib.datasource.post.CommentRemoteDataSource;
import it.unimib.communimib.datasource.post.ICommentRemoteDataSource;
import it.unimib.communimib.datasource.post.IPostRemoteDataSource;
import it.unimib.communimib.datasource.post.PostRemoteDataSource;
import it.unimib.communimib.model.Comment;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.util.ErrorMapper;

public class PostRepositoryTest {

    private IPostRemoteDataSource postRemoteDataSource;
    private ICommentRemoteDataSource commentRemoteDataSource;
    private IPostRepository postRepository;
    private User user;

    @Before
    public void setUp() throws Exception {
        postRemoteDataSource = mock(PostRemoteDataSource.class);
        commentRemoteDataSource = mock(CommentRemoteDataSource.class);
        postRepository = new PostRepository(postRemoteDataSource, commentRemoteDataSource);
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
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result -> {
                    assertTrue(result instanceof Result.Error);
                    assertEquals(((Result.Error) result).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
                });

        verify(postRemoteDataSource).readAllPosts(any(), any(), any(), any());
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
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result -> {
                    assertTrue(result instanceof Result.Error);
                    assertEquals(((Result.Error) result).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
                });

        verify(postRemoteDataSource).readPostsByCategory(eq(category), any(), any(), any(), any());
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
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result -> {
                    assertTrue(result instanceof Result.Error);
                    assertEquals(((Result.Error) result).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
                });

        verify(postRemoteDataSource).readPostsByTitleOrDescription(eq(keyword), any(), any(), any(), any());
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
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result ->
                        assertTrue(result instanceof Result.Success)
                ,
                result -> {
                    assertTrue(result instanceof Result.Error);
                    assertEquals(((Result.Error) result).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
                });

        verify(postRemoteDataSource).readPostsByTitleOrDescriptionAndCategory(eq(keyword), eq(category), any(), any(), any(), any());
    }

    @Test
    public void readPostsByUid(){
        String uid = "123456789";

        doAnswer(invocation ->{
            Callback addedCallback = invocation.getArgument(1);
            addedCallback.onComplete(new Result.Success());
            Callback changedCallback = invocation.getArgument(2);
            changedCallback.onComplete(new Result.Success());
            Callback removedCallback = invocation.getArgument(3);
            removedCallback.onComplete(new Result.Success());
            Callback cancelledCallback = invocation.getArgument(4);
            cancelledCallback.onComplete(new Result.Error(ErrorMapper.REMOTEDB_GET_ERROR));
            return null;
        }).when(postRemoteDataSource).readPostsByUid(eq(uid), any(), any(), any(), any());

        postRepository.readPostsByUid(
                uid,
                addedResult -> assertTrue(addedResult instanceof Result.Success),
                changedresult -> assertTrue(changedresult instanceof Result.Success),
                removedResult -> assertTrue(removedResult instanceof Result.Success),
                cancelledResult -> {
                    assertTrue(cancelledResult instanceof Result.Error);
                    assertEquals(((Result.Error) cancelledResult).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
                });

        verify(postRemoteDataSource).readPostsByUid(eq(uid), any(), any(), any(), any());
    }

    @Test
    public void createPost() {

        Post post = new Post("title", "description", "category", user, "g.vitale16@campus.unimib.it", "https://link", 1234566, new ArrayList<>());

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.Success());
            return null;
        }).when(postRemoteDataSource).createPost(eq(post), any());

        postRepository.createPost("title",
                "description",
                "category",
                user,
                "g.vitale16@campus.unimib.it",
                "https://link",
                new ArrayList<>(),
                result ->
                        assertTrue(result instanceof Result.Success)
        );

        verify(postRemoteDataSource).createPost(eq(post), any());
    }

    @Test
    public void deletePost() {

        Post post = new Post("title", "description", "category", user, "g.vitale16@campus.unimib.it", "https://link", 1234566, new ArrayList<>());

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.Success());
            return null;
        }).when(postRemoteDataSource).deletePost(eq(post), any());

        postRepository.deletePost(
                post,
                result ->
                        assertTrue(result instanceof Result.Success)
        );

        verify(postRemoteDataSource).deletePost(eq(post), any());
    }

    @Test
    public void readCommentsByPid() {
        Post post = new Post();
        post.setPid("11111");

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
        }).when(commentRemoteDataSource).readCommentsByPid(eq(post.getPid()), any(), any(), any(), any());

        postRepository.readCommentsByPid(
                post.getPid(),
                result ->
                        assertTrue(result instanceof Result.Success),
                result ->
                        assertTrue(result instanceof Result.Success),
                result ->
                        assertTrue(result instanceof Result.Success),
                result -> {
                    assertTrue(result instanceof Result.Error);
                    assertEquals(((Result.Error) result).getMessage(), ErrorMapper.REMOTEDB_GET_ERROR);
                });
    }

    @Test
    public void createComment() {
        Comment comment = new Comment(user, "commento", System.currentTimeMillis());
        Post post = new Post();
        post.setPid("11111");

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(2);
            callback.onComplete(new Result.Success());
            return null;
        }).when(commentRemoteDataSource).createComment(eq(post.getPid()), eq(comment), any());

        postRepository.createComment(
                post.getPid(),
                user,
                "commento",
                result ->
                        assertTrue(result instanceof Result.Success)
        );
    }

}