package ru.smartflex.djf.controller.bean;

public class BeanFormDefProperty {

    private String name = null;
    private Integer length = null;
    private Boolean notNull = null;
    private int precision = 0;
    private int scale = 0;
    private String fillExpression = null;
    private String humanName = null;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;
    }

    public Boolean getNotNull() {
        return notNull;
    }

    public void setNotNull(Boolean notNull) {
        this.notNull = notNull;
    }

    public int getPrecision() {
        return precision;
    }

    public void setPrecision(int precision) {
        this.precision = precision;
    }

    public int getScale() {
        return scale;
    }

    public void setScale(int scale) {
        this.scale = scale;
    }

    public String getFillExpression() {
        return fillExpression;
    }

    public void setFillExpression(String fillExpression) {
        this.fillExpression = fillExpression;
    }

    String getHumanName() {
        return humanName;
    }

    public void setHumanName(String humanName) {
        this.humanName = humanName;
    }

}
