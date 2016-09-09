package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.DisplayHelper;

public class StoryActivity extends Activity {
    private DisplayHelper dh;
    private ViewPager mViewPager;
    private CustomPagerAdapter mCustomPagerAdapter;
    int[] mResources = {
            R.drawable.puzzle_1,
            R.drawable.puzzle_2,
            R.drawable.puzzle_11,
            R.drawable.puzzle_12,
            R.drawable.puzzle_13,
            R.drawable.puzzle_14,
            R.drawable.puzzle_15,
            R.drawable.puzzle_16
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        dh = DisplayHelper.getInstance(this);

        mCustomPagerAdapter = new CustomPagerAdapter(this);

        mViewPager = (ViewPager) findViewById(R.id.packScroller);
        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(150, 0, 150, 0);
        mViewPager.setPageMargin(25);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(mCustomPagerAdapter);
    }

    class CustomPagerAdapter extends PagerAdapter {
        Context mContext;
        LayoutInflater mLayoutInflater;

        public CustomPagerAdapter(Context context) {
            mContext = context;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return mResources.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((LinearLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(mResources[position]);

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }

    /*public void openLeaderboard(View v) {
        if (GooglePlayHelper.IsConnected()) {
            if (v.getId() == R.id.timeLeaderboard && !selectedPack.getTimeLeaderboard().equals("")) {
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(GooglePlayHelper.mGoogleApiClient, selectedPack.getTimeLeaderboard()), GooglePlayHelper.RC_LEADERBOARDS);
            } else if (v.getId() == R.id.movesLeaderboard && !selectedPack.getMovesLeaderboard().equals("")) {
                startActivityForResult(Games.Leaderboards.getLeaderboardIntent(GooglePlayHelper.mGoogleApiClient, selectedPack.getMovesLeaderboard()), GooglePlayHelper.RC_LEADERBOARDS);
            }
        }
    }*/
}
