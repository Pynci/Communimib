package it.unimib.communimib.ui.main.dashboard.detailedpostwithcomments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.BottomNavigationBarListener;
import it.unimib.communimib.databinding.FragmentDetailedPostBinding;
import it.unimib.communimib.model.Post;
import it.unimib.communimib.util.DateFormatter;
import it.unimib.communimib.util.TopbarHelper;

public class DetailedPostFragment extends Fragment {

    private BottomNavigationBarListener mListener;
    private FragmentDetailedPostBinding binding;
    private Post post;

    public DetailedPostFragment() {
        //Costruttore vuoto
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
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
            binding.detailedPostItemImageInsideCard.setVisibility(View.VISIBLE);
        }
        else{
            binding.detailedPostItemImageSlider.setVisibility(View.GONE);
            binding.detailedPostItemImageSliderCardView.setVisibility(View.GONE);
            binding.detailedPostItemImageInsideCard.setVisibility(View.GONE);
        }

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof BottomNavigationBarListener) {
            mListener = (BottomNavigationBarListener) context;
        } else {
            throw new RuntimeException(context.toString() + " must implement BottomNavigationBarListener");
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