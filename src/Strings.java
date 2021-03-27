/*  DoW1 Texture Tool
    Copyright (C) 2021  Daemonjax

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/gpl-3.0.html>.
*/

/**
 *
 * @author daemonjax
 */
final class Strings
{
    static final String VERSION = "DoW1 Texture Tool v1.0.1";
    static final String LICENSE_INFO = "\n" + VERSION + "  Copyright (C) 2021  Daemonjax\n" +
                                       "This program comes with ABSOLUTELY NO WARRANTY. This is free software, and you\n" +
                                       "are welcome to redistribute it under certain conditions.  See included LICENSE\n" +
                                       "file for more info, or go to https://www.gnu.org/licenses/gpl-3.0.html";
    static final String USAGE = LICENSE_INFO + "\n\nUsage:\nDoW1TextureTool \"<FILEPATH>\" <TARGET> <COMMAND>\n\n" + getTargetList() + "\n" + getCommandList() + "\n";
    static final String FATAL_ERROR = ("!!!FATAL ERROR!!! ");
    static final String WARNING = ("Warning: ");    
    static final String CONFIG_FILENAME = "DoW1TextureTool.ini";
    static final String CONFIG_FILENAME_DEFAULT = CONFIG_FILENAME + ".default";
    static final String CONFIG_COMMENT = ";";
    static final String CONFIG_CREATING_NEW = "Creating a new default ini config file...  ";
    static final String CONFIG_KEY_MYMOD = "mod module file";
    static final String CONFIG_KEY_EXTRACTED_W40K = "W40k data folder";
    static final String CONFIG_KEY_EXTRACTED_WA   = "WA data folder";
    static final String CONFIG_KEY_EXTRACTED_DC   = "DC data folder";
    static final String CONFIG_KEY_EXTRACTED_SS   = "SS data folder";    
    static final String COMMAND = "Command: ";
    static final String TARGET  = "Target: ";
    static final String FILE = "File: ";
    static final String EQUAL_SIGN = "=";
    static final String NEWLINE = "\n";
    static final String LIST_SEPERATOR = " | ";
    static final String MAP_SGB_FILE = "Map SGB file detected.";
    static final String TARGET_INFO = "Info: Here's a complete list of valid decal targets found in this map file:";
    static final String MODEL_WHE_FILE = "Model WHE file detected.  Unfortunaetly this feature is not yet implemented.";
    static final String EMPTY = "";
    static final String TARGET_EXAMPLE_DECAL = "=\"<decal file path>\"";
    static final String COMMAND_EXAMPLE_MULTIPLY = "=<float value>";
    static final String COMMAND_EXAMPLE_SET = "=<float value>";
    static final String FOUND_UNIQUE_ID = "Found unique ID for decal within this map.";
    static final String MULTIPLY_MESSAGE_1 = "Multiplying the size of all instances of this decal by: ";
    static final String INFO_MESSAGE_1 = "List of current sizes for each instance of this decal: ";
    static final String MULTIPLY_DECAL_COUNTER = "Total number of these decals modified on this map: ";
    static final String MULTIPLY_DECAL_MINSIZE_COUNTER = "The number of decals that were prevented from going below the minimum size: ";
    static final String INFO_MESSAGE_NUM_DECALS_FOUND = "Total number of these decals found in this map: ";
    static final String ALL_DONE = "\nAll done!  ";    
    static final String SAVE_FORMAT = "-%02d";
    static final String SAVING_FILE = "Saving modified file to: ";
    static final String INFO_DONE = "No files have been modified or created because of -info switch.";
    static final String EXTENSION = ".dow1tt";
    static final String FULL_EXAMPLES = "Full Examples:\n" +
                                        "\"C:/Dawn of War/My_Mod/data/scenarios/sp/mymap.sgb\" -info\n" +
                                        "\"C:/Dawn of War/My_Mod/data/scenarios/sp/mymap.sgb\" -decal=\"art/decals/z_all/terrain_dust_02\" -info\n" +
                                        "\"C:/Dawn of War/My_Mod/data/scenarios/sp/mymap.sgb\" -decal=\"art/decals/z_all/terrain_dust_02\" -mul=0.5\n" + 
                                        "NOTE: The -set command is not yet implemented, and neither is .WHE model file support.\n";
    static final String SUCESS = "Success!  ";
                                        
    private Strings(){}

    private static final String getCommandList()
    {
        final int length = Command.VALUES.length - 1;
        String s = Strings.COMMAND;
        Command c;

        for(int i = 0; i < length; ++i)
        {
            c = Command.VALUES[i];
            s += c.text + c.getExample() + Strings.LIST_SEPERATOR;
        }
        c = Command.VALUES[length];
        return s + c.text + c.getExample();
    }

    private static final String getTargetList()
    {
        final int length = Target.VALUES.length - 1;
        String s = Strings.TARGET;
        Target t;

        for(int i = 0; i < length; ++i)
        {
            t = Target.VALUES[i];
            s += t.text + t.getExample() + Strings.LIST_SEPERATOR;
        }
        t = Target.VALUES[length];
        return s + t.text + t.getExample();
    }
}
