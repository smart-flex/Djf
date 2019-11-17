package ru.smartflex.djf;

public enum SizeFrameEnum {

    HALF, ALMOST_WHOLE, CUSTOM;

    private int width = 0;
    private int height = 0;

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    @SuppressWarnings("unused")
    public void setHeight(int height) {
        this.height = height;
    }
}
