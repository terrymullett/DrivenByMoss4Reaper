// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2018
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.transformator;

import de.mossgrabers.transformator.util.PropertiesEx;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


/**
 * The main configuration file.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class MainConfiguration extends PropertiesEx
{
    private static final long   serialVersionUID = 2863435764890303358L;

    private static final String CONFIG_FILENAME  = "DrivenByMoss4Reaper.config";


    /**
     * Load the settings from the config file.
     *
     * @throws IOException Could not load configuration file
     */
    public void load () throws IOException
    {
        final File configFile = new File (CONFIG_FILENAME);
        if (!configFile.exists ())
            return;

        try (final FileReader reader = new FileReader (configFile))
        {
            this.load (reader);
        }
    }


    /**
     * Store the settings to the config file.
     *
     * @throws IOException Could not store
     */
    public void save () throws IOException
    {
        final File configFile = new File (CONFIG_FILENAME);
        try (final FileWriter writer = new FileWriter (configFile))
        {
            this.store (writer, "");
        }
    }
}
