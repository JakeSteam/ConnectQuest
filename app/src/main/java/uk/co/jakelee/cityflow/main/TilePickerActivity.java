package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.Arrays;
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
    private SharedPreferences prefs;
    private enum filters {Flow, Environment, Height}

    private ArrayList<Integer> selectedEnvironmentIDs = new ArrayList<>();
    private ArrayList<Integer> selectedFlowIDs = new ArrayList<>();
    private ArrayList<Integer> selectedHeightIDs = new ArrayList<>();

    private MultiSpinner environmentSpinner;
    private MultiSpinner flowSpinner;
    private MultiSpinner heightSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile_picker);
        dh = DisplayHelper.getInstance(this);
        prefs = getSharedPreferences("uk.co.jakelee.cityflow", MODE_PRIVATE);

        Intent intent = getIntent();
        Long tileId = intent.getLongExtra(Constants.INTENT_TILE, 0);
        if (tileId > 0) {
            tile = Tile.get(tileId);
        }

        loadSelectedIds("tilePickerEnvironments", filters.Environment);
        loadSelectedIds("tilePickerFlows", filters.Flow);
        loadSelectedIds("tilePickerHeights", filters.Height);

        populateEnvironmentPicker();
        populateFlowPicker();
        populateHeightPicker();

        populateTilePicker();
        populateText();
    }

    @Override
    protected void onStop() {
        super.onStop();

        prefs.edit().putString("tilePickerEnvironments", selectedEnvironmentIDs.toString()).apply();
        prefs.edit().putString("tilePickerFlows", selectedFlowIDs.toString()).apply();
        prefs.edit().putString("tilePickerHeights", selectedHeightIDs.toString()).apply();
    }

    private void populateText() {
        ((TextView) findViewById(R.id.areaLabel)).setText(Text.get("WORD_AREA"));
        ((TextView) findViewById(R.id.flowLabel)).setText(Text.get("WORD_FLOW"));
        ((TextView) findViewById(R.id.heightLabel)).setText(Text.get("WORD_HEIGHT"));
    }

    private void loadSelectedIds(String preferencesKey, filters filter) {
        String selectedIdString = prefs.getString(preferencesKey, "");
        if (!selectedIdString.equals("") && selectedIdString.length() > 3) {
            String trimmedSelectedIdsString = selectedIdString.substring(1, selectedIdString.length() - 1);
            List<String> selectedIds = Arrays.asList(trimmedSelectedIdsString.split(", "));
            for (String selectedId : selectedIds) {
                if (filter == filters.Environment) {
                    selectedEnvironmentIDs.add(Integer.parseInt(selectedId));
                } else if (filter == filters.Flow) {
                    selectedFlowIDs.add(Integer.parseInt(selectedId));
                } else if (filter == filters.Height) {
                    selectedHeightIDs.add(Integer.parseInt(selectedId));
                }
            }
        }

        if (filter == filters.Environment && selectedEnvironmentIDs.size() == 0) {
            selectedEnvironmentIDs.add(Constants.ENVIRONMENT_GRASS);
        } else if (filter == filters.Flow && selectedFlowIDs.size() == 0) {
            for (int i = 0; i <= Constants.FLOW_MAX; i++) {
                selectedFlowIDs.add(i);
            }
        } else if (filter == filters.Height && selectedHeightIDs.size() == 0) {
            for (int i = 0; i <= Constants.HEIGHT_MAX; i++) {
                selectedHeightIDs.add(i);
            }
        }
    }

    private void populateEnvironmentPicker() {
        int numEnvironments = (Constants.ENVIRONMENT_MAX - Constants.ENVIRONMENT_MIN) + 1;
        ArrayAdapter<String> envAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item);
        for (int i = 0; i < numEnvironments; i++) {
            envAdapter.add(Text.get("ENVIRONMENT_" + i + "_NAME"));
        }
        boolean[] selectedEnvironments = new boolean[numEnvironments];
        for (Integer environmentId : selectedEnvironmentIDs) {
            selectedEnvironments[environmentId] = true;
        }

        environmentSpinner = (MultiSpinner) findViewById(R.id.environmentPicker);
        environmentSpinner.setAdapter(envAdapter, false, environmentSelected);
        environmentSpinner.setSelected(selectedEnvironments);
    }

    private void populateFlowPicker() {
        int numFlows = (Constants.FLOW_MAX - Constants.FLOW_MIN) + 1;
        ArrayAdapter<String> flowAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item);
        for (int i = 0; i < numFlows; i++) {
            flowAdapter.add(Text.get("FLOW_" + i + "_NAME"));
        }

        boolean[] selectedFlows = new boolean[numFlows];
        for (Integer flowId : selectedFlowIDs) {
            selectedFlows[flowId] = true;
        }

        flowSpinner = (MultiSpinner) findViewById(R.id.flowPicker);
        flowSpinner.setAdapter(flowAdapter, false, flowSelected);
        flowSpinner.setSelected(selectedFlows);
        flowSpinner.setText(Text.get("WORD_ALL"));
    }

    private void populateHeightPicker() {
        int numHeights = (Constants.HEIGHT_MAX - Constants.HEIGHT_MIN) + 1;
        ArrayAdapter<String> flowAdapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item);
        for (int i = 0; i < numHeights; i++) {
            flowAdapter.add(Text.get("HEIGHT_" + i + "_NAME"));
        }

        boolean[] selectedHeights = new boolean[numHeights];
        for (Integer heightId : selectedHeightIDs) {
            selectedHeights[heightId] = true;
        }

        heightSpinner = (MultiSpinner) findViewById(R.id.heightPicker);
        heightSpinner.setAdapter(flowAdapter, false, heightSelected);
        heightSpinner.setSelected(selectedHeights);
        heightSpinner.setText(Text.get("WORD_ALL"));
    }

    private MultiSpinner.MultiSpinnerListener environmentSelected = new MultiSpinner.MultiSpinnerListener() {
        public void onItemsSelected(boolean[] environments) {
            selectedEnvironmentIDs.clear();
            for (int environmentId = 0; environmentId < environments.length; environmentId++) {
                if (environments[environmentId]) {
                    selectedEnvironmentIDs.add(environmentId);
                }
            }

            if (selectedEnvironmentIDs.size() == environments.length) {
                environmentSpinner.setText("All");
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

            if (selectedFlowIDs.size() == flows.length) {
                flowSpinner.setText("All");
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

            if (selectedHeightIDs.size() == heights.length) {
                heightSpinner.setText("All");
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
