package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DateHelper;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.model.Pack;
import uk.co.jakelee.cityflow.model.ShopItem;
import uk.co.jakelee.cityflow.model.Text;

public class StoryActivity extends Activity {
    private DisplayHelper dh;
    private ViewPager mViewPager;
    private CustomPagerAdapter mCustomPagerAdapter;
    private int numPacks = 0;
    private int selectedPack = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story);
        SoundHelper.getInstance(this).playOrResumeMusic(SoundHelper.AUDIO.main);
        dh = DisplayHelper.getInstance(this);
        numPacks = Pack.listAll(Pack.class).size();

        mCustomPagerAdapter = new CustomPagerAdapter(this);

        mViewPager = (ViewPager) findViewById(R.id.packScroller);
        mViewPager.setClipToPadding(false);
        mViewPager.setPadding(150, 0, 150, 0);
        mViewPager.setPageMargin(25);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            public void onPageSelected(int position) {
                selectedPack = position + 1;
                displayPackInfo();
            }
        });
        mViewPager.setAdapter(mCustomPagerAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        displayPackInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SoundHelper.stopIfExiting(this);
    }

    private void displayPackInfo() {
        final Pack pack = Pack.getPack(selectedPack);
        boolean isUnlocked = pack.isUnlocked();
        boolean isUnlockable = pack.isUnlockable();

        ((TextView)findViewById(R.id.packName)).setText(pack.getName());
        ((TextView)findViewById(R.id.packPuzzleCount)).setText(Integer.toString(pack.getMaxStars() / 3) + " puzzles");

        findViewById(R.id.unlockedPackContainer).setVisibility(isUnlocked ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.unlockablePackContainer).setVisibility(!isUnlocked && isUnlockable ? View.VISIBLE : View.INVISIBLE);
        findViewById(R.id.lockedPackContainer).setVisibility(!isUnlocked && !isUnlockable ? View.VISIBLE : View.INVISIBLE);

        ((TextView)findViewById(R.id.actionButton)).setText(Text.get(isUnlocked ? "WORD_OPEN" : "WORD_UNLOCK"));
        findViewById(R.id.actionButton).setVisibility(isUnlocked || isUnlockable ? View.VISIBLE : View.GONE);

        if (isUnlocked) {
            ((TextView)findViewById(R.id.unlockedPackStars)).setText(String.format(Locale.ENGLISH, Text.get("UI_PACK_UNLOCKED_STARS"), pack.getCurrentStars(), pack.getMaxStars()));
            ((TextView)findViewById(R.id.unlockedPackTime)).setText(String.format(Locale.ENGLISH, Text.get("UI_PACK_UNLOCKED_TIME"), pack.getCurrentTime() > 0 ? DateHelper.getPuzzleTimeString(pack.getCurrentTime()) : "N/A"));
            ((TextView)findViewById(R.id.unlockedPackMoves)).setText(String.format(Locale.ENGLISH, Text.get("UI_PACK_UNLOCKED_MOVES"), pack.getCurrentMoves() > 0 ? Integer.toString(pack.getCurrentMoves()) : "N/A"));
            findViewById(R.id.actionButton).setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(getApplicationContext(), PackActivity.class);
                    intent.putExtra(Constants.INTENT_PACK, pack.getPackId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            });
        } else if (pack.isUnlockable()) {
            Pack previousPack = Pack.getPack(selectedPack - 1);
            ((TextView)findViewById(R.id.unlockablePackHeader)).setText(Text.get("UI_PACK_UNLOCKABLE_HEADER"));
            ((TextView)findViewById(R.id.unlockablePackInstruction)).setText(String.format(Locale.ENGLISH, Text.get("UI_PACK_UNLOCKABLE_INSTRUCTION"), previousPack.getName(), previousPack.getCurrentStars(), previousPack.getMaxStars()));
            findViewById(R.id.actionButton).setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    ShopItem packItem = ShopItem.getPackItem(pack.getPackId());
                    Intent intent = new Intent(getApplicationContext(), ShopActivity.class);
                    intent.putExtra(Constants.INTENT_ITEM, packItem.getItemId());
                    intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(intent);
                }
            });
        } else {
            ((TextView)findViewById(R.id.lockedPackDescription)).setText(pack.getUnlockChallenge());
        }
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
            return numPacks;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            View itemView = mLayoutInflater.inflate(R.layout.custom_pager_item, container, false);

            ImageView imageView = (ImageView) itemView.findViewById(R.id.imageView);
            imageView.setImageResource(dh.getPackDrawableID(position + 1));

            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((LinearLayout) object);
        }
    }
}
