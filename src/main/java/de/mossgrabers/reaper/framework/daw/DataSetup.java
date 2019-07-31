// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.framework.daw;

import de.mossgrabers.framework.controller.IValueChanger;
import de.mossgrabers.framework.controller.color.ColorManager;
import de.mossgrabers.framework.daw.IHost;
import de.mossgrabers.framework.daw.ITransport;
import de.mossgrabers.reaper.communication.MessageSender;


/**
 * Configuration parameters for DAW objects.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class DataSetup
{
    private final IHost         host;
    private final IValueChanger valueChanger;
    private final ColorManager  colorManager;
    private final MessageSender sender;

    private ITransport          transport;


    /**
     * Constructor.
     *
     * @param host The DAW host
     * @param valueChanger The value changer
     * @param colorManager The color manager
     * @param sender The OSC sender
     */
    public DataSetup (final IHost host, final IValueChanger valueChanger, final ColorManager colorManager, final MessageSender sender)
    {
        this.host = host;
        this.valueChanger = valueChanger;
        this.colorManager = colorManager;
        this.sender = sender;
    }


    /**
     * Get the DAW host.
     *
     * @return The host
     */
    public IHost getHost ()
    {
        return this.host;
    }


    /**
     * Get the value changer.
     *
     * @return The value changer
     */
    public IValueChanger getValueChanger ()
    {
        return this.valueChanger;
    }


    /**
     * Get the color manager.
     *
     * @return The color manager
     */
    public ColorManager getColorManager ()
    {
        return this.colorManager;
    }


    /**
     * Get the sender.
     *
     * @return The sender
     */
    public MessageSender getSender ()
    {
        return this.sender;
    }


    /**
     * Set the transport.
     *
     * @param transport The transport
     */
    public void setTransport (final ITransport transport)
    {
        this.transport = transport;
    }


    /**
     * Get the transport.
     *
     * @return The transport
     */
    public ITransport getTransport ()
    {
        return this.transport;
    }
}
