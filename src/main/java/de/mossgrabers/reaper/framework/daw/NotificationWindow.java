package de.mossgrabers.reaper.framework.daw;

import de.mossgrabers.transformator.util.SafeRunLater;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;


/**
 * Can display a notification window for a certain amount of time.
 *
 * @author J&uuml;rgen Mo&szlig;graber
 */
public class NotificationWindow
{
    private static final int               TIMEOUT    = 2;

    private final AtomicInteger            counter    = new AtomicInteger ();
    private final ScheduledExecutorService executor   = Executors.newSingleThreadScheduledExecutor ();

    private final JFrame                   popupStage = new JFrame ();
    private final JLabel                   label      = new JLabel ("");


    /**
     * Constructor. Starts the count down timer.
     */
    public NotificationWindow ()
    {
        this.popupStage.setTitle ("Title of popup");
        this.popupStage.setAlwaysOnTop (true);
        this.popupStage.setUndecorated (true);
        this.popupStage.setResizable (false);

        final JPanel root = new JPanel (new BorderLayout ());
        root.add (this.label, BorderLayout.CENTER);

        this.popupStage.setContentPane (root);
        // this.popupStage.setMinimumSize (new Dimension (FRAME_WIDTH, FRAME_HEIGHT));

        this.popupStage.addComponentListener (new FontResizeAdapter ());

        // this.popupStage.centerOnScreen ();

        this.executor.scheduleAtFixedRate ( () -> {
            final int c = this.counter.get ();
            if (c <= 0)
                return;
            if (this.counter.decrementAndGet () == 0)
            {
                // Needs to be run on the Swing tread
                SafeRunLater.execute ( () -> {
                    this.popupStage.setVisible (false);
                });
            }
        }, 1, 1, TimeUnit.SECONDS);
    }


    /**
     * Shutdown the count down process.
     */
    public void shutdown ()
    {
        this.executor.shutdown ();
    }


    /**
     * Displays the notification window and resets the count down to hide it.
     *
     * @param message The message to display
     */
    public void displayMessage (final String message)
    {
        this.counter.set (TIMEOUT);

        this.label.setText (message);

        if (!this.popupStage.isShowing ())
        {
            final Dimension dim = Toolkit.getDefaultToolkit ().getScreenSize ();
            final Dimension size = this.popupStage.getSize ();
            this.popupStage.setLocation (dim.width / 2 - size.width / 2, dim.height / 2 - size.height / 2);
            this.popupStage.setVisible (true);
        }
    }


    void updateLabel ()
    {
        float fittedFontSize = 1.0f;
        while (getFittedText (this.label, fittedFontSize).equals (this.label.getText ()))
            fittedFontSize += 1.0f;
        this.label.setFont (this.label.getFont ().deriveFont (fittedFontSize - 1.0f));
        this.label.revalidate ();
        this.label.repaint ();
    }


    private static String getFittedText (JLabel label, float fontSize)
    {
        Insets i = label.getInsets ();
        Rectangle viewRect = new Rectangle ();
        Rectangle textRect = new Rectangle ();
        Rectangle iconRect = new Rectangle ();
        viewRect.x = i.left;
        viewRect.y = i.top;
        viewRect.width = label.getWidth () - (i.right + viewRect.x);
        viewRect.height = label.getHeight () - (i.bottom + viewRect.y);
        textRect.x = textRect.y = textRect.width = textRect.height = 0;
        iconRect.x = iconRect.y = iconRect.width = iconRect.height = 0;

        return SwingUtilities.layoutCompoundLabel (label, label.getFontMetrics (label.getFont ().deriveFont (fontSize)), label.getText (), label.getIcon (), label.getVerticalAlignment (), label.getHorizontalAlignment (), label.getVerticalTextPosition (), label.getHorizontalTextPosition (), viewRect, textRect, iconRect, label.getIconTextGap ());
    }

    class FontResizeAdapter extends ComponentAdapter
    {
        @Override
        public void componentResized (ComponentEvent e)
        {
            updateLabel ();
        }
    }
}
