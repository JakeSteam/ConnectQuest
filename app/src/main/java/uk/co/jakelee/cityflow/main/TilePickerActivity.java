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
import uk.co.jakelee.cityflow.objects.TileFilter;

public class TilePickerActivity extends Activity {
    private DisplayHelper dh;
    private Tile tile;
    private SharedPreferences prefs;
    private final String environmentsPreferenceID = "tilePickerEnvironments";
    private final String flowsPreferenceID = "tilePickerFlows";
    private final String heightsPreferenceID = "tilePickerHeights";

    private ArrayList<TileFilter> filters = new ArrayList<>();

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

        filters.add(new TileFilter(Constants.ENVIRONMENT_MIN, Constants.ENVIRONMENT_MAX, "ENVIRONMENT_", R.id.environmentPicker, environmentsPreferenceID));
        filters.add(new TileFilter(Constants.FLOW_MIN, Constants.FLOW_MAX, "FLOW_", R.id.flowPicker, flowsPreferenceID));
        filters.add(new TileFilter(Constants.HEIGHT_MIN, Constants.HEIGHT_MAX, "HEIGHT_", R.id.heightPicker, heightsPreferenceID));

        for (TileFilter filter : filters) {
            loadSelected(filter);
            populatePicker(filter);
        }

        populateTilePicker();
        populateText();
    }

    @Override
    protected void onStop() {
        super.onStop();

        for (TileFilter filter : filters) {
            prefs.edit().putString(filter.preferenceKey, filter.selected.toString()).apply();
        }
    }

    private void populateText() {
        ((TextView) findViewById(R.id.areaLabel)).setText(Text.get("WORD_AREA"));
        ((TextView) findViewById(R.id.flowLabel)).setText(Text.get("WORD_FLOW"));
        ((TextView) findViewById(R.id.heightLabel)).setText(Text.get("WORD_HEIGHT"));
    }

    private void loadSelected(TileFilter filter) {
        String selectedIdString = prefs.getString(filter.preferenceKey, "");
        if (!selectedIdString.equals("") && selectedIdString.length() >= 3) {
            String trimmedSelectedIdsString = selectedIdString.substring(1, selectedIdString.length() - 1);
            List<String> selectedIds = Arrays.asList(trimmedSelectedIdsString.split(", "));
            for (String selectedId : selectedIds) {
                filter.selected.add(Integer.parseInt(selectedId));
            }
        }

        if (filter.preferenceKey.equals(environmentsPreferenceID) && filter.selected.size() == 0) {
            filter.selected.add(Constants.ENVIRONMENT_GRASS);
        } else if ((filter.preferenceKey.equals(flowsPreferenceID) || filter.preferenceKey.equals(heightsPreferenceID)) && filter.selected.size() == 0) {
            for (int i = 0; i <= filter.max; i++) {
                filter.selected.add(i);
            }
        }
    }

    private void populatePicker(final TileFilter filter) {
        int numOptions = (filter.max - filter.min) + 1;
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.custom_spinner_item);
        for (int i = 0; i < numOptions; i++) {
            adapter.add(Text.get(filter.prefix + i + "_NAME"));
        }
        boolean[] selectedOptions = new boolean[numOptions];
        for (Integer id : filter.selected) {
            selectedOptions[id] = true;
        }

        final MultiSpinner spinner = (MultiSpinner) findViewById(filter.spinnerId);
        spinner.setAdapter(adapter, false, new MultiSpinner.MultiSpinnerListener() {
            public void onItemsSelected(boolean[] selectedIds) {
                filter.selected.clear();
                for (int environmentId = 0; environmentId < selectedIds.length; environmentId++) {
                    if (selectedIds[environmentId]) {
                        filter.selected.add(environmentId);
                    }
                }

                if (filter.selected.size() == selectedIds.length) {
                    spinner.setText("All");
                }

                populateTilePicker();
            }
        });
        spinner.setSelected(selectedOptions);
    }

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
        TileFilter environmentFilter = filters.get(0);
        if (environmentFilter.selected.size() == 0) {
            return "environment_id IS NOT NULL";
        }

        StringBuilder sb = new StringBuilder();
        for (int environmentId : environmentFilter.selected) {
            sb.append(environmentId);
            sb.append(",");
        }

        String environmentListString = sb.toString().substring(0, sb.toString().length() - 1);
        return "environment_id IN (" + environmentListString + ")";
    }

    private String getFlowSQL() {
        TileFilter flowFilter = filters.get(1);
        if (flowFilter.selected.size() == 0) {
            return "flow_north IS NOT NULL";
        }

        StringBuilder sb = new StringBuilder();
        for (int flowId : flowFilter.selected) {
            sb.append(flowId);
            sb.append(",");
        }

        String flowListString = sb.toString().substring(0, sb.toString().length() - 1);
        return String.format("(flow_north IN (%1$s) OR flow_east IN (%1$s) OR flow_south IN (%1$s) OR flow_west IN (%1$s))", flowListString);
    }

    private String getHeightSQL() {
        TileFilter heightFilter = filters.get(2);
        if (heightFilter.selected.size() == 0) {
            return "height_north IS NOT NULL";
        }

        StringBuilder sb = new StringBuilder();
        for (int heightId : heightFilter.selected) {
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
