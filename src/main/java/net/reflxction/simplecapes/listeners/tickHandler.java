package net.reflxction.simplecapes.listeners;

import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.reflxction.simplecapes.SimpleCapes;
import net.reflxction.simplecapes.cape.CapeDownloader;

public class tickHandler {
    private int tickCounter = 0;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END ||
                !SimpleCapes.getSettings().isAnimated() ||
                !SimpleCapes.getSettings().isEnabled() ||
                !SimpleCapes.getSettings().isCapeSet())
        {
            return;
        }

        tickCounter += 1;

        if (tickCounter >= CapeDownloader.DOWNLOADER.getCurrentFrameTime()) {
            tickCounter = 0;
            changeTexture();
        }
    }

    private void changeTexture() {
        CapeDownloader.DOWNLOADER.setCurrentFrame(CapeDownloader.DOWNLOADER.getCurrentFrame() + 1);

        if (CapeDownloader.DOWNLOADER.getCurrentFrame() > CapeDownloader.DOWNLOADER.getCurrentFrameAmount()) {
            CapeDownloader.DOWNLOADER.setCurrentFrame(1);
        }

        CapeDownloader.DOWNLOADER.updateCachedTexture();
    }
}
