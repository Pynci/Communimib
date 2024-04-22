package it.unimib.communimib.ui.main.reports.dialogs.favorites;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
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

    private final List<String> checkedItems;
    public FavoriteBuildingsAdapter(Context context, List<String> data, List<String> checkedItems) {
        this.context = context;
        this.data = data;
        this.checkedItems = checkedItems;
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
        ViewHolder holder;

        if (listItemView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            listItemView = inflater.inflate(R.layout.favorite_building_listview_element, parent, false);
            holder = new ViewHolder();
            holder.likeButton = listItemView.findViewById(R.id.favorite_building_toggle_button);
            holder.textViewBuilding = listItemView.findViewById(R.id.favorite_building_textview);
            listItemView.setTag(holder);
        } else {
            holder = (ViewHolder) listItemView.getTag();
        }

        String item = data.get(position);

        holder.likeButton.setOnCheckedChangeListener(null);
        holder.likeButton.setChecked(checkedItems.contains(item));

        Drawable icon = holder.likeButton.isChecked() ?
                AppCompatResources.getDrawable(context, R.drawable.heart_filled) :
                AppCompatResources.getDrawable(context, R.drawable.heart_unfilled);
        holder.likeButton.setBackground(icon);

        holder.likeButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                checkedItems.add(item);
                holder.likeButton.setBackground(AppCompatResources.getDrawable(context, R.drawable.heart_filled));
            } else {
                checkedItems.remove(item);
                holder.likeButton.setBackground(AppCompatResources.getDrawable(context, R.drawable.heart_unfilled));
            }
            onButtonClicked(context, isChecked, holder.likeButton);
            notifyDataSetChanged();
        });

        holder.textViewBuilding.setText("Edificio " + item);

        return listItemView;
    }

    static class ViewHolder {
        ToggleButton likeButton;
        TextView textViewBuilding;
    }

    private void onButtonClicked(Context context, boolean checked, ToggleButton toggleButton) {
        if(checked)
            beatDownAnimation(context, toggleButton);
    }

    private void beatDownAnimation(Context context, ToggleButton toggleButton) {
        Animation beatDown = AnimationUtils.loadAnimation(context, R.anim.heart_beat);
        toggleButton.startAnimation(beatDown);
    }

    public List<String> getCheckedItems() {
        return checkedItems;
    }
}
