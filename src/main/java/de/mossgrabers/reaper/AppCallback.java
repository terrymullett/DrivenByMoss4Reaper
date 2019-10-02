// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper;

import de.mossgrabers.reaper.controller.IControllerInstance;


/**
 * Interface for callback from the user interface.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public interface AppCallback
{
    /**
     * Add a controller from a controller definition.
     *
     * @param definitionIndex The index of the controller definition
     * @return The created controller instance or null if an error occured
     */
    IControllerInstance addController (int definitionIndex);


    /**
     * Edit a controller.
     *
     * @param controllerIndex The index of the controller
     */
    void editController (int controllerIndex);


    /**
     * Remove a controller.
     *
     * @param controllerIndex The index of the controller
     */
    void removeController (int controllerIndex);


    /**
     * Dis-/enable a controller.
     *
     * @param controllerIndex The index of the controller
     */
    void toggleEnableController (int controllerIndex);


    /**
     * Request a full update of the data model.
     */
    void sendRefreshCommand ();


    /**
     * Clear the logging messages.
     */
    void clearLogMessage ();


    /**
     * Dis-/enable an update processor for performance improvements.
     *
     * @param processor The processor to The processor to dis-/enable
     * @param enable True to enable processor updates, false to disable
     */
    void enableUpdates (final String processor, final boolean enable);
}
