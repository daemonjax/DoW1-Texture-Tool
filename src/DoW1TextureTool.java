import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;


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
 * @author Daemonjax
 */
final class DoW1TextureTool
{
    enum Arg { FILENAME, TARGET, COMMAND; }
    static final float MIN_DECAL_SIZE = 0.0105334455f;

    private DoW1TextureTool(){}


    public static final void main(final String[] args)
    {
        final Instant stop;
        final Instant start = Instant.now();
        //final String[] config = processConfig();
        final File f = processFile(args);
        final FileType fileType = FileType.get(f);
        final Target target = processTarget(args);
        final Command command = processCommand(args, fileType, target);
        final byte[] fileBytes; try { fileBytes = Files.readAllBytes(f.toPath()); } catch (Exception e) { Error.FILEREAD.exit(e); /*UNCREACHABLE*/ return; }

        switch (fileType)
        {
            case MAP_SGB:
            {
                Utils.sb.append(Strings.MAP_SGB_FILE).append(Strings.NEWLINE);

                int offset = DataSMap.findStartingOffset(fileBytes, 0);
                int numNodes = DataSMap.getNumNodes(fileBytes, offset);
                offset += DataSMap.Header.TOTAL_SIZE;

                if (target == Target.INFO)
                {
                    Utils.sb.append(Strings.TARGET_INFO).append(Strings.NEWLINE);

                    for (int i = 0, thisDecalPathCharsLength; i < numNodes; ++i)
                    {
                        thisDecalPathCharsLength = Utils.getBEintFromLEbytes(fileBytes, offset);
                        Utils.sb.append(DataSMap.getDecalPathChars(fileBytes, offset, thisDecalPathCharsLength)).append(Strings.NEWLINE);
                        offset = DataSMap.nextDecalTypesNodeOffset(offset, thisDecalPathCharsLength);
                    }
                }
                else if (command != Command.SET)
                {
                    final float multiplier = command.getValueFromArg(args);
                    final byte[] targetDecalPathChars = Utils.getBytesFromChars(target.getValueFromArg(args).toCharArray());
                    offset = getUniqueIDOffsetFromPathChars(fileBytes, targetDecalPathChars, numNodes, offset);
                    final byte[] uniqueID = {fileBytes[offset], fileBytes[offset + 1], fileBytes[offset + 2], fileBytes[offset + 3]};
                    final int[] counters;

                    Utils.sb.append(Strings.FOUND_UNIQUE_ID).append(Strings.NEWLINE);

                    offset = DataEnty.findStartingOffset(fileBytes, offset + DataSMap.Node.NUM_BYTES_UNIQUE_ID);
                    numNodes = DataEnty.getNumNodes(fileBytes, offset);
                    offset += DataEnty.Header.TOTAL_SIZE + DataEnty.Node.UNIQUE_ID.relativeOffset;

                    if (command == Command.MULTIPLY) Utils.sb.append(Strings.MULTIPLY_MESSAGE_1).append(multiplier).append(Strings.NEWLINE);
                    else Utils.sb.append(Strings.INFO_MESSAGE_1).append(Strings.NEWLINE);

                    counters = applyMultiplier(fileBytes, offset, numNodes, uniqueID, command, multiplier);

                    if (command == Command.MULTIPLY)
                    {
                        if (counters[1] == 0) Utils.sb.append(Strings.MULTIPLY_DECAL_COUNTER).append(counters[0]);
                        else Utils.sb.append(Strings.MULTIPLY_DECAL_COUNTER).append(counters[0]).append(Strings.NEWLINE).append(Strings.MULTIPLY_DECAL_MINSIZE_COUNTER).append(counters[1]);
                    }
                    else Utils.sb.append(Strings.INFO_MESSAGE_NUM_DECALS_FOUND).append(counters[0]);
                }

                break;
            }
            case MODEL_WHE:
            {
                Utils.sb.append(Strings.MODEL_WHE_FILE).append(Strings.NEWLINE);
                Error.NOT_IMPLEMENTED.exit(new Exception());
                break;
            }
        }

        Utils.sb.append(Strings.ALL_DONE);

        if (target == Target.INFO || command == Command.INFO) Utils.sb.append(Strings.INFO_DONE);
        else
        {
            File savefile = null;

            for (int i = 1; i < 100; ++i, savefile = null)
            {
                savefile = new File(f.getPath() + Strings.EXTENSION + String.format(Strings.SAVE_FORMAT, i));
                if (!savefile.exists()) break;
            }

            if (savefile != null) Utils.sb.append(Strings.SAVING_FILE).append(savefile.getPath());
            else { Error.SAVEFILE.exit(new Exception()); /*UNREACHABLE*/ return; }
            try { Files.write(savefile.toPath(), fileBytes); } catch (Exception e) { Error.SAVEFILE.exit(e); /*UNREACHABLE*/ return; }
        }

        System.out.println(Utils.sb.toString());

        stop = Instant.now();
        Utils.displayTimer(start, stop);
    }

