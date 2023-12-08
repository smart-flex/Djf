package ru.smartflex.djf;

import java.awt.Color;
import java.awt.Font;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;

import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.border.LineBorder;

import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.bean.ButtonOperCache;
import ru.smartflex.djf.controller.bean.FormBag;
import ru.smartflex.djf.controller.helper.ObjectCreator;
import ru.smartflex.djf.controller.helper.PrefixUtil;
import ru.smartflex.djf.tool.FontUtil;
import ru.smartflex.djf.widget.IFrame;
import ru.smartflex.djf.widget.mask.ISFMaskConstants;

public class DjfConfigurator {

    public static Color GRID_COLUMN_HEAD_FOREGROUND = new Color(0, 0, 0);
    public static Color GRID_COLUMN_HEAD_NOEDIT_BACKGROUND = new Color(160,
            160, 160); // 192
    public static Color GRID_COLUMN_HEAD_BACKGROUND = new Color(210, 210, 210);

    @SuppressWarnings("unused")
    public static int MODE_GRID_RESIZE_COLUMN = JTable.AUTO_RESIZE_LAST_COLUMN;

    private static final String LABEL_FILE_NAME = "sf_label";
    private static final String PATH_DELIMITER = "/";

    private static String formClassName = null;
    private static String frameClassName = null;

    private static IFrame frame = null;

    private static String maskDate = null;
    private static String maskDateOnlyDelimiter = null;

    private static String maskPeriod = null;
    private static String maskPeriodOnlyDelimiter = null;

    private static String fontCommon = null;
    private static String fontTextInput = null;
    private static int fontSize = 0;
    private static float fontTextInputRateIncreasing = 0f;

    private static AtomicInteger uiCounter = new AtomicInteger(1);
    private static AtomicInteger uiOrder = new AtomicInteger(-1);

    private static IAccessible accessibleHandler = null;
    private static IClosingClient closingClient = null;

    private static Map<WidgetTypeEnum, AlignTypeEnum> mapAlign = new HashMap<WidgetTypeEnum, AlignTypeEnum>();

    private static String pathToForm = null;
    private static String pathToPanel = null;
    private static String pathToBean = null;
    private static String pathToIcon = null;

    private static DjfConfigurator configurator = new DjfConfigurator();

    private ButtonOperCache frameButCache = new ButtonOperCache();
    private ButtonOperCache dialogButCache = new ButtonOperCache(true);

    private DjfConfigurator() {
    }

    static DjfConfigurator getInstance() {
        return configurator;
    }

    private static void init() {
        mapAlign.put(WidgetTypeEnum.DATE, AlignTypeEnum.CENTER);
        mapAlign.put(WidgetTypeEnum.LABEL, AlignTypeEnum.LEFT);
        mapAlign.put(WidgetTypeEnum.TEXT, AlignTypeEnum.LEFT);
        mapAlign.put(WidgetTypeEnum.PERIOD, AlignTypeEnum.CENTER);
        mapAlign.put(WidgetTypeEnum.BYTE, AlignTypeEnum.RIGHT);
        mapAlign.put(WidgetTypeEnum.SHORT, AlignTypeEnum.RIGHT);
        mapAlign.put(WidgetTypeEnum.INT, AlignTypeEnum.RIGHT);
        mapAlign.put(WidgetTypeEnum.LONG, AlignTypeEnum.RIGHT);
        mapAlign.put(WidgetTypeEnum.NUMERIC, AlignTypeEnum.RIGHT);
        mapAlign.put(WidgetTypeEnum.PHONE, AlignTypeEnum.LEFT);
    }

    public AlignTypeEnum getDefaultAlign(WidgetTypeEnum widget) {
        return mapAlign.get(widget);
    }

    @SuppressWarnings("unused")
    public void setDefaultAlign(WidgetTypeEnum widget,
                                AlignTypeEnum align) {
        mapAlign.put(widget, align);
    }

    @SuppressWarnings("unused")
    public void configure() {
        configure(null);
    }

