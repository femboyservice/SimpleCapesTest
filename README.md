[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)
# SimpleCapesTest
A MinecraftForge mod which adds capes to the player

## Explanation
The mod basically has 3 modes:
* **URL mode**: It will get the cape image from the URL. To switch to this mode, **/simplecapestest url \<cape url>** is used.
* **Local mode**: It will take the cape image from your computer. The image must be in **.minecraft/simplecapes/** directory. To switch to this mode, **/simplecapestest local \<image name>**. Don't forget to add the extension (.png, .jpg, etc.)
* **Clipboard mode**: It will take the cape image from your current clipboard. To use this mode, **/simplecapes clipboard** is used. Note that, when you use this mode, the image is saved in **.minecraft/simplecapestest/clipboard.png**, and will be used later, and only changes when you use the command with another clipboard image.

## Commands
Command: **/simplecapestest**. Aliases: **/Simplecapestest**, **/sct**, **/Sct**
* /sct toggle - Toggles the mod
* /sct animate - Toggles the cape animation (if available)
* /sct url <url> - Sets the cape image from the given URL
* /sct local <image name> - Sets the cape image from your machine. The image **must** be in **.minecraft/simplecapes/**. Adding the extension of the image is also required. Refer to the explanation section for more details
* /sct clipboard - Sets the cape image from your current image clipboard.

## Examples
* /sct url https://i.imgur.com/2zndJGu.png
* /sct local mojang.png

## Autocomplete Example
Inside **".minecraft/simplecapetest/capes.json"**, you can write format like this to have it autocomplete on /sct local <name>.
Here's an example:
```json
{
  "animatedExample": {
    "path": "animatedExample/1.png",
    "animated": true
  },

  "staticExample": {
    "path": "example.webp",
    "animated": false
  }
}
```
In more details, the key is the in-game command autocomplete (animatedExample, staticExample);
the "path" value is the path to the cape, starting from **".minecraft/simplecapestest/"**; and to finish,
the "animated" value is to set the cape animation state.

## Animated Example
![example image](https://r2.e-z.host/dea88475-106f-4841-8b38-faf65c8c42bc/y8atcb1m.png)
In a directory named after the cape, make every frame of your cape a valid cape with each frame
named the frame's index, starting from 1. (1.png, 2.png, 3.png, ...)
Make a new file named **"config.json"** and have it's content set to this:
```json
{
  "frameTime": 3,
  "frameAmount": 20
}
```

frameTime is the amount of tick waited before going to the next image (here 3 ticks = 0.15s)
and frameAmount the amount of frames there is (here 20 images)

A valid example of an animated cape can be found in **"./animatedExample/"** in this repo.
Here's a video:

https://github.com/user-attachments/assets/d1dfc532-3ad1-4fed-9b39-e343f9d2e819
