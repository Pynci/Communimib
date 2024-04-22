package it.unimib.communimib.ui.main.reports.dialogs.favourites;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.content.res.AppCompatResources;

import java.util.List;

import it.unimib.communimib.R;

public class FavoriteBuildingsAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> data;
    public FavoriteBuildingsAdapter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            listItemView = inflater.inflate(R.layout.favorite_building_listview_element, parent, false);
        }

        //Gestione del bottone
        ToggleButton likeButton = listItemView.findViewById(R.id.favorite_building_toggle_button);
        likeButton.setOnClickListener(v -> {
            Drawable icon;
            boolean checked = likeButton.isChecked();

            if(checked)
                icon = AppCompatResources.getDrawable(context, R.drawable.heart_filled);
            else
                icon = AppCompatResources.getDrawable(context, R.drawable.heart_unfilled);

            likeButton.setBackground(icon);
            onButtonClicked(context, checked, likeButton);
        });

        //Gestione del testo dell'edificio
        TextView textViewBuilding = listItemView.findViewById(R.id.favorite_building_textview);
        textViewBuilding.setText("Edificio " + data.get(position));

        return listItemView;
    }

    private void onButtonClicked(Context context, boolean checked, ToggleButton toggleButton) {
        if(checked)
            beatDownAnimation(context, toggleButton);
    }

    private void beatDownAnimation(Context context, ToggleButton toggleButton) {
        Animation beatDown = AnimationUtils.loadAnimation(context, R.anim.heart_beat);
        toggleButton.startAnimation(beatDown);
    }
}
