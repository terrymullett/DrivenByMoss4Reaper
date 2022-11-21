// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2022
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.framework.device;

/**
 * All available device locations.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public enum DeviceLocation
{
    /** AU devices. */
    AU("AU"),
    /** AU devices. */
    CLAP("CLAP"),
    /** Jesusonic effects. */
    JS("JS"),
    /** VST devices. */
    VST("VST");


    private String name;


    /**
     * Constructor.
     *
     * @param name The name of the device location
     */
    private DeviceLocation (final String name)
    {
        this.name = name;
    }


    /**
     * Get the name of the device location.
     *
     * @return The name
     */
    public String getName ()
    {
        return this.name;
    }
}
