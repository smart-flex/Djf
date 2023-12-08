package ru.smartflex.djf.widget.grid;

import java.util.StringTokenizer;

import ru.smartflex.djf.SFConstants;
import ru.smartflex.djf.tool.FontUtil;

public class ColumnWidth {

    private int minWidth = 0;
    private int prefWidth = 0;
    private int amount = 0;

    ColumnWidth(String w) {
        if (w != null) {
            int[] widths = new int[3];
            StringTokenizer st = new StringTokenizer(w, ":");
            while (st.hasMoreTokens()) {
                if (amount >= widths.length) {
                    break;
                }
                //noinspection CatchMayIgnoreException
                try {
                    int wd = Integer.parseInt(st.nextToken());
                    widths[amount++] = wd;
                } catch (Exception e) {
                }
            }
            switch (amount) {
                case 1:
                    minWidth = widths[0];
                    break;
                case 2:
                    minWidth = widths[0];
                    prefWidth = widths[1];
                    break;
            }
        }
    }

    public int getMinWidth() {
        if (amount <= 0) {
            return getIncreasedWidth(SFConstants.DEFAULT_GRID_COLUMN_WIDTH);
        } else {
            return getIncreasedWidth(minWidth);
        }
    }

    public int getPrefWidth() {
        return getIncreasedWidth(prefWidth);
    }

    private int getIncreasedWidth(int wh) {
        float rate = FontUtil.getRateWidthOfFontIncreasing();
        if (rate != 0f) {
            // нужно увеличение шрифта
            return wh + Math.round(rate * (float)wh);
        }
        return wh;
    }

}
