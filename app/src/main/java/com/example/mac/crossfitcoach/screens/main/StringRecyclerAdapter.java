/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
! *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.mac.crossfitcoach.screens.main;

import android.support.v4.app.NotificationCompat;
import android.support.wear.widget.WearableRecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mac.crossfitcoach.R;
import com.example.mac.crossfitcoach.utils.UtilsKt;

/**
 * Provides a binding from {@link NotificationCompat.Style} data set to views displayed within the
 * {@link WearableRecyclerView}.
 */
public class StringRecyclerAdapter extends
        WearableRecyclerView.Adapter<StringRecyclerAdapter.ViewHolder> {

    private static final String TAG = "StringRecyclerAdapter";

    private String[] menuItemsStrings;
    private String[] emojis;
    private final OnListItemClicked listener;



    /**
     * Provides reference to the views for each data item. We don't maintain a reference to the
     * {@link ImageView} (representing the icon), because it does not change for each item. We
     * wanted to keep the sample simple, but you could add extra code to customize each icon.
     */
    public static class ViewHolder extends WearableRecyclerView.ViewHolder {

        private final TextView menuItem;
        private final TextView emoji;

        public ViewHolder(View view) {
            super(view);
            menuItem =  view.findViewById(R.id.menu_item_text);
            emoji = view.findViewById(R.id.emoji_icon);
        }

        @Override
        public String toString() { return (String) menuItem.getText(); }
    }

    public StringRecyclerAdapter(String[] emojis, String[] dataSet,OnListItemClicked listener ) {
        menuItemsStrings = dataSet;
        this.emojis = emojis;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.view_main_menu_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        viewHolder.menuItem.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                listener.onItemListClicked(position);
            }
        });
        viewHolder.menuItem.setText(menuItemsStrings[position]);
        viewHolder.emoji.setText(emojis[position]);
        UtilsKt.addTouchEffect(viewHolder.menuItem);
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return menuItemsStrings.length;
    }


    public interface OnListItemClicked{

        void onItemListClicked(int index);
    }
}