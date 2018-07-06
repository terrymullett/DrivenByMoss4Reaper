package de.mossgrabers.reaper.framework.daw;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Frame;
import java.awt.Label;
import java.awt.Toolkit;
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
    /** Display the message for about 4 seconds. */
    private static final int               TIMEOUT         = 4;
    private static final int               FRAME_WIDTH     = 600;
    private static final int               FRAME_HEIGHT    = 100;
    private static final int               MIN_FONT_HEIGHT = 10;
    private static final int               MAX_FONT_HEIGHT = 60;

    private final Frame                    frame           = new Frame ();
    private final Label                    label           = new Label ();
    private final AtomicInteger            counter         = new AtomicInteger ();
    private final ScheduledExecutorService executor        = Executors.newSingleThreadScheduledExecutor ();


    /**
     * Constructor. Starts the count down timer.
     */
    public NotificationWindow ()
    {
        this.frame.add (this.label, BorderLayout.CENTER);
        this.frame.setUndecorated (true);
        this.label.setAlignment (Label.CENTER);

        this.executor.scheduleAtFixedRate ( () -> {
            if (this.counter.get () > 0 && this.counter.decrementAndGet () == 0)
                this.frame.setVisible (false);
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
        this.fitFontSize ();
        if (!this.frame.isVisible ())
        {
            this.centerFrame ();
            this.frame.setVisible (true);
        }
    }


    /**
     * Center the notification window on the screen.
     */
    private void centerFrame ()
    {
        this.frame.setSize (FRAME_WIDTH, FRAME_HEIGHT);
        final Dimension dim = Toolkit.getDefaultToolkit ().getScreenSize ();
        final int x = (dim.width - FRAME_WIDTH) / 2;
        final int y = (dim.height - FRAME_HEIGHT) / 2;
        this.frame.setLocation (x, y);
    }


    /**
     * Fit the font size to the current label text.
     */
    private void fitFontSize ()
    {
        final String text = this.label.getText ();
        Font font = this.label.getFont ();
        for (int size = MAX_FONT_HEIGHT; size > MIN_FONT_HEIGHT; size -= 2)
        {
            font = font.deriveFont (font.getStyle (), size);
            final FontMetrics fm = this.label.getFontMetrics (font);
            if (fm.getHeight () <= FRAME_HEIGHT && fm.stringWidth (text) <= FRAME_WIDTH)
                break;
        }
        this.label.setFont (font);
    }
}
