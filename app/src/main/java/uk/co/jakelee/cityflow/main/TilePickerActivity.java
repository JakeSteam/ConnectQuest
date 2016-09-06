package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
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

    private ArrayList<Integer> selectedEnvironmentIDs = new ArrayList<>();
    private ArrayList<Integer> selectedFlowIDs = new ArrayList<>();
    private ArrayList<Integer> selectedHeightIDs = new ArrayList<>();

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
        populateText();
    }

    private void populateText() {
        ((TextView) findViewById(R.id.areaLabel)).setText(Text.get("WORD_AREA"));
        ((TextView) findViewById(R.id.flowLabel)).setText(Text.get("WORD_FLOW"));
        ((TextView) findViewById(R.id.heightLabel)).setText(Text.get("WORD_HEIGHT"));
    }

    private void displayTilePicker() {
        populateEnvironmentPicker();
        populateFlowPicker();
        populateHeightPicker();

        populateTilePicker();
    }

    private void populateEnvironmentPicker() {
        int numEnvironments = (Constants.ENVIRONMENT_MAX - Constants.ENVIRONMENT_MIN) + 1;
        ArrayAdapter<String> envAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item);
        for (int i = 0; i < numEnvironments; i++) {
            envAdapter.add(Text.get("ENVIRONMENT_" + i + "_NAME"));
        }

        boolean[] selectedEnvironments = new boolean[numEnvironments];
        selectedEnvironments[Constants.ENVIRONMENT_GRASS] = true;
        selectedEnvironmentIDs.add(Constants.ENVIRONMENT_GRASS);

        MultiSpinner spinner = (MultiSpinner) findViewById(R.id.environmentPicker);
        spinner.setAdapter(envAdapter, false, environmentSelected);
        spinner.setSelected(selectedEnvironments);
    }

    private void populateFlowPicker() {
        int numFlows = (Constants.FLOW_MAX - Constants.FLOW_MIN) + 1;
        boolean[] selectedFlows = new boolean[numFlows];

        ArrayAdapter<String> flowAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item);
        for (int i = 0; i < numFlows; i++) {
            flowAdapter.add(Text.get("FLOW_" + i + "_NAME"));
            selectedFlows[i] = true;
        }

        MultiSpinner spinner = (MultiSpinner) findViewById(R.id.flowPicker);
        spinner.setAdapter(flowAdapter, false, flowSelected);
        spinner.setSelected(selectedFlows);
        spinner.setText(Text.get("WORD_ALL"));
    }

    private void populateHeightPicker() {
        int numHeights = (Constants.HEIGHT_MAX - Constants.HEIGHT_MIN) + 1;
        boolean[] selectedHeights = new boolean[numHeights];

        ArrayAdapter<String> flowAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item);
        for (int i = 0; i < numHeights; i++) {
            flowAdapter.add(Text.get("HEIGHT_" + i + "_NAME"));
            selectedHeights[i] = true;
        }

        MultiSpinner spinner = (MultiSpinner) findViewById(R.id.heightPicker);
        spinner.setAdapter(flowAdapter, false, heightSelected);
        spinner.setSelected(selectedHeights);
        spinner.setText(Text.get("WORD_ALL"));
    }

    private MultiSpinner.MultiSpinnerListener environmentSelected = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] environments) {
            selectedEnvironmentIDs.clear();
            for (int environmentId = 0; environmentId < environments.length; environmentId++) {
                if (environments[environmentId]) {
                    selectedEnvironmentIDs.add(environmentId);
                }
            }

            populateTilePicker();
        }
    };

    private MultiSpinner.MultiSpinnerListener flowSelected = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] flows) {
            selectedFlowIDs.clear();
            for (int flowId = 0; flowId < flows.length; flowId++) {
                if (flows[flowId]) {
                    selectedFlowIDs.add(flowId);
                }
            }

            populateTilePicker();
        }
    };

    private MultiSpinner.MultiSpinnerListener heightSelected = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] heights) {
            selectedHeightIDs.clear();
            for (int heightId = 0; heightId < heights.length; heightId++) {
                if (heights[heightId]) {
                    selectedHeightIDs.add(heightId);
                }
            }

            populateTilePicker();
        }
    };

    private void populateTilePicker() {
        TableLayout tileContainer = (TableLayout)findViewById(R.id.tileContainer);
        tileContainer.removeAllViews();

        String whereClause = String.format("%1$s AND %2$s AND %3$s AND status = %4$d ORDER BY environment_id ASC",
                getEnvironmentSQL(),
                getFlowSQL(),
                getHeightSQL(),
                Constants.TILE_STATUS_UNLOCKED);
        List<TileType> tileTypes = TileType.find(TileType.class, whereClause);

        int numTiles = tileTypes.size();
        TableRow row = new TableRow(this);
        for (int tileIndex = 1; tileIndex <= numTiles; tileIndex++) {
            TileType tileType = tileTypes.get(tileIndex - 1);

            ImageView tileImage = dh.createTileIcon(tileType.getTypeId(), 80, 80);
            tileImage.setTag(tileType.getTypeId());
            tileImage.setOnClickListener(new Button.OnClickListener() {
                public void onClick(View v) {
                    clickTile(v);
                }
            });
            row.addView(tileImage);

            if (tileIndex % 3 == 0 || tileIndex == numTiles) {
                tileContainer.addView(row);
                row = new TableRow(this);
            }
        }
    }

    private String getEnvironmentSQL() {
        if (selectedEnvironmentIDs.size() == 0) {
            return "environment_id IS NOT NULL";
        }

        StringBuilder sb = new StringBuilder();
        for (int environmentId : selectedEnvironmentIDs) {
            sb.append(environmentId);
            sb.append(",");
        }

        String environmentListString = sb.toString().substring(0, sb.toString().length() - 1);
        return "environment_id IN (" + environmentListString + ")";
    }

    private String getFlowSQL() {
        if (selectedFlowIDs.size() == 0) {
            return "flow_north IS NOT NULL";
        }

        StringBuilder sb = new StringBuilder();
        for (int flowId : selectedFlowIDs) {
            sb.append(flowId);
            sb.append(",");
        }

        String flowListString = sb.toString().substring(0, sb.toString().length() - 1);
        return String.format("(flow_north IN (%1$s) OR flow_east IN (%1$s) OR flow_south IN (%1$s) OR flow_west IN (%1$s))", flowListString);
    }

    private String getHeightSQL() {
        if (selectedHeightIDs.size() == 0) {
            return "height_north IS NOT NULL";
        }

        StringBuilder sb = new StringBuilder();
        for (int heightId : selectedHeightIDs) {
            sb.append(heightId);
            sb.append(",");
        }

        String heightListString = sb.toString().substring(0, sb.toString().length() - 1);
        return String.format("(height_north IN (%1$s) OR height_east IN (%1$s) OR height_south IN (%1$s) OR height_west IN (%1$s))", heightListString);
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
