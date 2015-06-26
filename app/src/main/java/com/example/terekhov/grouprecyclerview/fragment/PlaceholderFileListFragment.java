package com.example.terekhov.grouprecyclerview.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.example.terekhov.grouprecyclerview.R;
import com.example.terekhov.grouprecyclerview.adapter.FileListAdapter;
import com.example.terekhov.grouprecyclerview.adapter.SectionFileListAdapter;
import com.example.terekhov.grouprecyclerview.data.FileInfo;
import com.example.terekhov.grouprecyclerview.data.SectionFileInfo;
import com.example.terekhov.grouprecyclerview.widget.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Created by Artemiy Terekhov on 26.06.2015.
 */
public class PlaceholderFileListFragment extends Fragment {

    public static final String TAG = PlaceholderFileListFragment.class.getName();

    private EmptyRecyclerView mRecyclerView;
    private LinearLayout mEmptyLayout;
    private ProgressBar mProgress;

    private SectionFileListAdapter mAdapter;
    private FileListAdapter.OnClickListener mListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (FileListAdapter.OnClickListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FileListAdapter.OnClickListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        setData(null);
        mListener = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAdapter = new SectionFileListAdapter(new FileListAdapter(mListener));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_file_list, container, false);

        injectViews(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setEmptyView(mEmptyLayout);
    }

    private void injectViews(View view) {
        mRecyclerView = (EmptyRecyclerView) view.findViewById(R.id.listViews);
        mEmptyLayout = (LinearLayout) view.findViewById(R.id.layoutEmptyView);
        mProgress = (ProgressBar) view.findViewById(R.id.progressBarView);
    }

    public List<FileInfo> getItems() {
        if (mAdapter != null) {
            return mAdapter.getItems();
        }

        return null;
    }

    public void setData(List<FileInfo> data) {
        mAdapter.clear();
        mAdapter.addAll(data);

        if (data == null || data.size() <= 0) {
            mAdapter.setSections(null);
            return;
        }

        Calendar calendarInstance = GregorianCalendar.getInstance();

        List<SectionFileInfo> sections = new ArrayList<SectionFileInfo>();
        boolean isSectionFound = false;
        int counter = 0;
        for (FileInfo info : data) {
            calendarInstance.setTimeInMillis(info.getDateInMillis());
            String headerDate = DateUtils.formatDateTime(getActivity(), calendarInstance.getTimeInMillis(), DateUtils.FORMAT_SHOW_DATE);
            for (SectionFileInfo entrySection : sections) {
                if (entrySection.getTitle().toString().equals(headerDate)) {
                    isSectionFound = true;
                    break;
                }
            }
            if (!isSectionFound) {
                SectionFileInfo section = new SectionFileInfo(counter, headerDate);
                sections.add(section);
            }
            isSectionFound = false;
            counter++;
        }

        SectionFileInfo[] sectionsArray = new SectionFileInfo[sections.size()];
        mAdapter.setSections(sections.toArray(sectionsArray));
    }

    private void showEmptyView(boolean isShow) {
        int count = mAdapter.getItemCount();
        if (count <= 0 && isShow)
            mEmptyLayout.setVisibility(View.VISIBLE);
        else
            mEmptyLayout.setVisibility(View.GONE);
    }

    public boolean isProgress() {
        return mProgress.getVisibility() == View.VISIBLE;
    }

    public void showProgress() {
        mProgress.setVisibility(View.VISIBLE);
        showEmptyView(false);
    }

    public void hideProgress() {
        mProgress.setVisibility(View.GONE);
    }
}
