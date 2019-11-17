package ru.smartflex.djf;

import java.util.Enumeration;

public interface IAccessible {

    /**
     * Defines is accessible that item(s) or not.
     */
    boolean isAccessible(String[] infos);

    Enumeration<String> elements();
}
