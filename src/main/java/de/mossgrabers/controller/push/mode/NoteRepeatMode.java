// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.push.mode;

import de.mossgrabers.controller.push.controller.PushControlSurface;
import de.mossgrabers.controller.push.controller.PushDisplay;
import de.mossgrabers.framework.Resolution;
import de.mossgrabers.framework.controller.display.Display;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.data.ITrack;
import de.mossgrabers.framework.daw.midi.INoteInput;
import de.mossgrabers.framework.daw.midi.INoteRepeat;
import de.mossgrabers.framework.graphics.display.DisplayModel;
import de.mossgrabers.framework.mode.AbstractMode;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.framework.utils.Pair;


/**
 * Editing the length of note repeat notes.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NoteRepeatMode extends BaseMode
{
    private final INoteRepeat noteRepeat;


    /**
     * Constructor.
     *
     * @param surface The control surface
     * @param model The model
     */
    public NoteRepeatMode (final PushControlSurface surface, final IModel model)
    {
        super ("Note Repeat", surface, model);

        this.isTemporary = true;

        final INoteInput defaultNoteInput = surface.getInput ().getDefaultNoteInput ();
        this.noteRepeat = defaultNoteInput == null ? null : defaultNoteInput.getNoteRepeat ();
    }


    /** {@inheritDoc} */
    @Override
    public void onKnobValue (final int index, final int value)
    {
        if (!this.increaseKnobMovement ())
            return;

        final ITrack selectedTrack = this.model.getCurrentTrackBank ().getSelectedItem ();
        if (index < 2)
        {
            final int sel = Resolution.change (Resolution.getMatch (this.noteRepeat.getPeriod (selectedTrack)), this.model.getValueChanger ().calcKnobSpeed (value) > 0);
            this.noteRepeat.setPeriod (selectedTrack, Resolution.getValueAt (sel));
        }
        else if (index < 4)
        {
            final int sel = Resolution.change (Resolution.getMatch (this.noteRepeat.getNoteLength (selectedTrack)), this.model.getValueChanger ().calcKnobSpeed (value) > 0);
            this.noteRepeat.setNoteLength (selectedTrack, Resolution.getValueAt (sel));
        }
    }


    /** {@inheritDoc} */
    @Override
    public void onFirstRow (final int index, final ButtonEvent event)
    {
        if (event != ButtonEvent.UP || this.noteRepeat == null)
            return;

        final ITrack selectedTrack = this.model.getCurrentTrackBank ().getSelectedItem ();
        if (index < 2)
        {
            final int sel = Resolution.change (Resolution.getMatch (this.noteRepeat.getPeriod (selectedTrack)), index == 1);
            this.noteRepeat.setPeriod (selectedTrack, Resolution.getValueAt (sel));
        }
        else if (index < 4)
        {
            final int sel = Resolution.change (Resolution.getMatch (this.noteRepeat.getNoteLength (selectedTrack)), index == 3);
            this.noteRepeat.setNoteLength (selectedTrack, Resolution.getValueAt (sel));
        }
        else if (index == 7)
            this.noteRepeat.toggleShuffle (selectedTrack);
    }


    /** {@inheritDoc} */
    @Override
    public void updateFirstRow ()
    {
        this.surface.updateTrigger (20, AbstractMode.BUTTON_COLOR_ON);
        this.surface.updateTrigger (21, AbstractMode.BUTTON_COLOR_ON);
        this.surface.updateTrigger (22, AbstractMode.BUTTON_COLOR_ON);
        this.surface.updateTrigger (23, AbstractMode.BUTTON_COLOR_ON);

        this.surface.updateTrigger (24, AbstractMode.BUTTON_COLOR_OFF);
        this.surface.updateTrigger (25, AbstractMode.BUTTON_COLOR_OFF);
        this.surface.updateTrigger (26, AbstractMode.BUTTON_COLOR_OFF);
        this.surface.updateTrigger (27, this.noteRepeat != null && this.noteRepeat.isShuffle (this.model.getCurrentTrackBank ().getSelectedItem ()) ? AbstractMode.BUTTON_COLOR_HI : AbstractMode.BUTTON_COLOR_ON);
    }


    /** {@inheritDoc} */
    @Override
    public void updateDisplay1 ()
    {
        final Display d = this.surface.getDisplay ().clear ();

        final ITrack selectedTrack = this.model.getCurrentTrackBank ().getSelectedItem ();

        d.setCell (0, 0, "Period:");
        final int selPeriodIndex = getSelectedPeriodIndex (selectedTrack);
        int pos = 0;
        for (final Pair<String, Boolean> p: PushDisplay.createMenuList (4, Resolution.getNames (), selPeriodIndex))
        {
            d.setCell (pos, 1, (p.getValue ().booleanValue () ? PushDisplay.SELECT_ARROW : " ") + p.getKey ());
            pos++;
        }

        d.setCell (0, 2, "Length:");
        final int selLengthIndex = getSelectedNoteLengthIndex (selectedTrack);
        pos = 0;
        for (final Pair<String, Boolean> p: PushDisplay.createMenuList (4, Resolution.getNames (), selLengthIndex))
        {
            d.setCell (pos, 3, (p.getValue ().booleanValue () ? PushDisplay.SELECT_ARROW : " ") + p.getKey ());
            pos++;
        }

        d.setCell (3, 7, "Shuffle");

        d.allDone ();
    }


    /** {@inheritDoc} */
    @Override
    public void updateDisplay2 ()
    {
        final DisplayModel message = this.surface.getDisplay ().getModel ();
        final ITrack selectedTrack = this.model.getCurrentTrackBank ().getSelectedItem ();

        message.addOptionElement ("Period", "", false, "", "", false, false);
        final int selPeriodIndex = getSelectedPeriodIndex (selectedTrack);
        message.addListElement (6, Resolution.getNames (), selPeriodIndex);

        message.addOptionElement ("  Length", "", false, "", "", false, false);
        final int selLengthIndex = getSelectedNoteLengthIndex (selectedTrack);
        message.addListElement (6, Resolution.getNames (), selLengthIndex);

        message.addEmptyElement ();
        message.addEmptyElement ();
        message.addEmptyElement ();

        message.addOptionElement ("", "", false, "", "Shuffle", this.noteRepeat != null && this.noteRepeat.isShuffle (selectedTrack), false);

        message.send ();
    }


    /**
     * Get the index of the selected period.
     *
     * @param selectedTrack The currently selected track
     * @return The selected period index
     */
    private int getSelectedPeriodIndex (final ITrack selectedTrack)
    {
        return this.noteRepeat == null ? -1 : Resolution.getMatch (this.noteRepeat.getPeriod (selectedTrack));
    }


    /**
     * Get the index of the selected length.
     *
     * @param selectedTrack The currently selected track
     * @return The selected lenth index
     */
    private int getSelectedNoteLengthIndex (final ITrack selectedTrack)
    {
        return this.noteRepeat == null ? -1 : Resolution.getMatch (this.noteRepeat.getNoteLength (selectedTrack));
    }
}
