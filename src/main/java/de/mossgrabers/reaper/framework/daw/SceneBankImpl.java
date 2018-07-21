// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2018
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.framework.daw;

import de.mossgrabers.framework.daw.ISceneBank;
import de.mossgrabers.framework.daw.data.IScene;


/**
 * Encapsulates the data of a scene bank.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class SceneBankImpl extends AbstractBankImpl<IScene> implements ISceneBank
{
    /**
     * Constructor.
     *
     * @param numScenes The number of scenes in the page of the bank
     */
    public SceneBankImpl (final int numScenes)
    {
        super (null, null, null, numScenes);
        this.initItems ();
    }


    /** {@inheritDoc} */
    @Override
    public void stop ()
    {
        // Intentionally empty
    }


    /** {@inheritDoc} */
    @Override
    protected void initItems ()
    {
        // Intentionally empty
    }


    /** {@inheritDoc} */
    @Override
    public void scrollPageBackwards ()
    {
        // Intentionally empty
    }


    /** {@inheritDoc} */
    @Override
    public void scrollPageForwards ()
    {
        // Intentionally empty
    }
}