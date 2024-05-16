package it.unimib.communimib.ui.main.reports.dialogs.favorites;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.Callback;
import it.unimib.communimib.LiveDataTestUtil;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.repository.UserRepository;

public class FavoriteBuildingViewModelTest {

    private FavoriteBuildingViewModel favoriteBuildingViewModel;
    private UserRepository userRepository;

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        favoriteBuildingViewModel = new FavoriteBuildingViewModel(userRepository);
    }

    @Test
    public void setUserFavoriteBuildings() throws InterruptedException {
        List<String> buildings = new ArrayList<>();
        buildings.add("U1");
        buildings.add("U2");

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(1);
            callback.onComplete(new Result.Success());
            return null;
        }).when(userRepository).storeUserFavoriteBuildings(eq(buildings), any());

        favoriteBuildingViewModel.setUserFavoriteBuildings(buildings, () -> {});
        Result result = LiveDataTestUtil.getOrAwaitValue(favoriteBuildingViewModel.getSetUserFavoriteBuildingsResult());
        assertTrue(result instanceof Result.Success);
    }

    @Test
    public void getUserFavoriteBuildings() throws InterruptedException {
        List<String> buildings = new ArrayList<>();
        buildings.add("U1");
        buildings.add("U2");

        doAnswer(invocation -> {
            Callback callback = invocation.getArgument(0);
            callback.onComplete(new Result.UserFavoriteBuildingsSuccess(buildings));
            return null;
        }).when(userRepository).readUserFavoriteBuildings(any());

        favoriteBuildingViewModel.getUserFavoriteBuildings();
        Result result = LiveDataTestUtil.getOrAwaitValue(favoriteBuildingViewModel.getGetUserFavoriteBuildingsResult());
        assertTrue(result instanceof Result.UserFavoriteBuildingsSuccess);
        assertEquals(buildings, ((Result.UserFavoriteBuildingsSuccess) result).getFavoriteBuildings());
    }
}