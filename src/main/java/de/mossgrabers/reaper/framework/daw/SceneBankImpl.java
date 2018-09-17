// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2018
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.framework.daw;

import de.mossgrabers.framework.daw.IHost;
import de.mossgrabers.framework.daw.ISceneBank;
import de.mossgrabers.framework.daw.data.IScene;
import de.mossgrabers.reaper.communication.MessageSender;
import de.mossgrabers.reaper.framework.daw.data.SceneImpl;


/**
 * Encapsulates the data of a scene bank.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class SceneBankImpl extends AbstractBankImpl<IScene> implements ISceneBank
{
    protected final IScene emptyScene;
    protected int          bankOffset = 0;


    /**
     * Constructor.
     *
     * @param host The DAW host
     * @param sender The OSC sender
     * @param numScenes The number of scenes in the page of the bank
     */
    public SceneBankImpl (final IHost host, final MessageSender sender, final int numScenes)
    {
        super (host, sender, null, numScenes);
        this.initItems ();

        this.emptyScene = new SceneImpl (host, sender, -1);
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
        // Items are added on the fly in getItem
    }


    /** {@inheritDoc} */
    @Override
    public boolean canScrollBackwards ()
    {
        return this.bankOffset - this.pageSize >= 0;
    }


    /** {@inheritDoc} */
    @Override
    public boolean canScrollForwards ()
    {
        return this.bankOffset + this.pageSize < this.getItemCount ();
    }


    /** {@inheritDoc} */
    @Override
    public void scrollPageBackwards ()
    {
        this.bankOffset = Math.max (0, this.bankOffset - this.pageSize);
    }


    /** {@inheritDoc} */
    @Override
    public void scrollPageForwards ()
    {
        if (this.bankOffset + this.pageSize < this.getItemCount ())
            this.bankOffset += this.pageSize;
    }


    /** {@inheritDoc} */
    @Override
    public IScene getItem (final int index)
    {
        final int id = this.bankOffset + index;
        return id >= 0 && id < this.getItemCount () ? this.getScene (id) : this.emptyScene;
    }


    /**
     * Get a scene from the scene list. No paging is applied.
     *
     * @param position The position of the scene
     * @return The scene
     */
    public SceneImpl getScene (final int position)
    {
        synchronized (this.items)
        {
            final int size = this.items.size ();
            final int diff = position - size + 1;
            if (diff > 0)
            {
                for (int i = 0; i < diff; i++)
                    this.items.add (new SceneImpl (this.host, this.sender, this.pageSize == 0 ? 0 : (size + i) % this.pageSize));
            }
            return (SceneImpl) this.items.get (position);
        }
    }


    /**
     * Sets the number of scenes.
     *
     * @param sceneCount The number of scenes
     */
    public void setSceneCount (final int sceneCount)
    {
        this.itemCount = sceneCount;
    }
}