    public void configure(Properties props) {
        init();

        helloWorld();

        String msgResourceBundlePath = null;
        String msgResourceBundleName = null;
        if (props != null) {
            msgResourceBundlePath = props
                    .getProperty(SFConstants.PROPERTY_LABEL_RB_PATH);
            msgResourceBundleName = props
                    .getProperty(SFConstants.PROPERTY_LABEL_RB_NAME);

            formClassName = props.getProperty(SFConstants.PROPERTY_FORM_CLASS);
            frameClassName = props
                    .getProperty(SFConstants.PROPERTY_FRAME_CLASS);

            // masks
            maskDate = props.getProperty(SFConstants.PROPERTY_MASK_DATE);
            if (maskDate != null) {
                // validate
                try {
                    new SimpleDateFormat(maskDate);
                } catch (Exception e) {
                    SFLogger.warn(DjfConfigurator.class,
                            "Date mask format is wrong: " + maskDate);
                    maskDate = null;
                }
            }

            // fonts
            fontCommon = props.getProperty(SFConstants.PROPERTY_FONT_COMMON);
            if (fontCommon != null) {
                fontCommon = FontUtil.checkFontName(fontCommon);
            }
            fontTextInput = props
                    .getProperty(SFConstants.PROPERTY_FONT_TEXT_INPUT);
            if (fontTextInput != null) {
                fontTextInput = FontUtil.checkMonoFontName(fontTextInput);
            }
            try {
                String fz = props.getProperty(SFConstants.PROPERTY_FONT_SIZE);
                fontSize = Integer.parseInt(fz);
            } catch (Exception e) {
                fontSize = 0;
            }
            try {
                String fi = props.getProperty(SFConstants.PROPERTY_FONT_TEXT_INPUT_RATE_INCREASING);
                fontTextInputRateIncreasing = Float.parseFloat(fi);
            } catch (Exception e) {
                fontTextInputRateIncreasing = 0f;
            }

            pathToForm = props.getProperty(SFConstants.PROPERTY_PATH_TO_FORM);
            pathToForm = addDelimiter(pathToForm);
            pathToPanel = props.getProperty(SFConstants.PROPERTY_PATH_TO_PANEL);
            pathToPanel = addDelimiter(pathToPanel);
            pathToBean = props.getProperty(SFConstants.PROPERTY_PATH_TO_BEAN);
            pathToBean = addDelimiter(pathToBean);
            pathToIcon = props.getProperty(SFConstants.PROPERTY_PATH_TO_ICON);
            pathToIcon = addDelimiter(pathToIcon);
        }

        //default settings
        if (formClassName == null) {
            formClassName = "ru.smartflex.djf.widget.template.FormUI";
        }
        if (frameClassName == null) {
            frameClassName = "ru.smartflex.djf.widget.template.FrameUI";
        }
        if (maskDate == null) {
            maskDate = SFConstants.DEFAULT_MASK_DATE;
        }
        maskDateOnlyDelimiter = extractDelimitersFromDateMask(maskDate);

        // mask period
        maskPeriod = SFConstants.DEFAULT_MASK_PERIOD;
        maskPeriodOnlyDelimiter = extractDelimitersFromPeriodMask(maskPeriod);
        if (fontCommon == null) {
            fontCommon = SFConstants.DEFAULT_FONT_COMMON;
        }
        //noinspection ResultOfMethodCallIgnored
        FontUtil.checkFontName(fontCommon);
        if (fontTextInput == null) {
            fontTextInput = SFConstants.DEFAULT_FONT_TEXT_INPUT;
        }
        if (fontSize <= 0 || fontSize > SFConstants.DEFAULT_FONT_MAX_SIZE) {
            fontSize = SFConstants.DEFAULT_FONT_SIZE;
        }
        if (fontTextInputRateIncreasing < 0 || fontTextInputRateIncreasing > SFConstants.FONT_TEXT_INPUT_RATE_INCREASING_MAXIMUM) {
            fontTextInputRateIncreasing = 0;
        }

        loadMsgBundle(msgResourceBundlePath, msgResourceBundleName);

        initUIManager();

        // create frame
        frame = ObjectCreator.createFrame();
    }

    private void helloWorld() {
        SFLogger.info("*******************************************");
        SFLogger.info("***     Djf is a desktop java forms,    ***");
        SFLogger.info("***        the compact and litle        ***");
        SFLogger.info("***      master-detail UI library       ***");
        SFLogger.info("*** like as FoxBase, but based on Swing ***");
        SFLogger.info("*** @author gali.shaimardanov@gmail.com ***");
        SFLogger.info("***             ver: " + Djf.DJF_VERSION + "                ***");
        SFLogger.info("*******************************************");
    }

    public String getPathToForm() {
        return pathToForm;
    }

    public String getPathToPanel() {
        return pathToPanel;
    }

    public String getPathToBean() {
        return pathToBean;
    }

    public String getPathToIcon() {
        return pathToIcon;
    }

    private static String extractDelimitersFromDateMask(String mask) {
        String onlyDelim = mask.replace('d', ISFMaskConstants.CHAR_SPACE);
        onlyDelim = onlyDelim.replace('M', ISFMaskConstants.CHAR_SPACE);
        onlyDelim = onlyDelim.replace('y', ISFMaskConstants.CHAR_SPACE);
        return onlyDelim;
    }

    private static String extractDelimitersFromPeriodMask(String mask) {
        return mask.replace('0', ISFMaskConstants.CHAR_SPACE);
    }

    public String getMaskDate(String maskCustom) {
        if (maskCustom != null) {
            return maskCustom;
        }
        return maskDate;
    }

    public String getMaskDateOnlyDelimiter(String maskCustom) {
        if (maskCustom != null) {
            return extractDelimitersFromDateMask(maskCustom);
        }
        return maskDateOnlyDelimiter;
    }

    public String getMaskPeriod() {
        return maskPeriod;
    }

    public String getMaskPeriodOnlyDelimiter() {
        return maskPeriodOnlyDelimiter;
    }

    public IFrame getFrame() {
        return frame;
    }

    public String getFormClassName() {
        return formClassName;
    }

