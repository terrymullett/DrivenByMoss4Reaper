// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2019
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.controller.autocolor;

import de.mossgrabers.controller.autocolor.AutoColorDefinition;
import de.mossgrabers.controller.autocolor.AutoColorSetup;
import de.mossgrabers.framework.controller.IControllerSetup;
import de.mossgrabers.reaper.communication.MessageSender;
import de.mossgrabers.reaper.controller.AbstractControllerInstance;
import de.mossgrabers.reaper.framework.IniFiles;
import de.mossgrabers.reaper.framework.ReaperSetupFactory;
import de.mossgrabers.reaper.ui.WindowManager;
import de.mossgrabers.reaper.ui.utils.LogModel;


/**
 * The Auto Color instance.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class AutoColorInstance extends AbstractControllerInstance
{
    /**
     * Constructor.
     *
     * @param logModel The logging model
     * @param windowManager The window manager for the configuration dialog
     * @param sender The sender
     * @param iniFiles The INI configuration files
     */
    public AutoColorInstance (final LogModel logModel, final WindowManager windowManager, final MessageSender sender, final IniFiles iniFiles)
    {
        super (new AutoColorDefinition (), logModel, windowManager, sender, iniFiles);
    }


    /** {@inheritDoc} */
    @Override
    protected IControllerSetup<?, ?> createControllerSetup (final ReaperSetupFactory setupFactory)
    {
        return new AutoColorSetup (this.host, setupFactory, this.settingsUI, this.settingsUI);
    }
}
