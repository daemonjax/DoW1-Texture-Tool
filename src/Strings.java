/**
 *
 * @author daemonjax
 */
final class Strings
{
    static final String USAGE = "\n\nUsage:\nDoW1TextureTool \"<FILEPATH>\" <TARGET> <COMMAND>\n\n" + getTargetList() + "\n" + getCommandList() + "\n";
    static final String COMMAND = "Command: ";
    static final String TARGET  = "Target: ";
    static final String FILE = "File: ";
    static final String EQUAL_SIGN = "=";
    static final String NEWLINE = "\n";
    static final String LIST_SEPERATOR = " | ";
    static final String MAP_SGB_FILE = "Map SGB file detected.";
    static final String TARGET_INFO = "Info: Here's a complete list of valid decal targets found in this map file:";
    static final String MODEL_WHE_FILE = "Model WHE file detected.";
    static final String EMPTY = "";
    static final String TARGET_EXAMPLE_DECAL = "=\"<decal file path>\"";
    static final String COMMAND_EXAMPLE_MULTIPLY = "=<float value>";
    static final String COMMAND_EXAMPLE_SET = "=<float value>";
    static final String FOUND_UNIQUE_ID = "Found unique ID for decal within this map.";
    static final String MULTIPLY_MESSAGE_1 = "Multiplying the size of all instances of this decal by: ";
    static final String INFO_MESSAGE_1 = "List of current sizes for each instance of this decal: ";
    static final String MULTIPLY_MESSAGE_2 = "Total number of these decals modified on this map: ";
    static final String INFO_MESSAGE_2 = "Total number of these decals found in this map: ";
    static final String ALL_DONE = "All done!";    
    static final String SAVE_FORMAT = "-%02d";
    static final String SAVING_FILE = "Saving modified file to: ";
    static final String INFO_DONE = "No files have been modified.";
    static final String EXTENSION = ".dow1tt";
    static final String FULL_EXAMPLES = """
                                        Full Examples:
                                        "C:/Dawn of War/My_Mod/data/scenarios/sp/mymap.sgb" -info
                                        "C:/Dawn of War/My_Mod/data/scenarios/sp/mymap.sgb" -decal="art/decals/z_all/terrain_dust_02" -info
                                        "C:/Dawn of War/My_Mod/data/scenarios/sp/mymap.sgb" -decal="art/decals/z_all/terrain_dust_02" -mul=0.5
                                        """;
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