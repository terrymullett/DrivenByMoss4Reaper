package de.mossgrabers.reaper;

import de.mossgrabers.controller.kontrol.usb.mkii.controller.Kontrol2DisplayProtocol;
import de.mossgrabers.framework.usb.UsbException;
import de.mossgrabers.framework.utils.StringUtils;

import org.usb4java.Device;
import org.usb4java.DeviceDescriptor;
import org.usb4java.DeviceHandle;
import org.usb4java.DeviceList;
import org.usb4java.LibUsb;
import org.usb4java.LibUsbException;
import org.usb4java.Transfer;

import javax.imageio.ImageIO;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.Iterator;


public class TestKontrol2USB
{
    final static int interfaceID = 0x03;


    public static void main (String [] args)
    {
        int result;
        try
        {
            result = LibUsb.init (null);
            if (result != LibUsb.SUCCESS)
                throw new LibUsbException ("Unable to initialize libusb.", result);

            // Print LibUsb errors and warnings
            LibUsb.setDebug (null, LibUsb.LOG_LEVEL_WARNING);

        }
        catch (final LibUsbException ex)
        {
            ex.printStackTrace ();
            return;
        }

        final DeviceHandle handle;
        try
        {
            handle = openDeviceWithVidPid ((short) 0x17cc, (short) 0x1610);
            if (handle == null)
                return;
        }
        catch (final UsbException ex)
        {
            ex.printStackTrace ();
            return;
        }

        result = LibUsb.setAutoDetachKernelDriver (handle, true);
        if (result != LibUsb.SUCCESS && result != LibUsb.ERROR_NOT_SUPPORTED)
            new LibUsbException (result).printStackTrace ();

        result = LibUsb.claimInterface (handle, interfaceID);
        if (result != LibUsb.SUCCESS)
        {
            new LibUsbException (result).printStackTrace ();
            return;
        }

        sendImage (handle);

        result = LibUsb.releaseInterface (handle, interfaceID);
        if (result != LibUsb.SUCCESS)
            new LibUsbException (result).printStackTrace ();

        LibUsb.close (handle);
        LibUsb.exit (null);
    }


    private static void sendImage (DeviceHandle handle)
    {
        ///////////////////////////////
        // Display is 480 x 270

        int display = 0;
        int x = 0; // 448 = letztes
        int y = 0; // 256 = letztes
        int width = 64; // 32 = letztes
        int height = 64; // 16 = letztes

        ByteBuffer data = ByteBuffer.allocateDirect (width * height * 3);
        for (int i = 0; i < width * height; i++)
        {
            data.put ((byte) 255);
            data.put ((byte) 100);
            data.put ((byte) 0);
        }

        final ByteBuffer buffer = ByteBuffer.allocateDirect (9712);

        // Kontrol2DisplayProtocol.fill (buffer, data, 1, x, y, width, height);
        // final IntBuffer transfered = IntBuffer.allocate (1);
        // LibUsb.bulkTransfer (handle, (byte) 0x03, buffer, transfered, 0);

        for (int row = 0; row < 1; row++)
        {
            for (int col = 0; col < 1; col++)
            {
                data.rewind ();
                buffer.clear ();
                drawImage (handle, display, col * 64, row * 64, width, height, data, buffer);
            }
        }
    }


    private static void drawImage (DeviceHandle handle, int display, int x, int y, int width, int height, ByteBuffer data, ByteBuffer buffer)
    {
        long start = System.currentTimeMillis ();
        
        Kontrol2DisplayProtocol.encodeImage (buffer, data, display, x, y, width, height);

        System.out.println ("Encoding: " + (System.currentTimeMillis () - start));
        start = System.currentTimeMillis ();
        
        final IntBuffer transfered = IntBuffer.allocate (1);
        LibUsb.bulkTransfer (handle, (byte) 0x03, buffer, transfered, 0);
        
        System.out.println ("Transfer: " + (System.currentTimeMillis () - start));
    }


    private static DeviceHandle openDeviceWithVidPid (final short vendorId, final short productId) throws UsbException
    {
        final DeviceList list = new DeviceList ();
        int result = LibUsb.getDeviceList (null, list);
        if (result < LibUsb.SUCCESS)
            throw new UsbException ("Unable to get device list.", new LibUsbException (result));

        try
        {
            final Iterator<Device> iterator = list.iterator ();
            while (iterator.hasNext ())
            {
                final Device device = iterator.next ();
                final DeviceDescriptor descriptor = new DeviceDescriptor ();
                result = LibUsb.getDeviceDescriptor (device, descriptor);
                if (result != LibUsb.SUCCESS)
                {
                    new LibUsbException (result).printStackTrace ();
                    // Continue, maybe there is a working device
                    continue;
                }
                if (descriptor.idVendor () == vendorId && descriptor.idProduct () == productId)
                {
                    DeviceHandle handle = new DeviceHandle ();
                    try
                    {
                        result = LibUsb.open (device, handle);
                        if (result == LibUsb.SUCCESS)
                            return handle;
                        new LibUsbException (result).printStackTrace ();
                    }
                    catch (org.usb4java.LibUsbException ex)
                    {
                        ex.printStackTrace ();
                    }

                    // Continue, maybe there is a working device
                }
            }
        }
        finally
        {
            LibUsb.freeDeviceList (list, true);
        }

        return null;
    }
}
