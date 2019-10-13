// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.framework.daw.midi;

/**
 * List of arpeggiator modes.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public enum NoteRepeatModes
{
    /** All mode. */
    ALL,
    /** Up mode. */
    UP,
    /** Up/Down mode. */
    UP_DOWN,
    /** Up then down mode. */
    UP_THEN_DOWN,
    /** Down mode. */
    DOWN,
    /** Down/Up mode. */
    DOWN_UP,
    /** Down then up mode. */
    DOWN_THEN_UP,
    /** Flow mode. */
    FLOW,
    /** Random mode. */
    RANDOM,
    /** Converge up mode. */
    CONVERGE_UP,
    /** Converge down mode. */
    CONVERGE_DOWN,
    /** Diverge up mode. */
    DIVERGE_UP,
    /** Diverge down mode. */
    DIVERGE_DOWN,
    /** Thumb up mode. */
    THUMB_UP,
    /** Thumb down mode. */
    THUMB_DOWN,
    /** Pikny up mode. */
    PINKY_UP,
    /** Pinky down mode. */
    PINKY_DOWN;


    private static final String [] NAMES;
    private static final String [] VALUES;
    static
    {
        final NoteRepeatModes [] values = NoteRepeatModes.values ();
        NAMES = new String [values.length];
        VALUES = new String [values.length];
        for (int i = 0; i < values.length; i++)
        {
            final String name = values[i].name ();
            NAMES[i] = name.substring (0, 1) + name.substring (1).toLowerCase ().replace ('_', ' ');
            VALUES[i] = name.toLowerCase ().replace ('_', '-');
        }
    }


    /**
     * Get all names for display.
     *
     * @return The formatted names
     */
    public static String [] getNames ()
    {
        return NAMES;
    }


    /**
     * Get the name of a mode value.
     *
     * @param mode The mode value
     * @return The formatted name
     */
    public static String getName (final String mode)
    {
        return NAMES[NoteRepeatModes.valueOf (mode.toUpperCase ().replace ('-', '_')).ordinal ()];
    }


    /**
     * Get the index of a mode value.
     *
     * @param mode The mode value
     * @return The index
     */
    public static int getIndex (final String mode)
    {
        return NoteRepeatModes.valueOf (mode.toUpperCase ().replace ('-', '_')).ordinal ();
    }


    /**
     * Increase or descrease the index of a note repeat mode and keeps it in the bounds of the
     * existing range.
     *
     * @param index An index of a note repeat mode
     * @param inc Increase if true otherwise descrease
     * @return The index of the previous or next note repeat mode
     */
    public static int change (final int index, final boolean inc)
    {
        return Math.max (0, Math.min (values ().length - 1, index + (inc ? 1 : -1)));
    }


    /**
     * Get the value of the note repeat mode.
     *
     * @param index The index of the note repeat enum
     * @return The value
     */
    public static String getValueAt (final int index)
    {
        return VALUES[index];
    }
}
