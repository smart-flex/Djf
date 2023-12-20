package ru.smartflex.djf.controller.helper;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Date;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.swing.JCheckBox;
import javax.swing.JComponent;

import ru.smartflex.djf.SFLogger;
import ru.smartflex.djf.SmartFlexException;
import ru.smartflex.djf.WidgetTypeEnum;
import ru.smartflex.djf.controller.bean.UIWrapper;
import ru.smartflex.djf.tool.OtherUtil;
import ru.smartflex.djf.widget.ITextArea;
import ru.smartflex.djf.widget.SFPassword;
import ru.smartflex.djf.widget.SFStepperPercent;
import ru.smartflex.djf.widget.mask.MaskInfo;

public class ConverterUtil {

    private static Lock lockFormatSet = new ReentrantLock(false);
    private static Lock lockFormatGet = new ReentrantLock(false);

    private ConverterUtil() {
    }

    public static Object getFormattedData(WidgetTypeEnum widgetType,
                                          java.text.Format format, String maskDelimiter, Object obj) {
        Object human;

        if (widgetType == WidgetTypeEnum.DATE) {
            human = maskDelimiter;
            if (obj != null) {
                Date date = (Date) obj;
                lockFormatGet.lock();
                try {
                    human = ((DateFormat) format).format(date);
                } finally {
                    lockFormatGet.unlock();
                }
            }

        } else {
            human = obj;
        }

        return human;
    }

    public static Object getFormattedData(MaskInfo maskInfo, Object obj) {

        Object human = maskInfo.getMaskDelimiter();
        if (obj != null) {
            Integer period = (Integer) obj;
            lockFormatGet.lock();
            try {
                int year = period / 100;
                int mm = period % 100;

                human = maskInfo.getPeriodAsString(year, mm);
            } finally {
                lockFormatGet.unlock();
            }
        }

        return human;
    }

    @SuppressWarnings("DuplicateBranchesInSwitch")
    public static Object getValue(WidgetTypeEnum widgetType,
                                  java.text.Format format, JComponent comp, UIWrapper uiw) {
        Object obj = null;

        switch (widgetType) {
            case DATE: {
                String val = ((javax.swing.JTextField) comp).getText();
                obj = getValue(widgetType, format, val, uiw);
            }
            break;
            case PHONE: {
                String val = ((javax.swing.JTextField) comp).getText();
                obj = getValue(widgetType, format, val, uiw);
            }
            break;
            case TEXT: {
                String val = ((javax.swing.JTextField) comp).getText();
                obj = getValue(widgetType, format, val, uiw);
            }
            break;
            case TEXTAREA: {
                String val = ((ITextArea) comp).getText();
                obj = getValue(widgetType, format, val, uiw);
            }
            break;
            case TGRID_TREE_FIELD: {
                String val = ((javax.swing.JTextField) comp).getText();
                obj = getValue(widgetType, format, val, uiw);
            }
            break;
            case PASSWORD:
                char[] pwd = (((SFPassword)comp).getPasswordField()).getPassword();
                if (pwd != null && pwd.length > 0) {
                    obj = new String(pwd);
                }
                break;
            case CHECKBOX:
                boolean sel = ((JCheckBox) comp).isSelected();
                if (sel)
                    obj = Boolean.TRUE;
                else
                    obj = Boolean.FALSE;
                break;
            case PERIOD: {
                String val = ((javax.swing.JTextField) comp).getText();
                obj = getValue(widgetType, format, val, uiw);
            }
            break;
            case BYTE:
            case SHORT:
            case INT:
            case LONG: {
                String val = ((javax.swing.JTextField) comp).getText();
                obj = getValue(widgetType, format, val, uiw);
            }
            break;
            case NUMERIC: {
                String val = ((javax.swing.JTextField) comp).getText();
                obj = getValue(widgetType, format, val, uiw);
            }
            break;
            case STEPPER_PERCENT: {
                SFStepperPercent percent = (SFStepperPercent) comp;
                obj = percent.getPercent();
            }
            break;
        }

        return obj;
    }

    public static Object getValue(WidgetTypeEnum widgetType,
                                  java.text.Format format, String val, UIWrapper uiw) {
        Object obj = null;

        switch (widgetType) {
            case DATE:
                if (val != null && val.trim().length() > 6) {
                    lockFormatSet.lock();
                    try {
                        obj = ((DateFormat) format).parse(val);
                    } catch (Exception e) {
                        SFLogger.error("Date parsing error: " + val, e);
                        throw new SmartFlexException("Date parsing error: " + val,
                                e);
                    } finally {
                        lockFormatSet.unlock();
                    }
                }
                break;
            case TEXT:
            case PHONE:
            case TEXTAREA:
            case TGRID_TREE_FIELD:
                if (!OtherUtil.isStringEmpty(val)) {
                    obj = val;
                }
                break;
            case PERIOD:
                obj = uiw.getMaskInfo().getPeriodFromFormattedText(val);
                break;
            case BYTE:
                //noinspection CatchMayIgnoreException
                try {
                    obj = Byte.valueOf(val);
                } catch (Exception e) {
                }
                break;
            case SHORT:
                //noinspection CatchMayIgnoreException
                try {
                    obj = Short.valueOf(val);
                } catch (Exception e) {
                }
                break;
            case INT:
                //noinspection CatchMayIgnoreException
                try {
                    obj = Integer.valueOf(val);
                } catch (Exception e) {
                }
                break;
            case LONG:
                //noinspection EnumSwitchStatementWhichMissesCases,CatchMayIgnoreException
                try {
                    obj = Long.valueOf(val);
                } catch (Exception e) {
                }
                break;
            case NUMERIC:
                //noinspection CatchMayIgnoreException
                try {
                    obj = convertToBigDecimal(val, uiw);
                } catch (Exception e) {
                }
                break;
        }

        return obj;
    }

    public static BigDecimal convertToBigDecimal(String str, UIWrapper uiw) {
        BigDecimal ret;

        if (str == null) {
            return null;
        }
        if (str.indexOf(',') != -1) {
            str = str.replace(',', '.');
        }
        ret = new BigDecimal(str);
        if (uiw.getBeanFormDefPropertyFromBind() != null) {
            int scale = uiw.getBeanFormDefPropertyFromBind().getScale();
            if (ret.scale() > scale) {
                // cut scale
                ret = ret.setScale(scale, BigDecimal.ROUND_HALF_UP);
            }
        }

        return ret;
    }
}
