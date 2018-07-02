// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2018
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.transformator.communication;

import java.util.concurrent.atomic.AtomicBoolean;


/**
 * Runs a loop for updating the data model from Reaper.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class DataModelUpdateExecutor
{
    /** Update interval in milliseconds. */
    private static final int       UPDATE_INTERVAL   = 500;

    private final AtomicBoolean    stopUpdaterSignal = new AtomicBoolean (false);
    private final AtomicBoolean    dumpSignal        = new AtomicBoolean (false);
    private final DataModelUpdater updater;


    /**
     * Constructor.
     * 
     * @param updater The updater
     */
    public DataModelUpdateExecutor (final DataModelUpdater updater)
    {
        this.updater = updater;
    }


    /**
     * Start the thread who updates the data model.
     */
    public void execute ()
    {
        this.stopUpdaterSignal.set (false);

        new Thread ( () -> {
            while (true)
            {
                this.updater.updateDataModel (this.dumpSignal.get ());
                this.dumpSignal.set (false);

                if (this.stopUpdaterSignal.get ())
                    break;

                try
                {
                    Thread.sleep (UPDATE_INTERVAL);
                }
                catch (final InterruptedException ex)
                {
                    continue;
                }
            }
        }).start ();
    }


    /**
     * Update all data.
     */
    public void executeDump ()
    {
        this.dumpSignal.set (true);
    }


    /**
     * Stop the updater thread.
     */
    public void stopUpdater ()
    {
        this.stopUpdaterSignal.set (true);
    }
}
