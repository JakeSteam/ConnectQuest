package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.thomashaertel.widget.MultiSpinner;

import java.util.ArrayList;
import java.util.List;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.helper.Constants;
import uk.co.jakelee.cityflow.helper.DisplayHelper;
import uk.co.jakelee.cityflow.model.Text;
import uk.co.jakelee.cityflow.model.Tile;
import uk.co.jakelee.cityflow.model.TileType;

public class TilePickerActivity extends Activity {
    private DisplayHelper dh;
    private Tile tile;

    private MultiSpinner spinner;
    private ArrayAdapter<String> adapter;

    private ArrayList<Integer> selectedEnvironments = new ArrayList<>();
    private ArrayList<Integer> selectedFlows = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile_picker);
        dh = DisplayHelper.getInstance(this);

        Intent intent = getIntent();
        Long tileId = intent.getLongExtra(Constants.INTENT_TILE, 0);
        if (tileId > 0) {
            tile = Tile.get(tileId);
        }

        displayTilePicker();
    }

    private void displayTilePicker() {
        populateEnvironmentPicker();
        populateFlowPicker();

        populateTilePicker();
    }

    private void populateEnvironmentPicker() {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        for (int i = Constants.ENVIRONMENT_MIN; i <= Constants.ENVIRONMENT_MAX; i++) {
            adapter.add(Text.get("ENVIRONMENT_" + i + "_NAME"));
        }

        spinner = (MultiSpinner) findViewById(R.id.environmentPicker);
        spinner.setAdapter(adapter, false, environmentSelected);

        boolean[] selectedItems = new boolean[adapter.getCount()];
        selectedItems[1] = true;
        spinner.setSelected(selectedItems);
    }

    private MultiSpinner.MultiSpinnerListener environmentSelected = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] environments) {
            selectedEnvironments.clear();
            for (int environmentId = 0; environmentId < environments.length; environmentId++) {
                if (environments[environmentId]) {
                    selectedEnvironments.add(environmentId);
                }
            }

            // and update the selected item display
            populateTilePicker();
        }
    };

    private void populateFlowPicker() {
        String flowString = "Flows: ";
        for (int i = Constants.FLOW_MIN; i <= Constants.FLOW_MAX; i++) {
            flowString += Text.get("FLOW_" + i + "_NAME") + ", ";
        }
        ((TextView)findViewById(R.id.flowPicker)).setText(flowString);
    }

    private void populateTilePicker() {
        LinearLayout tilePicker = (LinearLayout)findViewById(R.id.tileContainer);
        tilePicker.removeAllViews();

        String whereClause = String.format("%1$s AND %2$s",
                getEnvironmentSQL(),
                getFlowSQL());
        List<TileType> tileTypes = TileType.find(TileType.class, whereClause);

        // foreach tile meeting criteria, set tag to tiletype, and display at default rotation in a grid
        for (TileType tileType : tileTypes) {
            ImageView tileImage = dh.createTileIcon(tileType.getTypeId(), 60, 60);
            tileImage.setTag(tileType.getTypeId());
            tileImage.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    clickTile(v);
                }
            });
            tilePicker.addView(tileImage);
        }
    }

    private String getEnvironmentSQL() {
        if (selectedEnvironments.size() == 0) {
            return "environment_id IS NOT NULL";
        }

        StringBuilder sb = new StringBuilder();
        for (int environmentId : selectedEnvironments) {
            sb.append(environmentId);
            sb.append(",");
        }

        String environmentListString = sb.toString().substring(0, sb.toString().length() - 1);
        return "environment_id IN (" + environmentListString + ")";
    }

    private String getFlowSQL() {
        if (selectedFlows.size() == 0) {
            return "flow_north IS NOT NULL";
        }

        StringBuilder sb = new StringBuilder();
        for (int flowId : selectedFlows) {
            sb.append(flowId);
            sb.append(",");
        }

        String flowListString = sb.toString().substring(0, sb.toString().length() - 1);
        return String.format("(flow_north IN (%1$s) OR flow_east IN (%1$s) OR flow_south IN (%1$s) OR flow_west IN (%1$s))", flowListString);
    }

    public void clickTile(View v) {
        tile.setTileTypeId((int)v.getTag());
        tile.save();

        this.finish();
    }

    public void closePopup (View v) {
        this.finish();
    }
}
