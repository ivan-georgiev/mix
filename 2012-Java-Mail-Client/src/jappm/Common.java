/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package jappm;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 *
 * @author Ivan
 */
public class Common {

    public Common() {
    }

    public static void sleep(int ms) {
        try {
            Thread.sleep(ms); // sleep for time miliseconds
        } catch (InterruptedException exs) {
        }

    }

    public static String stackTraceToString(Throwable e) {
        String retValue = null;
        StringWriter sw = null;
        PrintWriter pw = null;
        try {
            sw = new StringWriter();
            pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            retValue = sw.toString();
        } finally {
            try {
                if (pw != null) {
                    pw.close();
                }
                if (sw != null) {
                    sw.close();
                }
            } catch (IOException ignore) {
            }
        }

        if (retValue.length() > 210) {

            retValue = retValue.substring(0, 200);

            int lastIndexOf = retValue.lastIndexOf(" ");

            if (lastIndexOf != -1) {
                retValue = retValue.substring(0, lastIndexOf);
            }

        }


        return retValue;
    }

    public static ImageIcon getImageAsIconWithSize(String filePath, int x, int y) {

        ImageIcon icon = new ImageIcon();
        BufferedImage img = null;

        try {
            img = ImageIO.read(new File(filePath));
            icon = new ImageIcon(img.getScaledInstance(x, y, java.awt.Image.SCALE_SMOOTH));
        } catch (IOException ex) {
            Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, ex);
        }

        return icon;

    }

    public static long[] convertArrayListLongToPrimitive(ArrayList<Long> arrayList) {

        if (arrayList == null || arrayList.isEmpty()) {
            return new long[]{};
        }

        long[] result = new long[arrayList.size()];

        int i = 0;
        for (Object value : arrayList) {
            if (value == null) {
                result[i] = 0;
            } else {
                result[i] = ((Long)value).longValue();
            }
            i++;
        }

        return result;

    }
}