    public String getFrameClassName() {
        return frameClassName;
    }

    private static String addDelimiter(String path) {
        if (path != null && !path.endsWith(PATH_DELIMITER)) {
            path += PATH_DELIMITER;
        }
        return path;
    }

    private static void loadMsgBundle(String path, String name) {
        boolean noErrorShow = false;
        if (path == null && name == null) {
            noErrorShow = true;
        }
        String bundle;
        if (path != null && name != null) {
            path = addDelimiter(path);
            bundle = path + name;
        } else {
            if (path == null) {
                // search in root
                bundle = LABEL_FILE_NAME;
            } else {
                path = addDelimiter(path);
                bundle = path + LABEL_FILE_NAME;
            }
        }
        Locale locale = Locale.getDefault();
        ResourceBundle labelCoreBundle;
        ResourceBundle labelBundle;

        try {
            labelCoreBundle = ResourceBundle
                    .getBundle(SFConstants.PROPERTY_LABEL_CORE_NAME);
            PrefixUtil.setLabelCoreBundle(labelCoreBundle);
        } catch (java.util.MissingResourceException e) {
            SFLogger.error("Error", e);
        }

        try {
            labelBundle = ResourceBundle.getBundle(bundle, locale);
            PrefixUtil.setLabelBundle(labelBundle);
            SFLogger.debug("Bundle is: ", labelBundle);
        } catch (java.util.MissingResourceException e) {
            if (!noErrorShow) {
                SFLogger.error("Error", e);
            }
        }

    }

    public static Font getFontTextInput() {
        return new Font(fontTextInput, java.awt.Font.PLAIN, FontUtil.getIncreasedFontSize());
    }

    private static void initUIManager() {

        UIManager.put("Table.focusCellHighlightBorder", new LineBorder(
                Color.white, 1));
        UIManager.put("TextField.inactiveForeground", Color.black);

        UIManager.put("TextArea.font", getFontTextInput());
        UIManager.put("Table.font", getFontTextInput());

        UIManager.put("TextField.font", getFontTextInput());
        UIManager.put("PasswordField.font", getFontTextInput());
        UIManager.put("Label.font", new Font(fontCommon, Font.PLAIN, FontUtil.getIncreasedFontSize()));
        UIManager.put("Button.font", new Font(fontCommon, Font.PLAIN, FontUtil.getIncreasedFontSize()));

        // Affects press Enter on active button.
        UIManager.put("Button.defaultButtonFollowsFocus", Boolean.TRUE);

        UIManager.put("ToggleButton.font", new Font(fontCommon, Font.PLAIN, FontUtil.getIncreasedFontSize()));
        UIManager.put("TabbedPane.font", new Font(fontCommon, Font.PLAIN, FontUtil.getIncreasedFontSize()));
        UIManager.put("CheckBox.font", new Font(fontCommon, Font.PLAIN, FontUtil.getIncreasedFontSize()));
        UIManager.put("RadioButtonMenuItem.font", new Font(fontCommon, Font.PLAIN, FontUtil.getIncreasedFontSize()));
        UIManager.put("ComboBox.font", new Font(fontCommon, Font.PLAIN, FontUtil.getIncreasedFontSize()));
        UIManager.put("RadioButton.font", new Font(fontCommon, Font.PLAIN, FontUtil.getIncreasedFontSize()));

        UIManager.put("Label.foreground", new Color(128, 0, 64));
        UIManager.put("CheckBox.foreground", new Color(128, 0, 64));
        UIManager.put("TextArea.inactiveForeground", new Color(104, 79, 81));
        UIManager.put("RadioButton.foreground", new Color(128, 0, 64));
    }

    @SuppressWarnings("UnnecessaryCallToStringValueOf")
    public String getNextUIId(WidgetTypeEnum type) {
        return "_" + type.name() + String.valueOf(uiCounter.incrementAndGet());
    }

    public int getNextUIOrder() {
        return uiOrder.decrementAndGet();
    }

    public void setAccessibleHandler(IAccessible accessHandler) {
        accessibleHandler = accessHandler;
    }

    public IAccessible getAccessible() {
        return accessibleHandler;
    }

    public boolean isAccessible(String[] infos) {
        if (accessibleHandler != null) {
            return accessibleHandler.isAccessible(infos);
        }
        return false;
    }

    @SuppressWarnings("unused")
    public void setClosingClient(IClosingClient closingClient) {
        DjfConfigurator.closingClient = closingClient;
    }

    public void closeClientResources() {
        if (closingClient != null) {
            try {
                closingClient.closeClientResources();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public ButtonOperCache getFrameButCache() {
        return frameButCache;
    }

    public ButtonOperCache getDialogButCache() {
        return dialogButCache;
    }

    public ButtonOperCache getButtonCache() {
        FormBag fb = FormStack.getCurrentFormBag();
        return fb.getButtonOperCache();
    }

    public static int getFontSize() {
        return fontSize;
    }

    public static String getFontTextInputName() {
        return fontTextInput;
    }

    public static float getFontTextInputRateIncreasing() {
        return fontTextInputRateIncreasing;
    }
}
