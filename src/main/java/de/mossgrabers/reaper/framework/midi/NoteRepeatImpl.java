// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.framework.midi;

import de.mossgrabers.framework.daw.data.ITrack;
import de.mossgrabers.framework.daw.midi.INoteRepeat;
import de.mossgrabers.reaper.framework.daw.data.TrackImpl;


/**
 * Implementation for a note repeat.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NoteRepeatImpl implements INoteRepeat
{
    /**
     * Constructor.
     */
    public NoteRepeatImpl ()
    {
        // Intentionally empty
    }


    /** {@inheritDoc} */
    @Override
    public boolean isActive (final ITrack track)
    {
        return ((TrackImpl) track).isNoteRepeatActive ();
    }


    /** {@inheritDoc} */
    @Override
    public void toggleActive (final ITrack track)
    {
        ((TrackImpl) track).toggleNoteRepeat ();
    }


    /** {@inheritDoc} */
    @Override
    public void setPeriod (final ITrack track, final double length)
    {
        ((TrackImpl) track).setNoteRepeatPeriod (length);
    }


    /** {@inheritDoc} */
    @Override
    public double getPeriod (final ITrack track)
    {
        return ((TrackImpl) track).getNoteRepeatPeriod ();
    }
}
