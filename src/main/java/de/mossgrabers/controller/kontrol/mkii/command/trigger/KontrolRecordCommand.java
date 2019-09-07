// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.kontrol.mkii.command.trigger;

import de.mossgrabers.controller.kontrol.mkii.KontrolMkIIConfiguration;
import de.mossgrabers.controller.kontrol.mkii.controller.KontrolMkIIControlSurface;
import de.mossgrabers.framework.command.core.AbstractTriggerCommand;
import de.mossgrabers.framework.command.trigger.clip.NewCommand;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.data.ISlot;
import de.mossgrabers.framework.daw.data.ITrack;
import de.mossgrabers.framework.utils.ButtonEvent;


/**
 * Command handle the record and restart (Shift+Record) button.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class KontrolRecordCommand extends AbstractTriggerCommand<KontrolMkIIControlSurface, KontrolMkIIConfiguration>
{
    private boolean isRecordButton;


    /**
     * Constructor.
     *
     * @param isRecordButton True for the record button otherwise the restart button
     * @param model The model
     * @param surface The surface
     */
    public KontrolRecordCommand (final boolean isRecordButton, final IModel model, final KontrolMkIIControlSurface surface)
    {
        super (model, surface);
        this.isRecordButton = isRecordButton;
    }


    /** {@inheritDoc} */
    @Override
    public void executeNormal (final ButtonEvent event)
    {
        if (event != ButtonEvent.DOWN)
            return;

        final KontrolMkIIConfiguration configuration = this.surface.getConfiguration ();
        final KontrolMkIIConfiguration.RecordFunction recordMode = this.isRecordButton ? configuration.getRecordButtonFunction () : configuration.getShiftedRecordButtonFunction ();
        final ITrack track = this.model.getCurrentTrackBank ().getSelectedItem ();
        switch (recordMode)
        {
            case RECORD_ARRANGER:
                this.model.getTransport ().record ();
                break;
            case RECORD_CLIP:
                final ISlot slot = this.model.getSelectedSlot ();
                if (track != null && slot != null)
                    this.model.recordNoteClip (track, slot);
                break;
            case NEW_CLIP:
                new NewCommand<> (this.model, this.surface).executeNormal (ButtonEvent.DOWN);
                break;
            case TOGGLE_ARRANGER_OVERDUB:
                this.model.getTransport ().toggleOverdub ();
                break;
            case TOGGLE_CLIP_OVERDUB:
                this.model.getTransport ().toggleLauncherOverdub ();
                break;
            case TOGGLE_REC_ARM:
                if (track != null)
                    track.toggleRecArm ();
                break;
            default:
                // Intentionally empty
                break;
        }
    }
}