    static final String[] processConfig()
    {
        final int length = Config.Keys.VALUES.length;
        final String[] result = new String[length];
        int i = 0;

        try ( FileInputStream fis = new FileInputStream(Config.getConfigFile()); InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8); BufferedReader br = new BufferedReader(isr, 8192); )
        {
            for (String line = br.readLine(); line != null; line = br.readLine())
            {
                line = line.stripLeading();
                if (!line.isEmpty() && line.charAt(0) != ';' && line.startsWith(Config.Keys.VALUES[i].keyName, 0))
                {
                    result[i] = line.substring(line.indexOf('=') + 1).strip();
                    ++i;
                }
            }
        } catch (Exception e) { Error.CONFIG_FILEREAD.exit(e); }
        if (i == length) return result;
        return (String[])Error.CONFIG_BAD.exit(new Exception());
    }

    static final File processFile(final String[] args)
    {
        final File result;
        final String filename;
        final int ordinal = Arg.FILENAME.ordinal();

        if (args.length > ordinal)
        {
            filename = args[ordinal];
            Utils.sb.append(Strings.FILE).append(filename).append(Strings.NEWLINE);
            result = new File(filename);
            if (result.exists() && result.isFile() && result.canRead() && result.length() > 0) return result;
            Error.PROCESS_FILE.exit(new Exception());
        }
        if (args.length == 0) Error.NOT_AN_ERROR.cleanExit();
        return (File)Error.ARGSINVALID.exit(new Exception());
    }

    static final Target processTarget(final String[] args)
    {
        final String s;
        final int ordinal = Arg.TARGET.ordinal();

        if (args.length > ordinal)
        {
            s = args[ordinal] = args[ordinal].replace('\\', '/');
            Utils.sb.append(Strings.TARGET).append(s).append(Strings.NEWLINE);
            return Target.get(s);
        }

        return (Target)Error.PROCESS_TARGET.exit(new Exception());
    }

    static final Command processCommand(final String[] args, final FileType fileType, final Target target)
    {
        final Command result;
        final String s;

        if (target == Target.INFO) return Command.INFO;

        if (args.length > Arg.COMMAND.ordinal())
        {
            s = args[Arg.COMMAND.ordinal()];
            Utils.sb.append(Strings.COMMAND).append(s).append(Strings.NEWLINE);
            result = Command.get(s);
            if (result.isValidFor(fileType)) return result;
            Error.COMMAND_INVALID.exit(new Exception());
        }

        return (Command)Error.PROCESS_COMMAND.exit(new Exception());
    }

    static final int[] applyMultiplier(final byte[] fileBytes, int offset, final int numNodes, final byte[] uniqueID, final Command command, final float multiplier)
    {
        int counter = 0;
        int decalMinSizeCounter = 0;
        float decalSize;

        for (int i = 0, decalSizeOffset; i < numNodes; ++i, offset += DataEnty.Node.TOTAL_SIZE)
        {
            if (uniqueID[0] == fileBytes[offset] && uniqueID[1] == fileBytes[offset + 1] && uniqueID[2] == fileBytes[offset + 2] && uniqueID[3] == fileBytes[offset + 3])
            {
                 ++counter;

                decalSizeOffset = offset + DataEnty.Node.DECAL_SIZE.relativeOffset - DataEnty.Node.UNIQUE_ID.relativeOffset;
                decalSize = Utils.getBEfloatFromLEbytes(fileBytes, decalSizeOffset);

                if (command == Command.MULTIPLY)
                {
                    decalSize *= multiplier;
                    if (decalSize < MIN_DECAL_SIZE)
                    {
                        decalSize = MIN_DECAL_SIZE;
                         ++decalMinSizeCounter;
                    }
                    DataEnty.setDecalSize(fileBytes, decalSizeOffset, decalSize);
                }
                else Utils.sb.append(decalSize).append(Strings.NEWLINE);
            }
        }

        return new int[] { counter, decalMinSizeCounter };
    }

    static final int getUniqueIDOffsetFromPathChars(final byte[] fileBytes, final byte[] targetDecalPathChars, final int numDecalTypes, int dataSMapNodeOffset)
    {
        final int targetDecalPathCharsLength = targetDecalPathChars.length;
        for (int i = 0, thisDecalPathCharsLength; i < numDecalTypes; ++i, dataSMapNodeOffset = DataSMap.nextDecalTypesNodeOffset(dataSMapNodeOffset, thisDecalPathCharsLength))
        {
            thisDecalPathCharsLength = Utils.getBEintFromLEbytes(fileBytes, dataSMapNodeOffset + DataSMap.Node.DECAL_PATH_CHARS_LENGTH.relativeOffset);

            if (targetDecalPathCharsLength == thisDecalPathCharsLength && areDecalPathCharsEqual(targetDecalPathChars, targetDecalPathCharsLength, fileBytes, dataSMapNodeOffset + DataSMap.Node.DECAL_PATH_CHARS.relativeOffset))
            {
                return dataSMapNodeOffset + DataSMap.Node.DECAL_PATH_CHARS.relativeOffset + targetDecalPathCharsLength;
            }
        }
        return (int)Error.UNIQUE_ID_NOT_FOUND.exit(new Exception());
    }

    static final boolean areDecalPathCharsEqual(final byte[] pattern, final int patternlength, final byte[] mapFileBytes, final int mapFileBytesOffset)
    {
        for (int i = 0; i < patternlength; ++i) { if (pattern[i] != mapFileBytes[mapFileBytesOffset + i]) return false; }
        return true;
    }

}
