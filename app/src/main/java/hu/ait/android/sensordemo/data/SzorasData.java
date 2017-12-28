package hu.ait.android.sensordemo.data;

/**
 * Created by laufertamasjonas on 2017. 11. 15..
 */

public class SzorasData {
    String Tim2;
    String Ital;
    String Etel;
    String Suly;

    private double szoras1x;
    private double szoras1y;
    private double szoras1z;
    private double szoras2x;
    private double szoras2y;
    private double szoras2z;

    public SzorasData(String tim2, String suly, String etel, String ital, double szoras1x, double szoras1y, double szoras1z, double szoras2x, double szoras2y, double szoras2z) {
        Tim2 = tim2;
        Suly = suly;
        Etel = etel;
        Ital = ital;
        this.szoras1x = szoras1x;
        this.szoras1y = szoras1y;
        this.szoras1z = szoras1z;
        this.szoras2x = szoras2x;
        this.szoras2y = szoras2y;
        this.szoras2z = szoras2z;
    }

    public String getTim2() {
        return Tim2;
    }

    public void setTim2 (String tim2) {
        Tim2 = Tim2;
    }

    public String getSuly() {
        return Suly;
    }

    public void setSuly(String suly) {
        Suly = Suly;
    }

    public String getEtel() {
        return Etel;
    }

    public void setEtel(String etel) {
        Etel = Etel;
    }

    public String getItal() {
        return Ital;
    }

    public void setItal(String ital) {
        Ital = Ital;
    }

    public double getSzoras1x() {
        return szoras1x;
    }

    public void setSzoras1x(double szoras1x) {
        this.szoras1x = szoras1x;
    }

    public double getSzoras1y() {
        return szoras1y;
    }

    public void setSzoras1y(double szoras1y) {
        this.szoras1y = szoras1y;
    }

    public double getSzoras1z() {
        return szoras1z;
    }

    public void setSzoras1z(double szoras1z) {
        this.szoras1z = szoras1z;
    }

    public double getSzoras2x() {
        return szoras2x;
    }

    public void setSzoras2x(double szoras2x) {
        this.szoras2x = szoras2x;
    }

    public double getSzoras2y() {
        return szoras2y;
    }

    public void setSzoras2y(double szoras2y) {
        this.szoras2y = szoras2y;
    }

    public double getSzoras2z() {
        return szoras2z;
    }

    public void setSzoras2z(double szoras2z) {
        this.szoras2z = szoras2z;
    }
}
