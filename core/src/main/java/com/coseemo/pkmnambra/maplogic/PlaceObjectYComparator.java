package com.coseemo.pkmnambra.maplogic;

import java.util.Comparator;
import com.coseemo.pkmnambra.maplogic.YSortable;

public class PlaceObjectYComparator implements Comparator<YSortable> {
    @Override
    public int compare(YSortable o1, YSortable o2) {
        if (o1.getPlaceY() < o2.getPlaceY()) {
            return -1;
        } else if (o1.getPlaceY() > o2.getPlaceY()) {
            return 1;
        }
        return 0;
    }
}
