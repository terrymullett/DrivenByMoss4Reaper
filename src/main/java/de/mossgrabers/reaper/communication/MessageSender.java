// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.communication;

/**
 * Send an OSC message to the DAW.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public interface MessageSender
{
    /**
     * Sends an OSC message.
     *
     * @param command The command to send
     * @param value The value to send, may be null
     */
    void sendOSC (final String command, final Object value);


    /**
     * Invokes the action for the given action identifier.
     *
     * @param id The action identifier, must not be null
     */
    void invokeAction (final int id);
}
