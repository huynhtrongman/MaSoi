package com.huynhtrongman.masoi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

class IntroViewPagerAdapter  extends PagerAdapter {
    Context context;
    int mLayouts[];

    public IntroViewPagerAdapter(Context context, int[] mLayouts) {
        this.context = context;
        this.mLayouts = mLayouts;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = mInflater.inflate(mLayouts[position], container, false);
        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return mLayouts.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}
