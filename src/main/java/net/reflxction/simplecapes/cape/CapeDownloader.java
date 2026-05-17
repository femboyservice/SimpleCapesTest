/*
 * * Copyright 2018 github.com/ReflxctionDev
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.reflxction.simplecapes.cape;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.util.ResourceLocation;
import net.reflxction.simplecapes.SimpleCapes;
import net.reflxction.simplecapes.commons.SimpleSender;
import net.reflxction.simplecapes.utils.ImageUtils;
import net.reflxction.simplecapes.utils.Reference;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

/**
 * Class which downloads capes from the URL
 */
public class CapeDownloader {

    // Singleton instance
    public static final CapeDownloader DOWNLOADER = new CapeDownloader();

    // The cached texture. This is updated when a new URL is set
    private ResourceLocation cachedTexture = getCapeTexture();
    private int currentFrame = 1;
    private int currentFrameTime = 3;
    private int currentFrameAmount = 1;

    private final Minecraft mc = Minecraft.getMinecraft();
    private final HashMap<String, ResourceLocation> cachedLocations = new HashMap<>();

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrameAmount(int currentFrameAmount) {
        this.currentFrameAmount = currentFrameAmount;
    }

    public int getCurrentFrameAmount() {
        return currentFrameAmount;
    }

    public void setCurrentFrameTime(int currentFrameTime) {
        this.currentFrameTime = currentFrameTime;
    }

    public int getCurrentFrameTime() {
        return currentFrameTime;
    }

    /**
     * The cape texture as a resource location
     *
     * @return The cape texture as a resource location
     */
    private ResourceLocation getCapeTexture() {
        ResourceLocation location = null;

        if (mc == null) { return null; }
        if (mc.getTextureManager() == null) { return null; }

        switch (SimpleCapes.getSettings().getCurrentMode()) {
            case URL:
                location = mc.getTextureManager().getDynamicTextureLocation(Reference.MOD_ID, new DynamicTexture(ImageUtils.getImageFromURL(SimpleCapes.getSettings().getCapeURL())));
                break;
            case LOCAL:
                if (SimpleCapes.getSettings().isAnimated()) {
                    CapeAnimationConfig capeAnimationConfig = getCapeConfig();

                    if (capeAnimationConfig != null) {
                        setCurrentFrameAmount((capeAnimationConfig.getFrameAmount() != 0 ? capeAnimationConfig.getFrameAmount() : 1));
                        setCurrentFrameTime(capeAnimationConfig.getFrameTime() != 0 ? capeAnimationConfig.getFrameTime() : 5);

                        String capePath = SimpleCapes.getSettings().getCapePath();
                        String directory = capePath.substring(0, (capePath.lastIndexOf('/') + 1));
                        String fileExtension = capePath.substring(capePath.lastIndexOf('.'));

                        if (cachedLocations != null) {
                            ResourceLocation locationAttempt = cachedLocations.get(directory + "/" + getCurrentFrame() + fileExtension);

                            if (locationAttempt == null) {
                                // generate new image
                                BufferedImage image = ImageUtils.getImageFromFile(directory + "/" + getCurrentFrame() + fileExtension);
                                if (image == null) {
                                    break;
                                }

                                location = mc.getTextureManager().getDynamicTextureLocation(Reference.MOD_ID, new DynamicTexture(image));
                                cachedLocations.put(directory + "/" + getCurrentFrame() + fileExtension, location);

                                // cachedTexture = location;
                                break;

                            } else {
                                // get old one
                                location = locationAttempt;

                                //cachedTexture = locationAttempt;
                                break;
                            }
                        }
                    } else {
                        break;
                    }
                } else {
                    BufferedImage image = ImageUtils.getImageFromFile(SimpleCapes.getSettings().getCapePath());
                    if (image == null) {
                        break;
                    }

                    location = mc.getTextureManager().getDynamicTextureLocation(Reference.MOD_ID, new DynamicTexture(image));
                    break;
                }
            case CLIPBOARD:
                if (!SimpleCapes.getSettings().isClipboardSaved()) {
                    BufferedImage clipboard = ImageUtils.getImageFromClipboard();

                    if (clipboard != null) {
                        location = mc.getTextureManager().getDynamicTextureLocation(Reference.MOD_ID, new DynamicTexture(clipboard));
                    }
                } else {
					BufferedImage cape = ImageUtils.getImageFromFile("clipboard.png");
                    if (cape == null) {
                        break;
                    }

				    location = mc.getTextureManager().getDynamicTextureLocation(Reference.MOD_ID, new DynamicTexture(cape));
				}

                break;
        }

        return location;
    }

    /**
     * The cape config as a config class
     *
     * @return The cape config as a config class "CapeAnimationConfig"
     */
    private CapeAnimationConfig getCapeConfig() {
        if (SimpleCapes.getSettings().getCurrentMode().equals(CapeMode.LOCAL)) {
            String capePath = SimpleCapes.getSettings().getCapePath();

            if (!capePath.isEmpty()) {
                String directory = capePath.substring(0, (capePath.lastIndexOf('/') + 1));
                File capeConfigFile = new File(mc.mcDataDir, (Reference.MOD_ID + File.separator + directory + File.separator + "config.json"));

                if (capeConfigFile.exists()) {
                    try {
                        CapeAnimationConfig capeAnimationConfig = SimpleCapes.getObjectMapper().readValue(capeConfigFile, CapeAnimationConfig.class);

                        if (capeAnimationConfig.getFrameAmount() != 0 && capeAnimationConfig.getFrameTime() != 0) {
                            return capeAnimationConfig;
                        } else {
                            if (capeAnimationConfig.getFrameTime() == 0) {
                                SimpleSender.send("&cInvalid config. (frameTime = 0)");
                            } else {
                                SimpleSender.send("&cInvalid config. (frameAmount = 0)");
                            }

                            return null;
                        }
                    } catch (IOException e) {
                        SimpleSender.send("&cIOException thrown?");
                        return null;
                    }
                } else {
                    SimpleSender.send("&cNo 'config.json' file found in directory");
                    return null;
                }
            } else {
                SimpleSender.send("&cNo cape found?");
                return null;
            }
        } else {
            SimpleSender.send("&cCape Configs can only be used when mode is set to LOCAL.");
            return null;
        }
    }

    /**
     * The cached resource location that is updated every time a new URL is set
     *
     * @return The cached texture
     */
    ResourceLocation getCachedTexture() {
        return cachedTexture;
    }

    /**
     * Updates the cached texture.
     */
    public void updateCachedTexture() {
        cachedTexture = getCapeTexture();
    }
}
