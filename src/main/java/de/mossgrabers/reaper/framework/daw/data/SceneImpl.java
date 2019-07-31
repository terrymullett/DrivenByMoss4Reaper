// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.framework.daw.data;

import de.mossgrabers.framework.daw.data.IScene;
import de.mossgrabers.reaper.framework.daw.DataSetup;


/**
 * Encapsulates the data of a scene.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class SceneImpl extends ItemImpl implements IScene
{
    private double red   = 0;
    private double green = 0;
    private double blue  = 0;


    /**
     * Constructor.
     *
     * @param dataSetup Some configuration variables
     * @param index The index of the slot
     */
    public SceneImpl (final DataSetup dataSetup, final int index)
    {
        super (dataSetup, index);
        this.setExists (false);
    }


    /** {@inheritDoc} */
    @Override
    public double [] getColor ()
    {
        return new double []
        {
            this.red,
            this.green,
            this.blue
        };
    }


    /** {@inheritDoc} */
    @Override
    public void setColor (final double red, final double green, final double blue)
    {
        this.red = red;
        this.green = green;
        this.blue = blue;
    }


    /** {@inheritDoc} */
    @Override
    public void launch ()
    {
        this.sendSceneOSC ("launch");
    }


    /** {@inheritDoc} */
    @Override
    public void remove ()
    {
        this.sendSceneOSC ("remove");
    }


    /** {@inheritDoc} */
    @Override
    public void select ()
    {
        this.sendSceneOSC ("select");
    }


    /** {@inheritDoc} */
    @Override
    public void duplicate ()
    {
        // Not supported
    }


    protected void sendSceneOSC (final String command)
    {
        this.sender.processNoArg ("scene", this.getPosition () + "/" + command);
    }
}
