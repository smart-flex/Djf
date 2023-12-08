package ru.smartflex.djf;

import java.awt.Color;

public interface SFConstants {

    String MODEL_PACKAGE = "ru.smartflex.djf.model.gen";
    String NEW_LINE = "\n";
    String NEW_LINE_CR = "\r\n";
    String MARK_DEL = "TO DELETE: ";
    String MARK_INS = "TO INSERT: ";
    String MARK_UPD = "TO UPDATE: ";

    String PROPERTY_LABEL_RB_PATH = "label_resource_bundle_path";
    String PROPERTY_LABEL_RB_NAME = "label_resource_bundle_name"; // if it is different from sf_label
    String PROPERTY_FORM_CLASS = "form_class_name";
    String PROPERTY_FRAME_CLASS = "frame_class_name";

    String PROPERTY_PATH_TO_FORM = "property_path_to_form";
    String PROPERTY_PATH_TO_BEAN = "property_path_to_bean";
    String PROPERTY_PATH_TO_PANEL = "property_path_to_panel";
    String PROPERTY_PATH_TO_ICON = "property_path_to_icon";

    String PROPERTY_LABEL_CORE_NAME = "sf_framework_label";

    String PROPERTY_MASK_DATE = "mask_date";

    String PROPERTY_FONT_COMMON = "font_common";
    String PROPERTY_FONT_TEXT_INPUT = "font_text_input";
    String PROPERTY_FONT_SIZE = "font_size";

    String DEFAULT_MASK_DATE = "dd-MM-yyyy";
    String DEFAULT_MASK_PERIOD = "0000-00";

    String DEFAULT_FONT_COMMON = "SansSerif";
    //	String DEFAULT_FONT_TEXT_INPUT = "Monospaced";
    String DEFAULT_FONT_TEXT_INPUT = "DialogInput";

    float FONT_TEXT_INPUT_RATE_INCREASING_MAXIMUM = 1.5f;
    String PROPERTY_FONT_TEXT_INPUT_RATE_INCREASING = "font_incr";

    int DEFAULT_FONT_SIZE = 12; //12
    int DEFAULT_FONT_MAX_SIZE = 20;

    String BUTTON_NAME_EXIT = "oper_btn_exit";
    String BUTTON_NAME_SAVE = "oper_btn_save";
    String BUTTON_NAME_DELETE = "oper_btn_delete";
    String BUTTON_NAME_REFRESH = "oper_btn_refresh";
    String BUTTON_NAME_ADD = "oper_btn_add";

    String IMAGE_PACKAGE = "images/";

    Color FIELD_REQUIRED_BACKGROUND_COLOR = new Color(255, 255, 210);
    int DEFAULT_GRID_COLUMN_WIDTH = 50;

    String YES = "yes";
    String NO = "no";
    String TRUE = "true";
    String FALSE = "false";
    String BY_CLICK = "byclick";

    String INVOKE_ENABLED_METHOD_ON_ASSISTANT = "${form.sf.assist.enabled}";

    String MESSAGE_SWING_ERROR_WA = "*** Swing ERROR was handled by workaround: ";
    String COMBOBO_FIRST_ITEM = "";

    int MIN_PERIOD = 0;
    int MAX_PERIOD = 9999;

    int MONTH_NUM_JAN = 1;
    int MONTH_NUM_DEC = 12;

    String TEXT_VIEW_MODE_HTML = "HTMLView";

    String PHONE_ZONE_PROP_FILE_NAME = "idd_phone_zone.properties";
    char PHONE_PLUS = '+';
    String PHONE_GROUP_DELIMITER = " ";

}
