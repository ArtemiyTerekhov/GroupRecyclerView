package com.example.terekhov.grouprecyclerview.view;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.terekhov.grouprecyclerview.adapter.FileListAdapter;
import com.example.terekhov.grouprecyclerview.data.FileInfo;

/**
 * Created by Artemiy Terekhov on 26.06.2015.
 */
public class ItemFileListView extends RecyclerView.ViewHolder
        implements View.OnClickListener {

    private FileInfo mItem;
    private FileListAdapter.OnClickListener mListener;

    public ItemFileListView(View view, FileListAdapter.OnClickListener listener) {
        super(view);

        itemView.setOnClickListener(this);
        mListener = listener;

        injectViews(view);
    }

    private void injectViews(View view) {
//        mImageThumbnail = (ImageView) view.findViewById(R.id.ivThumbnail);
    }

    public void setItem(FileInfo itemFileInfo) {
        mItem = itemFileInfo;
        fillView(mItem);
    }

    @Override
    public void onClick(View view) {
        if (mItem != null && mListener != null) {
            mListener.onClickItem(mItem, this);
        }
    }

    private void fillView(FileInfo itemFileInfo) {
        if (itemFileInfo == null)
            return;
    }

}
