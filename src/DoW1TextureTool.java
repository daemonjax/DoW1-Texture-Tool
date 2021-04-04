import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;


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
    enum Arg { FILENAME, TARGET, COMMAND, OPTIONS; }
    static final float MIN_DECAL_SIZE = 0.0105334455f;
    static final int MIN_ARGUEMENTS = 3;


    private DoW1TextureTool(){}

    static final class DoWFileNameFilter implements FilenameFilter
    {
        private final String extension;
        DoWFileNameFilter(String extension) { this.extension = extension.toLowerCase(); }
        @Override public boolean accept(File dir, String name) { return name.toLowerCase().endsWith(extension); }
    }

    public static final void main(final String[] args)
    {
        final Instant stop;
        final Instant start = Instant.now();
        //final String[] config = processConfig();
        final File[] files = processFile(args);
        final FileType fileType = FileType.get(files[0]);
        Target target = Target.process(args);
        Command command = (args.length > Arg.COMMAND.ordinal() && args[Arg.COMMAND.ordinal()].equalsIgnoreCase(Command.INFO.text)) ? Command.INFO : null;
        final int optionMask = Option.getOptionMask(args, target, command);

        String[][] argList;

        if (target == Target.LIST)
        {
            Utils.sb.append(Strings.ARG_LIST_DETECTED).append(Strings.NEWLINE);
            argList = getArgList(args[Arg.TARGET.ordinal()].substring(args[Arg.TARGET.ordinal()].indexOf('=') + 1).stripLeading(), args[0]);

            if (command == Command.INFO)
            {
                Utils.sb.append(Strings.NEWLINE).append(Strings.ARG_LIST_CHECKING).append(Strings.NEWLINE).append(Strings.NEWLINE);
                for (int i = 0; i < argList.length; ++i) { Command.process(argList[i], fileType, Target.process(argList[i]), true); }
                Utils.sb.append(Strings.NEWLINE).append(Strings.ARG_LIST_OK).append(Strings.NEWLINE);
                Utils.outputAllMessages(Option.LOG.isSet(optionMask));
                stop = Instant.now();
                Utils.displayTimer(start, stop);
                return;
            }
            else if (!Option.OVERWRITE.isSet(optionMask)) Error.LIST_REQUIRES_OVERWRITE.exit();
        }
        else
        {
            argList = new String[1][args.length];
            argList[0] = args;
        }

        final boolean doSave = (target != Target.INFO && command != Command.INFO);
        byte[] fileBytes;


        switch (fileType)
        {
            case MAP_SGB:
            {
                Utils.sb.append(Strings.MAP_SGB_FILE).append(Strings.NEWLINE).append(Strings.NEWLINE);
                int bytesWereModified;

                for (int i = 0; i < files.length; ++i)
                {
                    try { fileBytes = Files.readAllBytes(files[i].toPath()); } catch (Exception e) { Error.FILEREAD.exit(e); /*UNCREACHABLE*/ return; }

                    bytesWereModified = 0;
                    for (int j = 0; j < argList.length; ++j)
                    {
                        target = Target.process(argList[j]);
                        command = Command.process(argList[j], fileType, target, true);
                        if (doMapOperation(files[i], fileBytes, target, command, argList[j])) ++bytesWereModified;
                    }
                    if (doSave && bytesWereModified > 0) Utils.save(files[i], fileBytes, Strings.EXTENSION_SAVE, Option.OVERWRITE.isSet(optionMask));
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

        Utils.sb.append(Strings.ALL_DONE).append(Strings.NEWLINE);
        if (!doSave) Utils.sb.append(Strings.INFO_DONE).append(Strings.NEWLINE);
        Utils.outputAllMessages(Option.LOG.isSet(optionMask));

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


    static final File[] processFile(final String[] args)
    {
        final int ordinal = Arg.FILENAME.ordinal();
        final File f;

        if (args.length > ordinal)
        {
            Utils.sb.append(Strings.FILE_OR_FOLDER).append(args[ordinal]).append(Strings.NEWLINE);
            f = new File(args[ordinal]);
            if (f.exists())
            {
                if (f.isFile())
                {
                    if (f.canRead() && f.length() > 0) return new File[] { f };
                }
                else // is a folder...
                {
                    final File[] result = f.listFiles(new DoWFileNameFilter(Strings.FILE_MAP));
                    if (result.length > 0)
                    {
                        for (int i = 0; i < result.length; ++i)
                        {
                            Utils.sb.append(Strings.INDENT).append(result[i].getName()).append(Strings.NEWLINE);

                            if (!(result[i].canRead()) || result[i].length() <= 0) return (File[])Error.PROCESS_FILES.exit(new Exception());
                        }
                        return result;
                    }
                    else return (File[])Error.EMPTY_FOLDER.exit(new Exception());
                }
            }
            Error.PROCESS_FILE.exit(new Exception());
        }
        if (args.length == 0) Error.NOT_AN_ERROR.cleanExit();
        return (File[])Error.ARGSINVALID.exit(new Exception());
    }

    static final String[][] getArgList(final String argListFilename, final String firstArg)
    {
        final File f = Utils.getNotEmptyFile(argListFilename);

        if (f != null)
        {
            ArrayList<String> arrayList = new ArrayList<>(32);

            try (BufferedReader br = new BufferedReader(new FileReader(f), 8192))
            {
                String s;
                while (br.ready())
                {
                    s = br.readLine().strip();
                    if (!s.isEmpty() && s.charAt(0) != ';') arrayList.add(s);
                }
            }
            catch (Exception e) { return (String[][])Error.FILEREAD.exit(e, f.getName()); }

            String[][] result = new String[arrayList.size()][3];
            int index, sLength;
            String s;
            final int length = arrayList.size();

            if (length > 0)
            {
                for (int i = 0; i < length; ++i)
                {
                    result[i][0] = firstArg;
                    s = arrayList.get(i);
                    sLength = s.length();

                    if (sLength > 8)
                    {
                        if (s.charAt(0) == '-')
                        {
                            index = s.indexOf('=', 2);
                            if (index > 0)
                            {
                                index = s.indexOf(' ', index);
                                if (index > 0)
                                {
                                    result[i][1] = s.substring(0, index);
                                    index = s.indexOf('-', index);
                                    if (index > 0)
                                    {
                                        if (s.indexOf('=', index) > 0)
                                        {
                                            result[i][2] = s.substring(index, sLength);
                                            continue;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    return (String[][])Error.ARG_LIST_BAD.exit(new Exception(), s);
                }
                return result;
            }
            return (String[][])Error.ARG_LIST_BAD.exit(new Exception());
        }

        return (String[][])Error.ARG_LIST_DNE.exit(new Exception(), new File(argListFilename).getAbsolutePath());
    }

    static final boolean doMapOperation(final File file, final byte[] fileBytes, final Target target, final Command command, final String[] args)
    {
        final int[] counters = new int[2];
        final byte[] uniqueID = new byte[4];
        int offset = DataSMap.findStartingOffset(fileBytes, 0);
        int numNodes = DataSMap.getNumNodes(fileBytes, offset);
        offset += DataSMap.Header.TOTAL_SIZE;
        String fileName = file.getName();

        if (target == Target.INFO)
        {
            Utils.sb.append(Strings.NEWLINE).append(Strings.TARGET_INFO_1).append(fileName).append(Strings.TARGET_INFO_2).append(Strings.NEWLINE);

            for (int j = 0, thisDecalPathCharsLength; j < numNodes; ++j)
            {
                thisDecalPathCharsLength = Utils.getBEintFromLEbytes(fileBytes, offset);
                Utils.sb.append(Strings.INDENT).append(DataSMap.getDecalPathChars(fileBytes, offset, thisDecalPathCharsLength)).append(Strings.NEWLINE);
                offset = DataSMap.nextDecalTypesNodeOffset(offset, thisDecalPathCharsLength);
            }
        }
        else if (command == Command.MULTIPLY || command == Command.INFO)
        {
            final byte[] targetDecalPathChars = Utils.getBytesFromChars(target.getValueFromArg(args).toCharArray());
            offset = getUniqueIDOffsetFromPathChars(fileBytes, targetDecalPathChars, numNodes, offset);

            if (offset < 0)
            {
                Utils.sb.append(Strings.UNIQUE_ID_NOT_FOUND).append(fileName).append(Strings.ENDING_1).append(Strings.NEWLINE);
                return false;
            }

            Utils.sb.append(Strings.UNIQUE_ID_FOUND).append(fileName).append(Strings.ENDING_1).append(Strings.NEWLINE);

            uniqueID[0] = fileBytes[offset]; uniqueID[1] = fileBytes[offset + 1]; uniqueID[2] = fileBytes[offset + 2]; uniqueID[3] = fileBytes[offset + 3];
            offset = DataEnty.findStartingOffset(fileBytes, offset + DataSMap.Node.NUM_BYTES_UNIQUE_ID);
            numNodes = DataEnty.getNumNodes(fileBytes, offset);
            offset += DataEnty.Header.TOTAL_SIZE + DataEnty.Node.UNIQUE_ID.relativeOffset;
            final float multiplier = command.getValueFromArg(args);

            if (command == Command.MULTIPLY) Utils.sb.append(Strings.INDENT).append(Strings.MULTIPLY_MESSAGE_1).append(multiplier).append(Strings.NEWLINE);
            else Utils.sb.append(Strings.INDENT).append(Strings.INFO_MESSAGE_1).append(Strings.NEWLINE);

            applyMultiplier(fileBytes, offset, numNodes, uniqueID, command, multiplier, counters);


            if (command == Command.MULTIPLY)
            {
                Utils.sb.append(Strings.INDENT).append(Strings.MULTIPLY_DECAL_COUNTER).append(counters[0]).append(Strings.NEWLINE);
                if (counters[1] > 0) Utils.sb.append(Strings.INDENT).append(Strings.MULTIPLY_DECAL_MINSIZE_COUNTER).append(counters[1]).append(Strings.NEWLINE);
            }
            else Utils.sb.append(Strings.INDENT).append(Strings.INFO_MESSAGE_NUM_DECALS_FOUND).append(counters[0]).append(Strings.NEWLINE);

            return true;
        }

        return false;
    }


    static final void applyMultiplier(final byte[] fileBytes, int offset, final int numNodes, final byte[] uniqueID, final Command command, final float multiplier, final int[] counters)
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
                else Utils.sb.append(Strings.INDENT).append(Strings.INDENT).append(decalSize).append(Strings.NEWLINE);
            }
        }

        counters[0] = counter;
        counters[1] = decalMinSizeCounter;
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
        //return (int)Error.UNIQUE_ID_NOT_FOUND.exit(new Exception());
        return -1;
    }

    static final boolean areDecalPathCharsEqual(final byte[] pattern, final int patternlength, final byte[] mapFileBytes, final int mapFileBytesOffset)
    {
        for (int i = 0; i < patternlength; ++i) { if (pattern[i] != mapFileBytes[mapFileBytesOffset + i]) return false; }
        return true;
    }

}
