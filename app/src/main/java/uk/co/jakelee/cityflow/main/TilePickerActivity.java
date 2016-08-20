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

    private MultiSpinner spinner;
    private ArrayAdapter<String> envAdapter;
    private ArrayAdapter<String> flowAdapter;

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
        populateText();
    }

    private void populateText() {
        ((TextView) findViewById(R.id.areaLabel)).setText(Text.get("WORD_AREA"));
        ((TextView) findViewById(R.id.flowLabel)).setText(Text.get("WORD_FLOW"));
    }

    private void displayTilePicker() {
        populateEnvironmentPicker();
        populateFlowPicker();

        populateTilePicker();
    }

    private void populateEnvironmentPicker() {
        envAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        for (int i = Constants.ENVIRONMENT_MIN; i <= Constants.ENVIRONMENT_MAX; i++) {
            envAdapter.add(Text.get("ENVIRONMENT_" + i + "_NAME"));
        }

        spinner = (MultiSpinner) findViewById(R.id.environmentPicker);
        spinner.setAdapter(envAdapter, false, environmentSelected);

        boolean[] selectedItems = new boolean[envAdapter.getCount()];
        selectedItems[1] = true;
        spinner.setSelected(selectedItems);
    }

    private void populateFlowPicker() {
        flowAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item);
        for (int i = Constants.FLOW_MIN; i <= Constants.FLOW_MAX; i++) {
            flowAdapter.add(Text.get("FLOW_" + i + "_NAME"));
        }

        spinner = (MultiSpinner) findViewById(R.id.flowPicker);
        spinner.setAdapter(flowAdapter, false, flowSelected);

        boolean[] selectedItems = new boolean[flowAdapter.getCount()];
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

            populateTilePicker();
        }
    };

    private MultiSpinner.MultiSpinnerListener flowSelected = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] flows) {
            selectedFlows.clear();
            for (int flowId = 0; flowId < flows.length; flowId++) {
                if (flows[flowId]) {
                    selectedFlows.add(flowId);
                }
            }

            populateTilePicker();
        }
    };

    private void populateTilePicker() {
        TableLayout tileContainer = (TableLayout)findViewById(R.id.tileContainer);
        tileContainer.removeAllViews();

        String whereClause = String.format("%1$s AND %2$s ORDER BY environment_id ASC",
                getEnvironmentSQL(),
                getFlowSQL());
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
