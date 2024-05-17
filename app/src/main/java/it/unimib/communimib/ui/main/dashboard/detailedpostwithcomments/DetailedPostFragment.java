package it.unimib.communimib.ui.main.dashboard.detailedpostwithcomments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.interfaces.ItemClickListener;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.BottomNavigationBarListener;
import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentDetailedPostBinding;
import it.unimib.communimib.model.Comment;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.User;
import it.unimib.communimib.ui.main.dashboard.dialogs.DashboardImageFragmentDialog;
import it.unimib.communimib.util.DateFormatter;
import it.unimib.communimib.util.TopbarHelper;

public class DetailedPostFragment extends Fragment {

    private static final int HIDE_THRESHOLD = 300;  // Soglia per nascondere il post
    private int scrolledDistanceDown = 0;
    private boolean controlsVisible = true;


    private interface OnSliderClickListener {
        void onClick();
    }

    private final OnSliderClickListener onSliderClickListener;
    private BottomNavigationBarListener mListener;
    private FragmentDetailedPostBinding binding;
    private Post post;

    public DetailedPostFragment() {
        onSliderClickListener = () -> {
            DashboardImageFragmentDialog imageDialog = new DashboardImageFragmentDialog(post);
            imageDialog.show(getParentFragmentManager(), "Image Dialog");
        };
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TopbarHelper.handleTopbar((AppCompatActivity) getActivity());
        hideBottomNavigationBar();
        try {
            DetailedPostFragmentArgs args = DetailedPostFragmentArgs.fromBundle(getArguments());
            this.post = args.getPost();
        }
        catch (Exception e) {
            post = null;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDetailedPostBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Caricamento dei dati nei relativi componenti
        binding.detailedPostItemName.setText(post.getAuthor().getName());
        binding.detailedPostItemSurname.setText(post.getAuthor().getSurname());
        binding.detailedPostItemTitle.setText(post.getTitle());
        binding.detailedPostItemDescription.setText(post.getDescription());
        binding.detailedPostItemDatetime.setText(String.valueOf(post.getTimestamp()));

        if(post.getEmail() != null && !post.getEmail().isEmpty()){
            binding.detailedPostItemEmail.setText(post.getEmail());
        }
        else{
            binding.detailedPostItemEmail.setVisibility(View.GONE);
            binding.detailedPostItemEmailIcon.setVisibility(View.GONE);
        }
        if(post.getLink() != null && !post.getLink().isEmpty()){
            binding.detailedPostItemLink.setText(post.getLink());
        }
        else{
            binding.detailedPostItemLink.setVisibility(View.GONE);
            binding.detailedPostItemLinkIcon.setVisibility(View.GONE);
        }
        binding.detailedPostItemDatetime.setText(DateFormatter.format(post.getTimestamp(), getContext()));
        if(post.getAuthor().getPropic() != null){
            Glide
                    .with(getContext())
                    .load(Uri.parse(post.getAuthor().getPropic()))
                    .into(binding.detailedPostItemPropic);
        }

        List<SlideModel> slideModels = new ArrayList<>();
        if(!post.getPictures().isEmpty()){
            for (String picture : post.getPictures()) {
                slideModels.add(new SlideModel(picture, ScaleTypes.FIT));
            }
            binding.detailedPostItemImageSlider.setImageList(slideModels, ScaleTypes.FIT);
            binding.detailedPostItemImageSlider.setVisibility(View.VISIBLE);
            binding.detailedPostItemImageSliderCardView.setVisibility(View.VISIBLE);
        }
        else{
            binding.detailedPostItemImageSlider.setVisibility(View.GONE);
            binding.detailedPostItemImageSliderCardView.setVisibility(View.GONE);
        }

        binding.detailedPostItemImageSlider.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemSelected(int i) {
                onSliderClickListener.onClick();
            }

            @Override
            public void doubleClick(int i) {
                //per ora non serve
            }
        });

        //gestione dei commenti
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        CommentsAdapter commentsAdapter = new CommentsAdapter(getContext());
        binding.detailedPostItemCommentsRecyclerView.setLayoutManager(layoutManager);
        binding.detailedPostItemCommentsRecyclerView.setAdapter(commentsAdapter);

        //Istanzio le animazioni
        Animation animationPostSlideUp = AnimationUtils.loadAnimation(getContext(), R.anim.post_slide_up);
        animationPostSlideUp.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                //Non deve fare niente
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.postsection.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //Non deve fare niente
            }
        });

        Animation animationPostSlideDown = AnimationUtils.loadAnimation(getContext(), R.anim.post_slide_down);
        animationPostSlideDown.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.postsection.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //Non deve fare niente
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                //Non deve fare niente
            }
        });
        binding.detailedPostItemCommentsRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {
                    // L'utente sta scorrendo verso il basso
                    if (scrolledDistanceDown > HIDE_THRESHOLD && controlsVisible) {
                        binding.postsection.startAnimation(animationPostSlideUp);
                        controlsVisible = false;
                        scrolledDistanceDown = 0;  // Resetta la distanza scrollata verso il basso
                    } else {
                        scrolledDistanceDown += dy;
                    }
                } else if (dy < 0) {
                    // L'utente sta scorrendo verso l'alto
                    scrolledDistanceDown = 0;  // Resetta la distanza scrollata verso il basso
                }

                if (!recyclerView.canScrollVertically(-1)) {
                    // L'utente ha raggiunto la cima
                    if (!controlsVisible) {
                        binding.postsection.startAnimation(animationPostSlideDown);
                        controlsVisible = true;
                    }
                    scrolledDistanceDown = 0;
                }
            }
        });


        for(int i = 0; i < 10; i++)
            commentsAdapter.addItem(new Comment(
                    new User("taaaah", "taaah", "Signor", "Provolazzi", false),
                    "Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                            "In egestas semper bibendum. Etiam fermentum est sit amet lacinia pulvinar. " +
                            "Phasellus at ipsum ante. Phasellus fringilla ipsum sem, eu vestibulum nisl blandit eu. " +
                            "Cras ornare lobortis est sed gravida. " +
                            "Aenean vitae justo laoreet, viverra mauris eget, faucibus enim. Fusce purus nunc. "));

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showBottomNavigationBar();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavigationBarListener) {
            mListener = (BottomNavigationBarListener) context;
        } else {
            throw new RuntimeException(context + " must implement BottomNavigationBarListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private void hideBottomNavigationBar() {
        if (mListener != null) {
            mListener.hideBottomNavigationBar();
        }
    }

    private void showBottomNavigationBar() {
        if (mListener != null) {
            mListener.showBottomNavigationBar();
        }
    }
}