package ru.smartflex.djf;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Set;

public class SimpleAccessible implements IAccessible {

    private String[] markers;
    private Set<String> markersSet = new HashSet<String>();
    private Marker marker = null;

    @SuppressWarnings({"UseBulkOperation", "ManualArrayToCollectionCopy"})
    public SimpleAccessible(String... markers) {
        this.markers = markers;
        if (markers != null) {
            for (String mrk : markers) {
                markersSet.add(mrk);
            }
        }
    }

    @Override
    public boolean isAccessible(String[] infos) {
        boolean fok = false;
        if (infos != null) {
            for (String inf : infos) {
                if (markersSet.contains(inf)) {
                    fok = true;
                    break;
                }
            }
        }
        return fok;
    }

    public class Marker implements Enumeration<String> {

        private int index = -1;

        @SuppressWarnings("RedundantIfStatement")
        @Override
        public boolean hasMoreElements() {
            if (markers != null && markers.length > 0) {
                index++;
                if (index < markers.length) {
                    return true;
                }
            }
            return false;
        }

        @Override
        public String nextElement() {
            if (markers != null && index < markers.length) {
                return markers[index];
            } else {
                throw new NoSuchElementException();
            }
        }

        void resetEnumeration() {
            index = -1;
        }
    }

    @Override
    public Enumeration<String> elements() {
        if (marker == null) {
            marker = new Marker();
        } else {
            marker.resetEnumeration();
        }
        return marker;
    }
}
