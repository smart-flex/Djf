package ru.smartflex.djf.controller.bean;

import org.apache.commons.lang3.StringUtils;

import java.util.StringTokenizer;

public class PhoneZone {
    private String prefix;
    private String zones;
    private String mainZone = null;

    public PhoneZone(String prefix, String zones) {
        this.prefix = prefix;
        this.zones = zones;
        if (StringUtils.isEmpty(prefix)) {
            throw new NullPointerException("Zone prefix is empty");
        }
        if (StringUtils.isEmpty(zones)) {
            throw new NullPointerException("Zone is empty");
        }
        init();
    }

    private void init() {
        StringTokenizer st = new StringTokenizer(zones, ",");
        while (st.hasMoreTokens()) {
            String zone = st.nextToken().trim();
            if (mainZone == null) {
                mainZone = zone;
                break;
            }
        }
    }

    public String getPrefix() {
        return prefix;
    }

    public int getPrefixLen() {
        return prefix.length();
    }

    public String getZones() {
        return zones;
    }

    public String getMainZone() {
        return mainZone;
    }

    @Override
    public String toString() {
        return "PhoneZone{" +
                "prefix='" + prefix + '\'' +
                ", mainZone='" + mainZone + '\'' +
                '}';
    }
}
