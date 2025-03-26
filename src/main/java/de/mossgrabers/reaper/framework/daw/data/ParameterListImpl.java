// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2025
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.framework.daw.data;

import de.mossgrabers.framework.controller.valuechanger.IValueChanger;
import de.mossgrabers.framework.daw.IHost;
import de.mossgrabers.framework.daw.data.IParameterList;
import de.mossgrabers.framework.parameter.IParameter;

import java.util.ArrayList;
import java.util.List;


/**
 * Implementation of a parameter list.
 *
 * @author Jürgen Moßgraber
 */
public class ParameterListImpl implements IParameterList
{
    private final int              maxNumberOfParameters;
    private final List<IParameter> parameters;
    private final IHost            host;


    /**
     * Constructor.
     * 
     * @param numMonitoredPages The number of pages t monitor
     * @param host The controller host
     * @param valueChanger The value changer
     */
    public ParameterListImpl (final int numMonitoredPages, final IHost host, final IValueChanger valueChanger)
    {
        final int numParams = 8;

        this.maxNumberOfParameters = numMonitoredPages * numParams;
        this.host = host;
        this.parameters = new ArrayList<> (this.maxNumberOfParameters);

        // TODO
        // for (int i = 0; i < numMonitoredPages; i++)
        // {
        // final int page = i;
        // final CursorRemoteControlsPage remoteControls = device.createCursorRemoteControlsPage
        // ("Page " + page, numParams, "");
        // remoteControls.pageCount ().addValueObserver (newValue -> reAdjust (remoteControls,
        // page), -1);
        // remoteControls.selectedPageIndex ().addValueObserver (newValue -> reAdjust
        // (remoteControls, page), -1);
        // for (int p = 0; p < numParams; p++)
        // this.parameters.add (new ParameterImpl (valueChanger, remoteControls.getParameter (p)));
        // }
    }


    /** {@inheritDoc} */
    @Override
    public int getMaxNumberOfParameters ()
    {
        return this.maxNumberOfParameters;
    }


    /** {@inheritDoc} */
    @Override
    public List<IParameter> getParameters ()
    {
        return this.parameters;
    }
}
