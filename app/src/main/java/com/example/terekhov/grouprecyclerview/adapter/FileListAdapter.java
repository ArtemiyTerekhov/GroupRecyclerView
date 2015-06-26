package com.example.terekhov.grouprecyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.terekhov.grouprecyclerview.R;
import com.example.terekhov.grouprecyclerview.data.FileInfo;
import com.example.terekhov.grouprecyclerview.view.ItemFileListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Artemiy Terekhov on 26.06.2015.
 */
public class FileListAdapter extends RecyclerView.Adapter<ItemFileListView> {

    private OnClickListener mListener;
    private List<FileInfo> mListItems = new ArrayList<FileInfo>();

    public interface OnClickListener {
        public void onClickItem(FileInfo itemStateEntry, ItemFileListView itemView);
    }

    public FileListAdapter(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public ItemFileListView onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_file_list_item, parent, false);

        return new ItemFileListView(view, mListener);
    }

    @Override
    public void onBindViewHolder(ItemFileListView viewHolder, int i) {
        final FileInfo item = mListItems.get(i);
        viewHolder.setItem(item);
    }

    @Override
    public int getItemCount() {
        return mListItems.size();
    }
    public void add(FileInfo item) {
        mListItems.add(item);
    }
    public void addAll(List<FileInfo> items) {
        if (items != null)
            mListItems.addAll(items);
    }

    public List<FileInfo> getItems() {
        return mListItems;
    }

    public int size() { return mListItems.size(); }

    public void clear() {
        mListItems.clear();
    }

}
