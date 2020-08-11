package ru.smartflex.djf.controller.helper;

import org.apache.commons.lang3.StringUtils;
import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.bean.PhoneBag;
import ru.smartflex.djf.controller.bean.PhoneZone;

import java.io.InputStream;
import java.util.*;

import static ru.smartflex.djf.tool.OtherUtil.*;

public class PhoneZoneUtil {
    private static String DEFAULT_ZONE_PREFIX = "7";
    private static Map<String, PhoneZone> treeMap = new TreeMap<String, PhoneZone>();
    private static Map<String, Integer> zoneMaxLen = new HashMap<String, Integer>();

    static {
        InputStream is = null;
        try {
            is = getBodyAsStream(null, SFConstants.PHONE_ZONE_PROP_FILE_NAME);
            PropertyResourceBundle bundle = new PropertyResourceBundle(is);
            Enumeration enm = bundle.getKeys();
            while (enm.hasMoreElements()) {
                String key = (String) enm.nextElement();
                String zones = bundle.getString(key);
                treeMap.put(key, new PhoneZone(key, zones));
                String first = key.substring(0,1);
                Integer maxLen = zoneMaxLen.get(first);
                if (maxLen == null) {
                    zoneMaxLen.put(first, key.length());
                } else {
                    if (key.length() > maxLen) {
                        zoneMaxLen.put(first, key.length());
                    }
                }
            }
        } catch (Exception e) {
            SFLogger.error("Error by reading phone zones", e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (Exception ee) {
                }
            }
        }
    }

    public static boolean isPhoneFormattedValid(String phone) {
        boolean fok = true;
        if (!StringUtils.isEmpty(phone)) {
            String rawNoSpaces = phone.replace(" ", "");
            int l = rawNoSpaces.length();
            if (!(l == 12 || l == 11 || l == 10 || isLocalPhoneNumber(l))) {
                // not full and not local phone number
                fok = false;
            }
        }
        return fok;
    }

    private static boolean isLocalPhoneNumber(int l) {
        if (l == 7 || l == 6 || l == 5) {
            return true;
        }
        return false;
    }

    private static PhoneZone defineZone(String _toKey) {
        PhoneZone zone = null;
        Integer maxLen = zoneMaxLen.get(_toKey.substring(0, 1));
        if (maxLen != null) {
            for (int i = maxLen; i >= 0; i--) {
                if (_toKey.length() >= i) {
                    String key = _toKey.substring(0, i);
                    zone = treeMap.get(key);
                    if (zone != null) {
                        break;
                    }
                }
            }
        }
        return zone;
    }

    public static PhoneBag formatPhoneWithZone(String finishedText) {
        return formatPhoneWithZone(finishedText, null, -1, true);
    }

    public static PhoneBag formatPhoneWithZone(String newText, String prevText, int ind) {
        return formatPhoneWithZone(newText, prevText, ind, false);
    }

    private static PhoneBag formatPhoneWithZone(String newText, String prevText, int ind, boolean finished) {
        PhoneBag phoneBag = null;
        String raw = null;
        if (prevText != null) {
            if (ind == prevText.length()) {
                raw =  prevText + newText;
            } else {
                if (ind == 0) {
                    raw = newText + prevText;
                } else {
                    raw = prevText.substring(0, ind) + newText + prevText.substring(ind);
                }
            }
        } else {
            raw = newText;
        }
        if (!StringUtils.isEmpty(raw)) {
            String rawNoSpaces = raw.replace(" ", "");
            String _toKey = rawNoSpaces;
            boolean fokNext = true;
            if (_toKey.length() == 1 && _toKey.charAt(0) == SFConstants.PHONE_PLUS) {
                fokNext = false;
            }

            if (fokNext) {
                if (_toKey.length() > 1 && _toKey.charAt(0) == SFConstants.PHONE_PLUS) {
                    _toKey = _toKey.substring(1);
                }
            }

            if (finished) {
                if (isLocalPhoneNumber(_toKey.length())) {
                    // does not to define phone zone
                    fokNext = false;
                } else {
                    switch (_toKey.length()) {
                        case 10:
                            // have to add default zone prefix
                            _toKey = DEFAULT_ZONE_PREFIX + _toKey;
                            break;
                            case 11:
                                if (_toKey.charAt(0) == '8') {
                                    // it can be SU tradition to put 8 before dial number of other city
                                    PhoneZone _zone = defineZone(_toKey);
                                    if (_zone == null) {
                                        // yes, it is. Replace 8 with 7
                                        _toKey = DEFAULT_ZONE_PREFIX + _toKey.substring(1);
                                    }
                                }
                                break;
                    }
                }
            }

            PhoneZone zone = null;
            if (fokNext) {
                fokNext = false;
                zone = defineZone(_toKey);
                if (zone != null) {
                    fokNext = true;
                }
            }

            if (fokNext) {
                // 1 xxx xxx xx xx
                // 2 xxx xxx xxx
                // 3 xxx xxx xx
                // 4 xxx xx xx
                int amountTriplex = 2;
                if (zone.getPrefixLen() == 2) {
                    amountTriplex = 3;
                } else if (zone.getPrefixLen() == 4) {
                    amountTriplex = 1;
                }

                StringBuilder sb = new StringBuilder(11 + 4);
                sb.append(SFConstants.PHONE_PLUS);
                sb.append(zone.getPrefix());

                int indTriplex = 0;
                int indDuplex = 0;

                for (int i=zone.getPrefixLen(); i<_toKey.length(); i++) {

                    if (amountTriplex > 0) {
                        if (indTriplex == 0) {
                            sb.append(SFConstants.PHONE_GROUP_DELIMITER);
                        }
                        sb.append(_toKey.substring(i, i+1));
                        if (indTriplex == 2) {
                            amountTriplex--;
                            indTriplex = -1;
                        }
                        indTriplex++;
                    } else {
                        if (indDuplex == 0) {
                            sb.append(SFConstants.PHONE_GROUP_DELIMITER);
                        }
                        sb.append(_toKey.substring(i, i+1));
                        if (indDuplex == 1) {
                            indDuplex = -1;
                        }
                        indDuplex++;
                    }

                }
                boolean fullNumber = false;
                if (_toKey.length() == 11) {
                    fullNumber = true;
                }
                phoneBag = new PhoneBag(sb.toString(), zone, fullNumber);

            } else {
                if (finished) {
                    // cycle to format finished phone number
                    StringBuilder fmt = new StringBuilder();
                    int j = -1;
                    for (int i = _toKey.length() - 1; i >= 0; i--) {
                        fmt.append(_toKey.charAt(i));
                        j++;
                        if (j == 1 || j == 3 || j == 6 || j == 9) {
                            fmt.append(SFConstants.PHONE_GROUP_DELIMITER);
                        }
                    }
                    phoneBag = new PhoneBag(fmt.reverse().toString().trim());
                } else {
                    // undefined
                    phoneBag = new PhoneBag(rawNoSpaces);
                }
            }
        }

        return phoneBag;
    }

}
