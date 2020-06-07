// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2020
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.controller.fire.view;

import de.mossgrabers.controller.fire.FireConfiguration;
import de.mossgrabers.controller.fire.controller.FireControlSurface;
import de.mossgrabers.framework.controller.ButtonID;
import de.mossgrabers.framework.daw.ICursorDevice;
import de.mossgrabers.framework.daw.IModel;
import de.mossgrabers.framework.daw.data.IDrumPad;
import de.mossgrabers.framework.utils.ButtonEvent;
import de.mossgrabers.framework.view.AbstractDrumView;


/**
 * The drum sequencer.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class DrumView extends AbstractDrumView<FireControlSurface, FireConfiguration> implements IFireView
{
    /**
     * Constructor.
     *
     * @param surface The surface
     * @param model The model
     */
    public DrumView (final FireControlSurface surface, final IModel model)
    {
        super ("Drum", surface, model, 4, 4);
    }


    /** {@inheritDoc} */
    @Override
    public int getButtonColor (final ButtonID buttonID)
    {
        switch (buttonID)
        {
            case SCENE1:
            case SCENE2:
            case SCENE3:
            case SCENE4:
                final int scene = buttonID.ordinal () - ButtonID.SCENE1.ordinal ();
                final ICursorDevice instrumentDevice = this.model.getInstrumentDevice ();
                if (instrumentDevice.hasDrumPads () && instrumentDevice.getDrumPadBank ().getItem (scene).doesExist ())
                    return this.surface.isPressed (buttonID) ? 2 : 1;
                return 0;

            default:
                return super.getButtonColor (buttonID);
        }
    }


    /** {@inheritDoc} */
    @Override
    public int getSoloButtonColor (final int index)
    {
        if (!this.isActive ())
            return 0;

        final int pos = 3 - index;
        final ICursorDevice instrumentDevice = this.model.getInstrumentDevice ();
        if (instrumentDevice.hasDrumPads ())
        {
            final IDrumPad item = instrumentDevice.getDrumPadBank ().getItem (pos);
            if (this.surface.isPressed (ButtonID.ALT))
                return item.isSolo () ? 4 : 0;
            return item.isMute () ? 3 : 0;
        }

        return 0;
    }


    /** {@inheritDoc} */
    @Override
    public void onButton (final ButtonID buttonID, final ButtonEvent event, final int velocity)
    {
        if (event != ButtonEvent.DOWN || !this.isActive ())
            return;

        if (buttonID == ButtonID.ARROW_LEFT)
        {
            if (this.surface.isPressed (ButtonID.ALT))
                this.setResolutionIndex (this.selectedResolutionIndex - 1);
            else
                this.getClip ().scrollStepsPageBackwards ();
            return;
        }

        if (buttonID == ButtonID.ARROW_RIGHT)
        {
            if (this.surface.isPressed (ButtonID.ALT))
                this.setResolutionIndex (this.selectedResolutionIndex + 1);
            else
                this.getClip ().scrollStepsPageForward ();
            return;
        }

        if (!ButtonID.isSceneButton (buttonID))
            return;
        final int index = 3 - (buttonID.ordinal () - ButtonID.SCENE1.ordinal ());
        final ICursorDevice instrumentDevice = this.model.getInstrumentDevice ();
        if (instrumentDevice.hasDrumPads ())
        {
            final IDrumPad item = instrumentDevice.getDrumPadBank ().getItem (index);
            if (this.surface.isPressed (ButtonID.ALT))
                item.toggleSolo ();
            else
                item.toggleMute ();
        }
    }


    /** {@inheritDoc} */
    @Override
    public void onSelectKnobValue (final int value)
    {
        this.changeOctave (ButtonEvent.DOWN, this.model.getValueChanger ().calcKnobSpeed (value) > 0, 4);
    }
}