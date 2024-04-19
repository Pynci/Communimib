package it.unimib.communimib.ui.main.reports;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import java.util.ArrayList;
import java.util.List;

import it.unimib.communimib.R;

public class FilterReportListViewAdapter extends BaseAdapter {
    private final Context context;
    private final List<String> data;
    private final List<String> checkedItems;

    private final OnListViewItemCheck onListViewItemCheck;

    public FilterReportListViewAdapter(Context context, List<String> data, OnListViewItemCheck onListViewItemCheck) {
        this.context = context;
        this.data = data;
        checkedItems = new ArrayList<>();
        this.onListViewItemCheck = onListViewItemCheck;
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
            listItemView = inflater.inflate(R.layout.building_filter_listview_element, parent, false);
        }

        CheckBox checkBoxItem = listItemView.findViewById(R.id.building_listview_element_checkbox);

        //checkBoxItem.setChecked(checkedItems.contains(data.get(position)));
        checkBoxItem.setText(data.get(position));
        /*
        checkBoxItem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if(isChecked) {
                checkedItems.add(data.get(position));
                onListViewItemCheck.execute();
            }
            else
                checkedItems.remove(data.get(position));
        });
         */

        return listItemView;
    }

    public List<String> getCheckedItems() {
        return checkedItems;
    }

    public void setAllItemsUnchecked() {
        checkedItems.clear();
        notifyDataSetChanged();
    }

    public interface OnListViewItemCheck{
        void execute();
    }
}


