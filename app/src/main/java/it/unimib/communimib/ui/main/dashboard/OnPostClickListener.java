package it.unimib.communimib.ui.main.dashboard;
import it.unimib.communimib.model.Post;

public interface OnPostClickListener {
    void onItemClick(Post post);
    void onImageSliderClick(Post post);
}
