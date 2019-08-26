package cl.sitack.ws.alphabet.soup.dto;

import org.springframework.beans.factory.annotation.Value;

import javax.ws.rs.DefaultValue;

public class ConfiguracionSoupDTO {
    @DefaultValue("15")
    private int w; // ancho de la sopa de letras
    @DefaultValue("15")
    private int h; // largo de la sopa de letras
    @DefaultValue("true")
    private boolean ltr;
    @DefaultValue("false")
    private boolean rtl;
    @DefaultValue("true")
    private boolean ttb;
    @DefaultValue("false")
    private boolean btt;
    @DefaultValue("false")
    private boolean d;

    public int getW() {
        return w;
    }

    public void setW(int w) {
        this.w = w;
    }

    public int getH() {
        return h;
    }

    public void setH(int h) {
        this.h = h;
    }

    public boolean isLtr() {
        return ltr;
    }

    public void setLtr(boolean ltr) {
        this.ltr = ltr;
    }

    public boolean isRtl() {
        return rtl;
    }

    public void setRtl(boolean rtl) {
        this.rtl = rtl;
    }

    public boolean isTtb() {
        return ttb;
    }

    public void setTtb(boolean ttb) {
        this.ttb = ttb;
    }

    public boolean isBtt() {
        return btt;
    }

    public void setBtt(boolean btt) {
        this.btt = btt;
    }

    public boolean isD() {
        return d;
    }

    public void setD(boolean d) {
        this.d = d;
    }
}
