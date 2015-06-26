package com.example.terekhov.grouprecyclerview.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.terekhov.grouprecyclerview.R;
import com.example.terekhov.grouprecyclerview.data.SectionFileInfo;
import com.example.terekhov.grouprecyclerview.view.ItemFileListSectionView;
import com.example.terekhov.grouprecyclerview.view.ItemFileListView;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by Artemiy Terekhov on 26.06.2015.
 */
public class SectionFileListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int SECTION_TYPE = 0;

    private FileListAdapter mAdapter;
    private boolean mValid = true;
    private SparseArray<SectionFileInfo> mSections = new SparseArray<SectionFileInfo>();

    private RecyclerView.AdapterDataObserver mDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            mValid = mAdapter.getItemCount() > 0;
            notifyDataSetChanged();
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mValid = mAdapter.getItemCount() > 0;
            notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mValid = mAdapter.getItemCount() > 0;
            notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mValid = mAdapter.getItemCount() > 0;
            notifyItemRangeRemoved(positionStart, itemCount);
        }
    };

    public SectionFileListAdapter(FileListAdapter adapter) {
        mAdapter = adapter;

        mAdapter.registerAdapterDataObserver(mDataObserver);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == SECTION_TYPE) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.view_file_list_section, parent, false);
            return new ItemFileListSectionView(view);
        } else {
            return mAdapter.onCreateViewHolder(parent, viewType - 1);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        final SectionFileInfo item = mSections.get(position);

        if (isSectionHeaderPosition(position)) {
            if (viewHolder instanceof ItemFileListSectionView)
                ((ItemFileListSectionView) viewHolder).setItem(item);
            else
                throw new ClassCastException("Need use section class: ItemFileListSectionView");
        } else {
            if (viewHolder instanceof ItemFileListView)
                mAdapter.onBindViewHolder((ItemFileListView) viewHolder, sectionedPositionToPosition(position));
            else
                throw new ClassCastException("Need use list item class: ItemFileListView");
        }
    }

    @Override
    public int getItemCount() {
        return (mValid ? mAdapter.getItemCount() + mSections.size() : 0);
    }

    @Override
    public long getItemId(int position) {
        return isSectionHeaderPosition(position)
                ? Integer.MAX_VALUE - mSections.indexOfKey(position)
                : mAdapter.getItemId(sectionedPositionToPosition(position));
    }

    @Override
    public int getItemViewType(int position) {
        return isSectionHeaderPosition(position)
                ? SECTION_TYPE
                : mAdapter.getItemViewType(sectionedPositionToPosition(position)) + 1;
    }

    public boolean isSectionHeaderPosition(int position) {
        return mSections.get(position) != null;
    }

    public int sectionedPositionToPosition(int sectionedPosition) {
        if (isSectionHeaderPosition(sectionedPosition)) {
            return RecyclerView.NO_POSITION;
        }

        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).getSectionedPosition() > sectionedPosition) {
                break;
            }
            --offset;
        }
        return sectionedPosition + offset;
    }

    public int positionToSectionedPosition(int position) {
        int offset = 0;
        for (int i = 0; i < mSections.size(); i++) {
            if (mSections.valueAt(i).getFirstPosition() > position) {
                break;
            }
            ++offset;
        }
        return position + offset;
    }

    public void setSections(SectionFileInfo[] sections) {
        mSections.clear();

        if (sections == null || sections.length <= 0) {
            notifyDataSetChanged();
            return;
        }

        Arrays.sort(sections, new Comparator<SectionFileInfo>() {
            @Override
            public int compare(SectionFileInfo o, SectionFileInfo o1) {
                return (o.getFirstPosition() == o1.getFirstPosition())
                        ? 0
                        : ((o.getFirstPosition() < o1.getFirstPosition()) ? -1 : 1);
            }
        });

        int offset = 0; // offset positions for the headers we're adding
        for (SectionFileInfo section : sections) {
            section.setSectionedPosition(section.getFirstPosition() + offset);
            mSections.append(section.getSectionedPosition(), section);
            ++offset;
        }

        notifyDataSetChanged();
    }
}
