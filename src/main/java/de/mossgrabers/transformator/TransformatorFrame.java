package de.mossgrabers.transformator;

import javafx.application.Platform;
import javafx.embed.swing.JFXPanel;
import javafx.scene.Scene;
import javafx.scene.control.Button;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import java.awt.Panel;


public class TransformatorFrame extends JFrame
{
    private JLabel swingLabel;
    private String iniPath;


    public TransformatorFrame (String iniPath)
    {
        // Setup Swing-Fenster, -Button und -Label
        super ("Swing Frame");

        this.iniPath = iniPath;

        TransformatorApplication app = new TransformatorApplication ();
        JPanel panel = app.start (this, this.iniPath);

        this.add (panel);

        SwingUtilities.invokeLater (this::showSwingWindow);
    }


    private void showSwingWindow ()
    {
        pack ();
        setVisible (true);
    }
}