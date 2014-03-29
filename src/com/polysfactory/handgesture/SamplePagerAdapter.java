package com.polysfactory.handgesture;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.glass.app.Card;

public class SamplePagerAdapter extends PagerAdapter {

    private Context mContext;
    private static final String menus[] = new String[] { "Google", "Take picture", "Record video", "Message", "Google",
            "Take picture", "Record video", "Message", "Google", "Take picture", "Record video", "Message" };

    public SamplePagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Card card = new Card(mContext);
        card.setText(menus[position]);
        card.setFootnote("card #" + position);
        View view = card.toView();
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return 11;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
