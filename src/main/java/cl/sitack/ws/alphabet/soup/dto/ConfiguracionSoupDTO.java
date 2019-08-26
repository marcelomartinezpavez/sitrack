package cl.sitack.ws.alphabet.soup.dto;

public class ConfiguracionSoupDTO {
    private int w; // ancho de la sopa de letras
    private int h=15; // largo de la sopa de letras
    private boolean ltr;
    //@Value("false")
    private boolean rtl;
    //@Value("true")
    private boolean ttb;
    //@DefaultValue("false")
    private boolean btt;
    //@DefaultValue("false")
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
