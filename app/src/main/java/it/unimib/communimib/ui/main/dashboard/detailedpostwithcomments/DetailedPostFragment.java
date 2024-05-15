package it.unimib.communimib.ui.main.dashboard.detailedpostwithcomments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.communimib.databinding.FragmentDetailedPostBinding;
import it.unimib.communimib.model.Post;

public class DetailedPostFragment extends Fragment {

    private FragmentDetailedPostBinding binding;

    private Post post;
    public DetailedPostFragment() {
        //Costruttore vuoto
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            DetailedPostFragmentArgs args = DetailedPostFragmentArgs.fromBundle(getArguments());
            this.post = args.getPost();
        }
        catch (Exception e) {
            post = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDetailedPostBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}