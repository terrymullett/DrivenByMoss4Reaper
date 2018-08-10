// Written by Jürgen Moßgraber - mossgrabers.de
// (c) 2017-2018
// Licensed under LGPLv3 - http://www.gnu.org/licenses/lgpl-3.0.txt

package de.mossgrabers.reaper.framework.configuration;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;


/**
 * A titled separator. This is a text followed by a horizontal separator line.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class TitledSeparator extends JPanel
{
    private static final long serialVersionUID = -4307059969818219806L;

    private final JLabel      label            = new JLabel ();
    private final JSeparator  separator        = new JSeparator (SwingConstants.HORIZONTAL);


    /**
     * Constructor.
     *
     * @param title The text to display as a title
     */
    public TitledSeparator (final String title)
    {
        super (new BorderLayout ());

        this.add (this.separator, BorderLayout.CENTER);
        this.add (this.label, BorderLayout.WEST);
        this.label.setText (title);
    }
}
