package ru.smartflex.djf.controller.helper;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.beanutils.MethodUtils;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.IAccessible;
import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.WidgetManager;
import ru.smartflex.djf.controller.bean.LabelBundle;
import ru.smartflex.djf.controller.bean.SFPair;
import ru.smartflex.djf.controller.bean.tree.TreeListUtils;
import ru.smartflex.djf.controller.exception.ObjectCreationException;
import ru.smartflex.djf.tool.OtherUtil;

public class PrefixUtil {
    private static final String COMMON_PREFIX = "${";
    private static final String LABEL_PREFIX_UI = "${label.";
    private static final String LABEL_PANEL_PREFIX_UI = "${#.";
    private static final String COMMON_SUFFIX = "}";
    private static final String FORM_PREFIX_UI = "${form.";
    private static final String PREDEFINED_SF_WRAPPER = "selected";

    private static final String SESSION_PREFIX_UI = "${session.";
    private static final String SESS_PREFIX_UI = "${sess.";
    private static final String S_PREFIX_UI = "${s.";

    private static final String PREFIX_DELIMITER = ".";

    private static final String REG_ACCESS = "(\\w*)";
    private static Pattern PATTERN_ACCESS = Pattern.compile(REG_ACCESS,
            Pattern.DOTALL);

    private static ResourceBundle labelBundle = null;
    private static ResourceBundle labelCoreBundle = null;

    private PrefixUtil() {
        super();
    }

    public static String getFormName(String val) {
        String ret = null;

        if (val != null) {
            if (val.startsWith(FORM_PREFIX_UI) && val.endsWith(COMMON_SUFFIX)
                    && val.length() > 8) {
                String key = extractFromPrefix(val, FORM_PREFIX_UI);
                if (key.startsWith(PREDEFINED_SF_WRAPPER)) {

                    Object obj = Djf.getCurrentObject();
                    if (key.length() > PREDEFINED_SF_WRAPPER.length()) {
                        String property = key.substring(PREDEFINED_SF_WRAPPER
                                .length() + 1);
                        ret = (String) TreeListUtils.getPropertyValue(obj,
                                property);
                    } else {
                        ret = (String) obj;
                    }
                }
            } else {
                ret = val;
            }
        }

        return ret;
    }

    public static boolean isValueDynamic(String val) {
        boolean fok = false;

        if (val != null) {
            if (val.startsWith(COMMON_PREFIX)) {
                fok = true;
            }
        }

        return fok;
    }

    public static String getMsg(String msg, LabelBundle locBundle) {
        String msgRet = msg;

        if (msg != null) {
            String m = null;
            if (msg.startsWith(LABEL_PREFIX_UI) && msg.endsWith(COMMON_SUFFIX)) {
                String key = extractFromPrefix(msg, LABEL_PREFIX_UI);
                //noinspection CatchMayIgnoreException
                try {
                    if (labelBundle != null) {
                        m = labelBundle.getString(key);
                    }
                } catch (Exception e) {
                }
                //noinspection CatchMayIgnoreException
                try {
                    if (m == null && labelCoreBundle != null) {
                        m = labelCoreBundle.getString(key);
                    }
                } catch (Exception e) {
                }
            } else if (msg.startsWith(LABEL_PANEL_PREFIX_UI)
                    && msg.endsWith(COMMON_SUFFIX)) {
                String key = extractFromPrefix(msg, LABEL_PANEL_PREFIX_UI);
                if (locBundle != null) {
                    m = locBundle.getMsg(key);
                    if (Djf.getConfigurator().getAccessible() != null) {
                        IAccessible access = Djf.getConfigurator()
                                .getAccessible();
                        Enumeration<String> markers = access.elements();
                        while (markers.hasMoreElements()) {
                            String suffix = markers.nextElement();
                            String mm = locBundle.getMsg(key + PREFIX_DELIMITER
                                    + suffix);
                            if (mm != null) {
                                // found message
                                m = mm;
                                break;
                            }
                        }
                    }
                }
            } else if (msg.startsWith(COMMON_PREFIX)
                    && msg.endsWith(COMMON_SUFFIX)) {
                String key = extractFromPrefix(msg, COMMON_PREFIX);

                //noinspection CatchMayIgnoreException
                try {
                    if (labelBundle != null) {
                        m = labelBundle.getString(key);
                    }
                } catch (Exception e) {
                }
                //noinspection CatchMayIgnoreException
                try {
                    if (m == null && labelCoreBundle != null) {
                        m = labelCoreBundle.getString(key);
                    }
                } catch (Exception e) {
                }
            }

            if (m != null) {
                msgRet = m;
            }
        }

        return msgRet;
    }

    private static String extractFromPrefix(String msg, String prefix) {
        String key = null;
        if (msg.length() > (prefix.length() + COMMON_SUFFIX.length())) {
            key = msg
                    .substring(prefix.length(), msg.length() - COMMON_SUFFIX.length());
        }
        return key;
    }

