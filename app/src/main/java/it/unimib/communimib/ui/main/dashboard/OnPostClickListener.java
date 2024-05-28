package it.unimib.communimib.ui.main.dashboard;
import java.util.List;

import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.User;

public interface OnPostClickListener {
    void onItemClick(Post post);
    void onImageSliderClick(List<String> pictures);
    void onProfileClick(User postAuthor);
}
