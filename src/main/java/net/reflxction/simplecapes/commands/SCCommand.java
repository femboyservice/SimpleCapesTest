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
package net.reflxction.simplecapes.commands;

import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.reflxction.simplecapes.SimpleCapes;
import net.reflxction.simplecapes.cape.CapeDownloader;
import net.reflxction.simplecapes.cape.CapeMode;
import net.reflxction.simplecapes.commons.SimpleSender;
import net.reflxction.simplecapes.utils.ChatColor;
import net.reflxction.simplecapes.utils.ImageUtils;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Class which handles command input for "/simplecapes"
 */
public class SCCommand implements ICommand {

    // Red chat color, since Forge has issues when sending messages (if string needs a new line it becomes white)
    private final ChatColor red = ChatColor.RED;

    /**
     * Gets the name of the command
     */
    @Override
    public String getCommandName() {
        return "simplecapes";
    }

    /**
     * Gets the usage string for the command.
     *
     * @param sender The command sender that executed the command
     */
    @Override
    public String getCommandUsage(ICommandSender sender) {
        return "<toggle " +
                red +
                "/ animate " +
                red +
                "/ config [cape config path] " +
                red +
                "/ local [cape image name] " +
                red +
                "/ url [cape image url] " +
                red +
                "/ " +
                red +
                "clipboard>";
    }


    private final List<String> commandAliases = new ArrayList<>(); {
        commandAliases.add("sc");
        commandAliases.add("Sc");
    }
    @Override
    public List<String> getCommandAliases() {
        return commandAliases;
    }

    /**
     * Callback when the command is invoked
     *
     * @param sender The command sender that executed the command
     * @param args   The arguments that were passed
     */
    @Override
    public void processCommand(ICommandSender sender, String[] args) {
        switch (args.length) {
            case 0:
                SimpleSender.send("&cIncorrect command usage. " + red + "Try " + getCommandUsage(sender));
                break;
            case 1:
                switch (args[0]) {
                    case "animate":
                        SimpleCapes.getSettings().setAnimated(!SimpleCapes.getSettings().isAnimated());
                        SimpleSender.send(SimpleCapes.getSettings().isAnimated() ? "&aCape Animations has been enabled" : "&cCape Animations has been disabled.");
                        break;
                    case "toggle":
                        SimpleCapes.getSettings().setEnabled(!SimpleCapes.getSettings().isEnabled());
                        SimpleSender.send(SimpleCapes.getSettings().isEnabled() ? "&aSimpleCapes has been enabled" : "&cSimpleCapes has been disabled. &cSwitch &cworlds &cto &csee &cchanges.");
                        break;
                    case "clipboard":
                        if (ImageUtils.getImageFromClipboard() == null) {
                            SimpleSender.send("&cCouldn't find any image &cfrom &cclipboard");
                        } else {
                            SimpleCapes.getSettings().setCurrentMode(CapeMode.CLIPBOARD);
                            ImageUtils.saveImage(ImageUtils.getImageFromClipboard());
                            CapeDownloader.DOWNLOADER.updateCachedTexture();
                            SimpleCapes.getSettings().setCapeSet(true);
                            SimpleSender.send("&aSuccessfully updated cape image.");
                        }
                        break;
                    default:
                        SimpleSender.send("&cIncorrect command usage. Try " + getCommandUsage(sender));
                        break;
                }
                break;
            case 2:
                switch (args[0]) {
                    case "url":
                        SimpleSender.send("&eResolving image...");
                        String url = args[1];
                        BufferedImage capeImage = ImageUtils.getImageFromURL(url);
                        if (capeImage == null) {
                            SimpleSender.send("&cInvalid image URL!");
                        } else {
                            SimpleCapes.getSettings().setCurrentMode(CapeMode.URL);
                            SimpleCapes.getSettings().setCapeURL(url);
                            CapeDownloader.DOWNLOADER.updateCachedTexture();
                            SimpleCapes.getSettings().setCapeSet(true);
                            SimpleSender.send("&aSuccessfully updated cape image.");
                        }
                        break;
                    case "local":
                        String name = args[1];
                        if (ImageUtils.getImageFromFile(name) == null) {
                            SimpleSender.send("&cInvalid image name (srupid)");
                        } else {
                            SimpleCapes.getSettings().setCurrentMode(CapeMode.LOCAL);
                            SimpleCapes.getSettings().setCapePath(name);
                            CapeDownloader.DOWNLOADER.updateCachedTexture();
                            SimpleCapes.getSettings().setCapeSet(true);
                            SimpleSender.send("&aSuccessfully updated cape image.");
                        }
                        break;
                }
                break;
        }
    }

    /**
     * Returns true if the given command sender is allowed to use this command.
     *
     * @param sender The command sender that executed the command
     */
    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return true;
    }

    private final List<String> tabCompletions = new ArrayList<>(); {
        tabCompletions.add("toggle");
        tabCompletions.add("animate");
        tabCompletions.add("update");
        tabCompletions.add("check");
        tabCompletions.add("url");
        tabCompletions.add("local");
        tabCompletions.add("clipboard");
    }
    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos) {
        return tabCompletions;
    }

    /**
     * Return whether the specified command parameter index is a username parameter.
     *
     * @param args  The arguments that were passed
     * @param index Idk lul
     */
    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }

}
