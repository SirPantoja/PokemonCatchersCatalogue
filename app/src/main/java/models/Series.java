package models;

import android.os.Parcel;
import android.widget.TextView;

import com.thoughtbot.expandablerecyclerview.models.ExpandableGroup;

import java.util.List;

public class Series extends ExpandableGroup<Set> {
    public Series(String title, List<Set> items) {
        super(title, items);
    }
}
