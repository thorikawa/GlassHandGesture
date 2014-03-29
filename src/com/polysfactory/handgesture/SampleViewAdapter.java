package com.polysfactory.handgesture;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;
import com.google.android.glass.widget.CardScrollAdapter;

public class SampleViewAdapter extends CardScrollAdapter {

    private Context mContext;
    private static final String menus[] = new String[] { "Google", "Take picture", "Record video", "Message", "Google",
            "Take picture", "Record video", "Message", "Google", "Take picture", "Record video", "Message" };

    public SampleViewAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int findIdPosition(Object arg0) {
        return 0;
    }

    @Override
    public int findItemPosition(Object arg0) {
        return 0;
    }

    @Override
    public int getCount() {
        return 11;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public int getHomePosition() {
        return 5;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Card card = new Card(mContext);
        card.setText(menus[position]);
        card.setFootnote("card #" + position);
        return card.toView();
    }
}
