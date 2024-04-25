package it.unimib.communimib.ui.main.reports;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.unimib.communimib.BottomNavigationBarListener;
import it.unimib.communimib.R;

public class DetailedReportFragment extends Fragment {

    private BottomNavigationBarListener mListener;

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

    public DetailedReportFragment() {
        //Empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomNavigationBar();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_detailed_report, container, false);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        showBottomNavigationBar();
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