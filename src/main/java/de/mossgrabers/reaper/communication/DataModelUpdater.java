// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2018
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.communication;

/**
 * Interface for updating the data model.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public interface DataModelUpdater
{
    /**
     * Update the data model.
     *
     * @param dump True to dump the whole model otherwise only changes
     */
    void updateDataModel (boolean dump);
}
