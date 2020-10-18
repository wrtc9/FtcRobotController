package org.firstinspires.ftc.teamcode;

public class Target { // depricated
    private String name;
    private float[] xyz;
    private float[] rot;

    public Target(String targetName, float[] targetXyz, float[]targetRot){
        name = targetName;
        xyz = targetXyz;
        rot = targetRot;
    }
    public String getName(){ return name; }
    public float[] getXyz(){ return xyz; }
    public float[] getRot(){ return rot; }
}
