package com.zyon.client;

public final class DiscoverySettings {
    public enum FinderMode { CLASSIC, ANBS_PLUS, ACTIVITY }
    public enum AmethystMode { BYPASS, LIGHT, ANBS_PLUS }

    public boolean baseFinder = true;
    public boolean chunkFinder = true;
    public boolean amethystScanner = true;
    public boolean showCard = true;
    public boolean tracers = false;
    public boolean showOutlines = true;
    public int rangeChunks = 6;
    public int minimumCells = 3;
    public int sensitivity = 55;
    public int outlineColor = 0xFF54D6FF;
    public int accentColor = 0xFF9B7BFF;
    public FinderMode finderMode = FinderMode.CLASSIC;
    public AmethystMode amethystMode = AmethystMode.ANBS_PLUS;

    public boolean anyScannerEnabled() {
        return baseFinder || chunkFinder || amethystScanner;
    }

    public void cycleFinderMode() {
        finderMode = FinderMode.values()[(finderMode.ordinal() + 1) % FinderMode.values().length];
    }

    public void cycleAmethystMode() {
        amethystMode = AmethystMode.values()[(amethystMode.ordinal() + 1) % AmethystMode.values().length];
    }
}
