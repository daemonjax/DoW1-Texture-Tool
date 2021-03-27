import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;

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
 * @author daemonjax
 */
final class Config
{
    enum Keys
    {
        MY_MOD(Strings.CONFIG_KEY_MYMOD, Strings.EMPTY),
        DOW1_EXTRACTED_W40K(Strings.CONFIG_KEY_EXTRACTED_W40K, Strings.EMPTY),
        DOW1_EXTRACTED_WA(Strings.CONFIG_KEY_EXTRACTED_WA, Strings.EMPTY),
        DOW1_EXTRACTED_DC(Strings.CONFIG_KEY_EXTRACTED_DC, Strings.EMPTY),
        DOW1_EXTRACTED_SS(Strings.CONFIG_KEY_EXTRACTED_SS, Strings.EMPTY);
        static final Keys[] VALUES = values();
        final String keyName;
        final String valueDefault;

        private Keys(final String keyName, final String valueDefault){ this.keyName = keyName; this.valueDefault = valueDefault; }
    }

    private Config(){}


    static final File getConfigFile()
    {
        File result = Utils.getNotEmptyFile(Strings.CONFIG_FILENAME);

        if (result != null) return result;
        else
        {
            Error.CONFIG_FILE_BAD.warn();
            result = Utils.getNotEmptyFile(Strings.CONFIG_FILENAME_DEFAULT);
            Keys.DOW1_EXTRACTED_DC.ordinal();
            if (result != null) return result;
        }

        return buildDefaults();
    }

    private static final File buildDefaults()
    {
        final File file = new File(Strings.CONFIG_FILENAME_DEFAULT);
        final int length = Keys.VALUES.length;

        Utils.sb.append(Strings.CONFIG_CREATING_NEW);

        try ( FileOutputStream fos = new FileOutputStream(file); BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos, StandardCharsets.UTF_8)); )
        {
            for (int i = 0; i < length; ++i)
            {
                bw.write(Keys.VALUES[i].keyName + Strings.EQUAL_SIGN + Keys.VALUES[i].valueDefault);
                bw.newLine();
            }

        }
        catch (Exception e) { Error.CONFIG_WRITING_DEFAULTS.exit(e); }

        Utils.sb.append(Strings.SUCESS).append(Strings.NEWLINE);
        return file;
    }

}
