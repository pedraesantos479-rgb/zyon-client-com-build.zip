package com.zyon.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;

public final class ZyonScreen extends Screen {
    private static final int PANEL = 0xE9161B29;
    private static final int CARD = 0xE922293A;
    private int left, top;

    public ZyonScreen() { super(Text.literal("Zyon Client")); }

    @Override
    protected void init() {
        left = (width - 330) / 2;
        top = (height - 250) / 2;
        int x = left + 18, y = top + 52, w = 294;
        addDrawableChild(ButtonWidget.builder(toggle("Base Finder", ZyonClient.SETTINGS.baseFinder),
                b -> { ZyonClient.SETTINGS.baseFinder = !ZyonClient.SETTINGS.baseFinder; clearAndInit(); })
                .dimensions(x, y, w, 22).build());
        addDrawableChild(ButtonWidget.builder(toggle("Chunk Finder", ZyonClient.SETTINGS.chunkFinder),
                b -> { ZyonClient.SETTINGS.chunkFinder = !ZyonClient.SETTINGS.chunkFinder; clearAndInit(); })
                .dimensions(x, y + 28, w, 22).build());
        addDrawableChild(ButtonWidget.builder(toggle("Amethyst Scanner", ZyonClient.SETTINGS.amethystScanner),
                b -> { ZyonClient.SETTINGS.amethystScanner = !ZyonClient.SETTINGS.amethystScanner; clearAndInit(); })
                .dimensions(x, y + 56, w, 22).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Finder mode: " + ZyonClient.SETTINGS.finderMode),
                b -> { ZyonClient.SETTINGS.cycleFinderMode(); clearAndInit(); })
                .dimensions(x, y + 84, w, 22).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Amethyst mode: " + ZyonClient.SETTINGS.amethystMode),
                b -> { ZyonClient.SETTINGS.cycleAmethystMode(); clearAndInit(); })
                .dimensions(x, y + 112, w, 22).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Range: " + ZyonClient.SETTINGS.rangeChunks + " chunks"),
                b -> { ZyonClient.SETTINGS.rangeChunks = ZyonClient.SETTINGS.rangeChunks >= 16 ? 2 : ZyonClient.SETTINGS.rangeChunks + 2; clearAndInit(); })
                .dimensions(x, y + 140, w, 22).build());
        addDrawableChild(ButtonWidget.builder(Text.literal("Tracers: " + onOff(ZyonClient.SETTINGS.tracers)),
                b -> { ZyonClient.SETTINGS.tracers = !ZyonClient.SETTINGS.tracers; clearAndInit(); })
                .dimensions(x, y + 168, w, 22).build());
    }

    private static Text toggle(String name, boolean enabled) {
        return Text.literal(name + ": " + onOff(enabled));
    }
    private static String onOff(boolean v) { return v ? "ON" : "OFF"; }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);
        ctx.fill(left, top, left + 330, top + 250, PANEL);
        ctx.fill(left, top, left + 330, top + 34, 0xFF252E43);
        ctx.drawText(textRenderer, "ZYON CLIENT", left + 16, top + 11, 0xFFFFFFFF, false);
        ctx.drawText(textRenderer, "DISCOVERY  /  1.21.11", left + 205, top + 12, 0xFF9AAAC7, false);
        ctx.fill(left + 12, top + 42, left + 318, top + 43, 0x6654D6FF);
        super.render(ctx, mouseX, mouseY, delta);
        ctx.drawText(textRenderer, "Right Shift  •  fechar", left + 16, top + 232, 0xFF9AAAC7, false);
    }

    @Override
    public boolean shouldPause() { return false; }
}
