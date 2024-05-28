package it.unimib.communimib.ui.main.dashboard.detailedpostwithcomments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

import it.unimib.communimib.BottomNavigationBarListener;
import it.unimib.communimib.R;
import it.unimib.communimib.databinding.FragmentDetailedPostBinding;
import it.unimib.communimib.model.Comment;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.model.User;
import it.unimib.communimib.ui.main.dashboard.OnPostClickListener;
import it.unimib.communimib.ui.main.dashboard.pictures.PostPicturesFragmentDialog;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.TopbarHelper;

public class DetailedPostFragment extends Fragment {


    private boolean isScrollButtonVisible = false;
    private boolean isAnimating = false;
    private DetailedPostViewModel detailedPostViewModel;
    private BottomNavigationBarListener mListener;
    private FragmentDetailedPostBinding binding;
    private Post post;


    public DetailedPostFragment() {
        //costruttore vuoto
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

        detailedPostViewModel = new ViewModelProvider(this,
                new DetailedPostViewModelFactory(this.getContext()))
                .get(DetailedPostViewModel.class);
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

        //gestione dei commenti
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        CommentsAdapter commentsAdapter = new CommentsAdapter(post, getContext(), new OnPostClickListener() {
            @Override
            public void onItemClick(Post post) {
                // non deve fare niente
            }

            @Override
            public void onImageSliderClick(List<String> pictures) {
                PostPicturesFragmentDialog imageDialog = new PostPicturesFragmentDialog(post.getPictures());
                imageDialog.show(getParentFragmentManager(), "Image Dialog");
            }

            @Override
            public void onProfileClick(User postAuthor) {
                Log.d("Pizza", "CIAOOOOO");
            }

        });
        binding.detailedPostItemCommentsRecyclerView.setLayoutManager(layoutManager);
        binding.detailedPostItemCommentsRecyclerView.setAdapter(commentsAdapter);

        // Gestione del pulsante per scrollare velocmente verso l'alto
        Animation animationButtonSlideLeft = AnimationUtils.loadAnimation(getContext(), R.anim.button_slide_left);
        animationButtonSlideLeft.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                binding.detailedPostItemFloatingActionButtonGoUp.setVisibility(View.VISIBLE);
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                isScrollButtonVisible = true;
                isAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Non deve fare niente
            }
        });

        Animation animationButtonSlideRight = AnimationUtils.loadAnimation(getContext(), R.anim.button_slide_right);
        animationButtonSlideRight.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                binding.detailedPostItemFloatingActionButtonGoUp.setVisibility(View.GONE);
                isScrollButtonVisible = false;
                isAnimating = false;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // Non deve fare niente
            }
        });

        binding.detailedPostItemFloatingActionButtonGoUp.setVisibility(View.GONE);
        binding.detailedPostItemFloatingActionButtonGoUp.setOnClickListener(v -> {
            // Faccio tornare la RecyclerView al primo elemento
            binding.detailedPostItemCommentsRecyclerView.smoothScrollToPosition(0);

            // Avvio l'animazione di uscita se il pulsante è visibile e non è in corso un'animazione
            if (isScrollButtonVisible && !isAnimating) {
                binding.detailedPostItemFloatingActionButtonGoUp.startAnimation(animationButtonSlideRight);
            }
        });

        RecyclerView recyclerView = binding.detailedPostItemCommentsRecyclerView;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition > 0 && !isScrollButtonVisible && !isAnimating) {
                    // Avvio l'animazione di entrata
                    binding.detailedPostItemFloatingActionButtonGoUp.startAnimation(animationButtonSlideLeft);
                } else if (firstVisibleItemPosition == 0 && isScrollButtonVisible && !isAnimating) {
                    // Avvio l'animazione di uscita
                    binding.detailedPostItemFloatingActionButtonGoUp.startAnimation(animationButtonSlideRight);
                }
            }
        });



        //Lettura dei commenti dal repository
        detailedPostViewModel.cleanViewModel();
        detailedPostViewModel.readCommentsByPid(post.getPid());

        //Gestione dell'aggiunta di un commento
        detailedPostViewModel.getCommentAddedReadResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Comment comment = ((Result.CommentSuccess) result).getComment();
                commentsAdapter.addItem(comment);
            }
            else{
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        //Gestione della modifica di un commento
        detailedPostViewModel.getCommentChangedReadResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Comment comment = ((Result.CommentSuccess) result).getComment();
                commentsAdapter.editItem(comment);
            }
            else{
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        //Gestione della rimozione di un commento
        detailedPostViewModel.getCommentRemovedReadResult().observe(getViewLifecycleOwner(), result -> {
            if(result.isSuccessful()){
                Comment comment = ((Result.CommentSuccess) result).getComment();
                commentsAdapter.removeItem(comment);
            }
            else{
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

        //Gestione dell'interruzione durante la lettura
        detailedPostViewModel.getReadCancelledResult().observe(getViewLifecycleOwner(), result -> {
            Snackbar.make(requireView(),
                    ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                    BaseTransientBottomBar.LENGTH_SHORT).show();
        });

        //Gestione della creazione di un nuovo commento
        binding.detailedPostItemSend.setOnClickListener(v -> {
            String text = binding.detailedPostItemComment.getText().toString();
            if(!text.isEmpty()){
                detailedPostViewModel.createComment(post.getPid(), text);
                binding.detailedPostItemComment.setText("");
            }
        });

        detailedPostViewModel.getCommentCreationResult().observe(getViewLifecycleOwner(), result -> {
            if(!result.isSuccessful()){
                Snackbar.make(requireView(),
                        ErrorMapper.getInstance().getErrorMessage(((Result.Error) result).getMessage()),
                        BaseTransientBottomBar.LENGTH_SHORT).show();
            }
        });

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