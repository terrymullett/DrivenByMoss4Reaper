// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.framework.daw.data;

import de.mossgrabers.framework.controller.IValueChanger;
import de.mossgrabers.framework.daw.IHost;
import de.mossgrabers.framework.daw.data.IMasterTrack;
import de.mossgrabers.framework.daw.resource.ChannelType;
import de.mossgrabers.framework.observer.ItemSelectionObserver;
import de.mossgrabers.reaper.communication.MessageSender;
import de.mossgrabers.reaper.framework.daw.TrackBankImpl;

import java.util.ArrayList;
import java.util.List;


/**
 * The master track.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class MasterTrackImpl extends TrackImpl implements IMasterTrack
{
    private final List<ItemSelectionObserver> observers = new ArrayList<> ();


    /**
     * Constructor.
     *
     * @param host The DAW host
     * @param trackBank The trackbank for calculating the index
     * @param sender The OSC sender
     * @param valueChanger The valueChanger
     * @param numSends The number of sends on a page
     */
    public MasterTrackImpl (final IHost host, final TrackBankImpl trackBank, final MessageSender sender, final IValueChanger valueChanger, final int numSends)
    {
        super (host, sender, trackBank, valueChanger, 0, 1, numSends, 0);

        // Master channel does always exist
        this.setExists (true);
        this.valueChanger = valueChanger;

        this.setName ("Master");
        this.setType (ChannelType.MASTER);
    }


    /** {@inheritDoc} */
    @Override
    public int getIndex ()
    {
        return (this.trackBank.getItemCount () - 1) % this.trackBank.getPageSize ();
    }


    /** {@inheritDoc} */
    @Override
    public void enter ()
    {
        // Master track is no group
    }


    /** {@inheritDoc} */
    @Override
    public void addSelectionObserver (final ItemSelectionObserver observer)
    {
        this.observers.add (observer);
    }


    /** {@inheritDoc} */
    @Override
    public void setSelected (final boolean isSelected)
    {
        super.setSelected (isSelected);
        for (final ItemSelectionObserver observer: this.observers)
            observer.call (-1, isSelected);
    }


    /** {@inheritDoc} */
    @Override
    public void setIsActivated (final boolean value)
    {
        // Not supported
    }


    /** {@inheritDoc} */
    @Override
    public void toggleIsActivated ()
    {
        // Not supported
    }


    /** {@inheritDoc} */
    @Override
    public void setRecArm (final boolean value)
    {
        // Not supported
    }


    /** {@inheritDoc} */
    @Override
    public void setMonitor (final boolean value)
    {
        // Not supported
    }


    /** {@inheritDoc} */
    @Override
    public void toggleMonitor ()
    {
        // Not supported
    }


    /** {@inheritDoc} */
    @Override
    public void setAutoMonitor (final boolean value)
    {
        // Not supported
    }


    /** {@inheritDoc} */
    @Override
    public void toggleAutoMonitor ()
    {
        // Not supported
    }


    /** {@inheritDoc} */
    @Override
    protected String getProcessor ()
    {
        return "master";
    }


    /** {@inheritDoc} */
    @Override
    protected String createCommand (final String command)
    {
        return command;
    }
}