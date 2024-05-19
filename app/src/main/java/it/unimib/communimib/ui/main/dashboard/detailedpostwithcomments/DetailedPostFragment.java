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

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import it.unimib.communimib.BottomNavigationBarListener;
import it.unimib.communimib.databinding.FragmentDetailedPostBinding;
import it.unimib.communimib.model.Comment;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.model.Result;
import it.unimib.communimib.ui.main.dashboard.OnPostClickListener;
import it.unimib.communimib.ui.main.dashboard.dialogs.DashboardImageFragmentDialog;
import it.unimib.communimib.util.ErrorMapper;
import it.unimib.communimib.util.TopbarHelper;

public class DetailedPostFragment extends Fragment {

    private interface OnSliderClickListener {
        void onClick();
    }

    private DetailedPostViewModel detailedPostViewModel;
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
            public void onImageSliderClick(Post post) {
                onSliderClickListener.onClick();
            }
        });
        binding.detailedPostItemCommentsRecyclerView.setLayoutManager(layoutManager);
        binding.detailedPostItemCommentsRecyclerView.setAdapter(commentsAdapter);

        //Gestione del pulsante di scroll in alto
        binding.detailedPostItemFloatingActionButtonGoUp.setVisibility(View.GONE);
        binding.detailedPostItemFloatingActionButtonGoUp.setOnClickListener(v -> {
            //Faccio tornare la scrollview al primo elemento
            binding.detailedPostItemCommentsRecyclerView.smoothScrollToPosition(0);
            binding.detailedPostItemFloatingActionButtonGoUp.setVisibility(View.GONE);
        });

        RecyclerView recyclerView = binding.detailedPostItemCommentsRecyclerView;

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();
                if (firstVisibleItemPosition > 0) {
                    binding.detailedPostItemFloatingActionButtonGoUp.setVisibility(View.VISIBLE);
                } else {
                    binding.detailedPostItemFloatingActionButtonGoUp.setVisibility(View.GONE);
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