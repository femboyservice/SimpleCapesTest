package net.reflxction.simplecapes.listeners;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.reflxction.simplecapes.SimpleCapes;
import net.reflxction.simplecapes.cape.CapeDownloader;

public class tickHandler {
    private int tickCounter = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            return;
        }

        tickCounter += 1;

        if (tickCounter >= CapeDownloader.DOWNLOADER.getCurrentFrameTime()) {
            tickCounter = 0;
            doHalfTick();
        }
    }

    private void doHalfTick() {
        if (!SimpleCapes.getSettings().isAnimated()) { return; }
        CapeDownloader.DOWNLOADER.setCurrentFrame(CapeDownloader.DOWNLOADER.getCurrentFrame() + 1);

        if (CapeDownloader.DOWNLOADER.getCurrentFrame() > CapeDownloader.DOWNLOADER.getCurrentFrameAmount()) {
            CapeDownloader.DOWNLOADER.setCurrentFrame(1);
        }

        CapeDownloader.DOWNLOADER.updateCachedTexture();
    }
}
