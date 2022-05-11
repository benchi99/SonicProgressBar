package data;

public enum BarCharacter {
    SONIC(new IconData("/sonic_dash.gif", 16, 12), new IconData("/sonic_skid.gif", 9, 11), new IconData("/sonic_skid_mirror.gif", 9, 11)),
    TAILS(new IconData("/tails_dash.gif", 16, 12), new IconData("/tails_skid.gif", 9, 11), new IconData("/tails_skid_mirror.gif", 9, 11));

    private final IconData dash;
    private final IconData skid;
    private final IconData skidMirror;

    BarCharacter(IconData dash, IconData skid, IconData skidMirror) {
        this.dash = dash;
        this.skid = skid;
        this.skidMirror = skidMirror;
    }

    public IconData getDash() {
        return dash;
    }

    public IconData getSkid() {
        return skid;
    }

    public IconData getSkidMirror() {
        return skidMirror;
    }
}
