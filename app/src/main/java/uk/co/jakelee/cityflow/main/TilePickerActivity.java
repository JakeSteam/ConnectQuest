package uk.co.jakelee.cityflow.main;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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

    private int[] environments = {1, 2};
    private int[] flows = {1, 2, 3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tile_picker);
        dh = DisplayHelper.getInstance(this);

        Intent intent = getIntent();
        int tileId = intent.getIntExtra(Constants.INTENT_TILE, 0);
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
        String environmentString = "Envs: ";
        for (int i = Constants.ENVIRONMENT_MIN; i <= Constants.ENVIRONMENT_MAX; i++) {
            environmentString += Text.get("ENVIRONMENT_" + i + "_NAME") + ", ";
        }
        ((TextView)findViewById(R.id.environmentPicker)).setText(environmentString);
    }

    private void populateFlowPicker() {
        String flowString = "Flows: ";
        for (int i = Constants.FLOW_MIN; i <= Constants.FLOW_MAX; i++) {
            flowString += Text.get("FLOW_" + i + "_NAME") + ", ";
        }
        ((TextView)findViewById(R.id.flowPicker)).setText(flowString);
    }

    private void populateTilePicker() {
        LinearLayout tilePicker = (LinearLayout)findViewById(R.id.tileContainer);
        String environmentString = Arrays.toString(environments).replace("[","(").replace("]",")");
        String flowString = Arrays.toString(flows).replace("[","(").replace("]",")");

        List<TileType> tileTypes = TileType.find(TileType.class, String.format("environment_id IN %1$s AND (flow_north IN %2$s OR flow_east IN %2$s OR flow_south IN %2$s OR flow_west IN %2$s)",
                environmentString,
                flowString));

        for (TileType tileType : tileTypes) {
            ImageView tileImage = new ImageView(this);
            // actually give it an image
            tileImage.setTag(tileType.getTypeId());

        }
        // foreach tile meeting criteria, set tag to tiletype, and display at default rotation in a grid
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
