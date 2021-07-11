package ru.smartflex.djf.tool;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;

import javax.swing.JFrame;

import ru.smartflex.djf.Djf;
import ru.smartflex.djf.controller.FormStack;
import ru.smartflex.djf.controller.bean.FormBag;

public class FrameUtil {

    @SuppressWarnings("FieldCanBeLocal")
    private static int DELTA_FIT = 20;

    private FrameUtil() {
    }

    public static void centerFitFrame(Component c) {
        JFrame pfr = (JFrame) Djf.getConfigurator().getFrame();
        int xs = pfr.getX() + DELTA_FIT / 2;
        int ys = pfr.getY() + DELTA_FIT / 2;

        Dimension size = pfr.getContentPane().getSize();
        int h = (int) size.getHeight();
        int w = (int) size.getWidth();
        Dimension sizeCmpn = new Dimension(w - DELTA_FIT / 2, h - DELTA_FIT / 2);
        c.setSize(sizeCmpn);

        c.setLocation(xs, ys);
    }

    public static void centerFitFrameDialog(Component c) {
        FormBag form = FormStack.getCurrentFormBag();
        Dimension sizeModal = form.getSizeModalForm();

        Rectangle rect = Djf.getConfigurator().getFrame()
                .getFormUISCreenBound();

        int xs = (int) (rect.getX());
        int ys = (int) (rect.getY());

        if (sizeModal == null) {
            c.setSize(rect.getSize());
        } else {
            if (sizeModal.getWidth() <= rect.getWidth()
                    && sizeModal.getHeight() <= rect.getHeight()) {
                c.setSize(sizeModal);

                xs = (int) (xs + (rect.getWidth() - sizeModal.getWidth()) / 2);
                ys = (int) (ys + (rect.getHeight() - sizeModal.getHeight()) / 2);
            } else {
                c.setSize(rect.getSize());
            }
        }

        c.setLocation(xs, ys);
    }

    public static void centerOnFrame(Component c) {

        Dimension size = c.getSize();
        int h = (int) size.getHeight();
        int w = (int) size.getWidth();

        JFrame pfr = (JFrame) Djf.getConfigurator().getFrame();

        int xs = pfr.getX() + (pfr.getWidth() - w) / 2;
        int ys = pfr.getY() + (pfr.getHeight() - h) / 2;

        c.setLocation(xs, ys);
    }

}
