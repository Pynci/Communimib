package it.unimib.communimib.ui.main.reports.dialogs.favourites;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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
            listItemView = inflater.inflate(R.layout.favorite_building_list_item, parent, false);
        }

        ToggleButton button = listItemView.findViewById(R.id.favorite_building_toggle_button);
        button.setOnClickListener(v -> {
            Drawable icon;

            if(button.isChecked())
                icon = AppCompatResources.getDrawable(context, R.drawable.heart_filled);
            else
                icon = AppCompatResources.getDrawable(context, R.drawable.heart_unfilled);

            button.setBackground(icon);
        });

        return listItemView;
    }
}
