package net.reflxction.simplecapes.cape;

public class CapeConfig {
    private String path;
    private boolean animated;

    // askip j'en ai besoin
    public CapeConfig() {}

    public CapeConfig(String path, boolean animated) {
        this.path = path;
        this.animated = animated;
    }

    public String getPath() {
        return path;
    }
    public boolean isAnimated() {
        return animated;
    }
}
