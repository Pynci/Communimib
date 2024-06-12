package it.unimib.communimib.datasource.user;

import java.util.List;
import it.unimib.communimib.Callback;

public interface IUserLocalDataSource {

    void saveUserFavoriteBuildings(List<String> favoriteBuildings, Callback callback);

    void getUserFavoriteBuildings(Callback callback);
}
