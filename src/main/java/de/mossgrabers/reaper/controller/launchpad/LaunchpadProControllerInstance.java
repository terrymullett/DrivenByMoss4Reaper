// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.controller.launchpad;

import de.mossgrabers.controller.launchpad.LaunchpadControllerDefinition;
import de.mossgrabers.controller.launchpad.LaunchpadControllerSetup;
import de.mossgrabers.framework.controller.IControllerSetup;
import de.mossgrabers.reaper.communication.MessageSender;
import de.mossgrabers.reaper.controller.AbstractControllerInstance;
import de.mossgrabers.reaper.framework.IniFiles;
import de.mossgrabers.reaper.framework.ReaperSetupFactory;
import de.mossgrabers.reaper.ui.WindowManager;
import de.mossgrabers.reaper.ui.utils.LogModel;


/**
 * Launchpad Pro controller instance.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class LaunchpadProControllerInstance extends AbstractControllerInstance
{
    /**
     * Constructor.
     *
     * @param logModel The logging model
     * @param windowManager The window manager for the configuration dialog
     * @param sender The sender
     * @param iniFiles The INI configuration files
     */
    public LaunchpadProControllerInstance (final LogModel logModel, final WindowManager windowManager, final MessageSender sender, final IniFiles iniFiles)
    {
        super (new LaunchpadControllerDefinition (false), logModel, windowManager, sender, iniFiles);
    }


    /** {@inheritDoc} */
    @Override
    protected IControllerSetup<?, ?> createControllerSetup (final ReaperSetupFactory setupFactory)
    {
        return new LaunchpadControllerSetup (this.host, setupFactory, this.settingsUI, this.settingsUI, true);
    }
}
