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
    private static final int images[] = new int[] { R.drawable.image1, R.drawable.image2, R.drawable.image3,
            R.drawable.image4, R.drawable.image5, R.drawable.image6, R.drawable.image1, R.drawable.image2,
            R.drawable.image3, R.drawable.image4, R.drawable.image5, R.drawable.image6 };

    public SamplePagerAdapter(Context context) {
        mContext = context;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Card card = new Card(mContext);
        card.setText(menus[position]);
        card.setFootnote("card #" + position);
        card.addImage(images[position]);
        View view = card.getView();
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
