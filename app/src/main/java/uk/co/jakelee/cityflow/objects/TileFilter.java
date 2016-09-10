package uk.co.jakelee.cityflow.objects;

import java.util.ArrayList;

public class TileFilter {
    public int min;
    public int max;
    public String prefix;
    public ArrayList<Integer> selected = new ArrayList<>();
    public int spinnerId;
    public String preferenceKey;

    public TileFilter(int min, int max, String prefix, int spinnerId, String preferenceKey) {
        this.min = min;
        this.max = max;
        this.prefix = prefix;
        this.spinnerId = spinnerId;
        this.preferenceKey = preferenceKey;
    }
}
