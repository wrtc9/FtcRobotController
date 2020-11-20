package org.firstinspires.ftc.teamcode.qol;

public class TelemetryInfo { // this is kinda stupid but oh well
    private String caption;
    private String format = "Unset";

    public TelemetryInfo(String caption) {
        this.caption = caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public void setFormat(String format) {
        this.format = format;
    }

    public String getCaption() {
        return caption;
    }

    public String getFormat() {
        return format;
    }
}
