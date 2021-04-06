# DoW1-Texture-Tool
This is a mod tool for Dawn of War 1 games.

Download link: https://github.com/daemonjax/DoW1-Texture-Tool/releases

Discussion thread: https://www.nexusmods.com/warhammer40000dawnofwar/mods/326?tab=posts

If you want to replace a decal with a higher resolution version in a map or model, you'll run into a problem -- the decal just gets bigger ingame and not higher resolution.

This tool fixes that by allowing you to very quickly modify the physical decal size shown ingame -- so your higher res textures are actually higher res and don't simply take up more space.

Definitely works with Dark Crusade files.  Should work with Soul Storm files (but I haven't tested it).  Might work with Do1 original and Winter Assault (not sure).  It'll either work perfectly or "fail-fast" during execution if the internal structure of the files changed between the DoW 1 versions, so you'll know quickly one way or another.

Feature list:
* SGB MAP FILES:
  * List the filenames for all decals used in a map (1.0)
  * List all the current sizes (as float value) of a specific decal used in a map (1.0)
  * Resize all instances of a specific decal used in a map by multiplying it by an arbitrary float value (1.0)
  * Can Specify folder to affect all map file in that folder (1.3)
  * Options (-o = overwrite, -l = log output to file) (1.3)
  * Specify a text file which contains a list of targets and commands to execute: -list=<list_file> (1.3




Currently it just works on SGB map files, but that is the most important feature because doing it by hand would be next to impossible.

Targets Java 11 features, so you'll need to use Java 11+ to run it.

To run:

java -jar DoW1TextureTool.jar

The above will print a "usage" screen which includes examples:

[code]
DoW1 Texture Tool v1.3.0  Copyright (C) 2021  Daemonjax
This program comes with ABSOLUTELY NO WARRANTY. This is free software, and you
are welcome to redistribute it under certain conditions.  See included LICENSE
file for more info, or go to https://www.gnu.org/licenses/gpl-3.0.html

Usage:
DoW1TextureTool "<FILE or FOLDER PATH>" <TARGET> <COMMAND> <OPTIONS>

Target: -decal="<decal file path>" | -list="<arguement list file path>" | -info
Command: -mul=<float value> | -set=<float value> | -info
Options: -{ o | l }
     -o == overwrite files
     -l == send output to logfile

Full Examples:
"C:/Dawn of War/My_Mod/data/scenarios/sp/mymap.sgb" -info
"C:/Dawn of War/My_Mod/data/scenarios/sp/mymap.sgb" -decal="art/decals/z_all/terrain_dust_02" -info
"C:/Dawn of War/My_Mod/data/scenarios/sp/mymap.sgb" -decal="art/decals/z_all/terrain_dust_02" -mul=0.5
"C:/Dawn of War/My_Mod/data/scenarios/sp" -decal="art/decals/z_all/terrain_dust_02" -mul=0.5 -ol
"C:/Dawn of War/My_Mod/data/scenarios/sp" -list="testArgList.DoW1tt" -o
NOTE: The -set command is not yet implemented, and neither is .WHE model file support.
[/code]
