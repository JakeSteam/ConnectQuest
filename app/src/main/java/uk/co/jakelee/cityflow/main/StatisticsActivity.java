package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.components.TextViewFont;
import uk.co.jakelee.cityflow.helper.SoundHelper;
import uk.co.jakelee.cityflow.helper.StatisticHelper;
import uk.co.jakelee.cityflow.model.Statistic;
import uk.co.jakelee.cityflow.model.Text;

public class StatisticsActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics);
        SoundHelper.keepPlayingMusic = true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        displayStatistics();
    }

    @Override
    protected void onStop() {
        super.onStop();

        SoundHelper.stopIfExiting(this);
    }

    private void displayStatistics() {
        LinearLayout container = (LinearLayout) findViewById(R.id.statisticsContainer);
        container.removeAllViews();

        ((TextView)findViewById(R.id.statisticsTitle)).setText(Text.get("DIALOG_STATISTICS"));

        // Get sorted map of name + value
        List<Statistic> statistics = Statistic.listAll(Statistic.class);
        Map<String,String> statisticsInfo = new TreeMap<>();
        for (int i = 0; i < statistics.size(); i++) {
            Statistic statistic = statistics.get(i);
            statisticsInfo.put(statistic.getName(), StatisticHelper.getStatisticString(statistic));
        }

        // Create row for each
        for (Map.Entry<String, String> statistic : statisticsInfo.entrySet()) {
            TableRow tableRow = new TableRow(this);
            tableRow.addView(createTextView(statistic.getKey(), true));
            tableRow.addView(createTextView(statistic.getValue(), false));
            container.addView(tableRow);
        }
    }

    private TextView createTextView(String text, boolean isHeader) {
        TextViewFont textView = new TextViewFont(this);
        textView.setTextColor(isHeader ? Color.DKGRAY : Color.GRAY);
        textView.setTextSize(20);
        textView.setText(text);
        return textView;
    }

    public void closePopup (View v) {
        this.finish();
    }
}