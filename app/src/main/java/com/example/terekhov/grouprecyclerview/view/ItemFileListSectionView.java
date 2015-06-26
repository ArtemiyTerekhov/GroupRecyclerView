package com.example.terekhov.grouprecyclerview.view;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.example.terekhov.grouprecyclerview.R;
import com.example.terekhov.grouprecyclerview.data.SectionFileInfo;

/**
 * Created by Artemiy Terekhov on 26.06.2015.
 */
public class ItemFileListSectionView extends RecyclerView.ViewHolder {

    private TextView mTextViewHeader;
    private SectionFileInfo mItem;

    public ItemFileListSectionView(View view) {
        super(view);

        mTextViewHeader = (TextView) view.findViewById(R.id.section_text);
    }

    public void setItem(SectionFileInfo item) {
        mItem = item;
        fillView(mItem);
    }

    private void fillView(SectionFileInfo item) {
        if (item == null)
            return;

        if (!TextUtils.isEmpty(item.getTitle())) {
            mTextViewHeader.setText(item.getTitle());
        } else {
            mTextViewHeader.setText(R.string.section_unknown);
        }

    }
}
