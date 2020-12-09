package org.firstinspires.ftc.teamcode.qol;

/**
 * TelemetryInfo provides a standardized format for telemetry storage. TelemetryInfo is mutable.
 *
 * @author Will (wrtc9)
 */
public class TelemetryInfo { // this is kinda stupid but oh well
    private String caption;
    private String content = "Unset";

    public TelemetryInfo(String caption) {
        this.caption = caption;
    }

    public TelemetryInfo(String caption, String content) {
        this.caption = caption;
        this.content = content;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    } // this could probably be removed

    public void setContent(String content) {
        this.content = content;
    }

    public String getCaption() {
        return caption;
    }

    public String getContent() {
        return content;
    }
}
