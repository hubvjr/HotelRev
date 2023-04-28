package com.hotelrev;


/**
 * The type card helper.
 */
public class TextCardHelper {


    private String id;
    private String line1;
    private String line2;

    public TextCardHelper(String id, String line1, String line2) {
        this.id = id;
        this.line1 = line1;
        this.line2 = line2;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }
}
