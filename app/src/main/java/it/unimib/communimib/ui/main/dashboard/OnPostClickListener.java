package it.unimib.communimib.ui.main.dashboard;
import java.util.List;

import it.unimib.communimib.model.Post;

public interface OnPostClickListener {
    void onItemClick(Post post);
    void onImageSliderClick(List<String> pictures);
}