    private static String extractFromPrefixs(String msg,
                                             String... prefixs) {
        String key = null;
        for (String prefix : prefixs) {
            if (msg.startsWith(prefix)) {
                if (msg.length() > (prefix.length() + COMMON_SUFFIX.length())) {
                    key = msg.substring(prefix.length(),
                            msg.length() - COMMON_SUFFIX.length());
                }
                break;
            }
        }
        return key;
    }

    public static SFPair<String, Object> getMethodDefinition(String str) {
        SFPair<String, Object> pair = null;
        if (str != null) {
            if ((str.startsWith(SESSION_PREFIX_UI)
                    || str.startsWith(SESS_PREFIX_UI) || str
                    .startsWith(S_PREFIX_UI)) && str.endsWith(COMMON_SUFFIX)) {
                String key = extractFromPrefixs(str,
                        S_PREFIX_UI, SESS_PREFIX_UI, SESSION_PREFIX_UI);
                String[] part = TreeListUtils.getPartProperties(key);
                if (part.length == 2) {
                    String id = part[0];
                    Object obj = Djf.getSession().getBean(id);
                    String methodName = part[1];
                    if (id != null && obj != null && methodName != null) {
                        pair = new SFPair<String, Object>(methodName, obj);
                    }
                }
            }
        }
        return pair;
    }

    static SFPair<String, String> extractEnumClassAndName(String str) {
        SFPair<String, String> pair = null;
        if (str != null) {
            int ind = str.lastIndexOf(".");
            if (ind >= 0) {
                String clazz = str.substring(0, ind);
                String name = str.substring(ind + 1);
                pair = new SFPair<String, String>(clazz, name);
            }
        }
        return pair;
    }

    public static Object getParameterValue(String value) {
        Object obj = null;

        if (value != null) {
            if (value.startsWith(COMMON_PREFIX)) {
                if ((value.startsWith(SESSION_PREFIX_UI)
                        || value.startsWith(SESS_PREFIX_UI) || value
                        .startsWith(S_PREFIX_UI))
                        && value.endsWith(COMMON_SUFFIX)) {
                    String key = extractFromPrefixs(value,
                            S_PREFIX_UI, SESS_PREFIX_UI, SESSION_PREFIX_UI);
                    String[] part = TreeListUtils.getPartProperties(key);
                    if (part.length == 1) {
                        obj = Djf.getSession().getBean(key);
                    } else if (part.length == 2) {
                        Object app = Djf.getSession().getBean(part[0]);
                        try {
                            obj = MethodUtils.invokeMethod(app, part[1], null);
                        } catch (Exception e) {
                            String err = "There is error for: " + value;
                            SFLogger.error(err, e);
                            throw new ObjectCreationException(err, e);
                        }
                    }
                } else if (value.startsWith(FORM_PREFIX_UI)
                        && value.endsWith(COMMON_SUFFIX)) {
                    String parameterName = extractFromPrefix(value,
                            FORM_PREFIX_UI);
                    String[] part = TreeListUtils
                            .getPartProperties(parameterName);
                    if (part.length == 1) {
                        obj = FormStack.getCurrentFormBag().getFormParameter(
                                parameterName);
                    } else if (part.length == 2) {
                        Object app = FormStack.getCurrentFormBag()
                                .getFormParameter(part[0]);
                        try {
                            obj = MethodUtils.invokeMethod(app, part[1], null);
                        } catch (Exception e) {
                            String err = "There is error for: " + value;
                            SFLogger.error(err, e);
                            throw new ObjectCreationException(err, e);
                        }
                    }

                    //
                }
            } else {
                obj = value;
            }
        }

        return obj;
    }

    public static Object getFormParameter(String value, WidgetManager wm) {
        Object obj = null;

        if (value != null) {
            if (value.startsWith(COMMON_PREFIX)) {
                if (value.startsWith(FORM_PREFIX_UI)
                        && value.endsWith(COMMON_SUFFIX)) {
                    String parameterName = extractFromPrefix(value,
                            FORM_PREFIX_UI);
                    obj = wm.getFormParameter(parameterName);
                }
            } else {
                obj = value;
            }
        }

        return obj;
    }

    static String[] getFormParameterAccessible(String value,
                                               WidgetManager wm) {
        if (value != null) {
            String src = (String) getFormParameter(value, wm);
            if (src == null) {
                return null;
            }

            List<String> list = new ArrayList<String>();
            Matcher matcher = PATTERN_ACCESS.matcher(src);
            while (matcher.find()) {
                String fnd = matcher.group(1);
                if (OtherUtil.isStringEmpty(fnd)) {
                    continue;
                }
                list.add(fnd);
            }
            if (list.size() == 0) {
                list.add(src);
            }
            String[] ret = new String[list.size()];
            list.toArray(ret);
            return ret;
        }
        return null;
    }

    public static void setLabelBundle(ResourceBundle lb) {
        labelBundle = lb;
    }

    public static void setLabelCoreBundle(ResourceBundle lb) {
        labelCoreBundle = lb;
    }

    public static String extractNameBySlash(String src) {
        int ind = src.lastIndexOf("/");
        if (ind != -1) {
            return src.substring((ind + 1));
        }
        return src;
    }
}
