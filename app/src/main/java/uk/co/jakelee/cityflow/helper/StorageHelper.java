package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.EnumMap;
import java.util.Map;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.components.ZoomableViewGroup;
import uk.co.jakelee.cityflow.model.Puzzle;

public class StorageHelper {
    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public final static int WIDTH = 400;
    public final static int HEIGHT = 400;
    private static final int screenshotSize = 500;

    public static void fillWithQrDrawable(ImageView imageView, String text) {
        try {
            Bitmap bitmap = generateQrCode(text);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private static Bitmap generateQrCode(String str) throws WriterException {
        BitMatrix result;
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.MARGIN, 0);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, WIDTH, HEIGHT, hints);
        } catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int width = result.getWidth();
        int height = result.getHeight();
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            int offset = y * width;
            for (int x = 0; x < width; x++) {
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    public static void saveCustomPuzzleImage(Activity activity, int puzzleId, final float screenshotScale, boolean forceSave) {
        final ZoomableViewGroup tileContainer = (ZoomableViewGroup) activity.findViewById(R.id.tileContainer);
        String filename = "puzzle_" + puzzleId + ".png";
        boolean existsAlready = activity.getFileStreamPath(filename).exists();
        if (tileContainer == null || (!forceSave && existsAlready)) {
            return;
        }

        try {
            tileContainer.setBackgroundColor(Color.TRANSPARENT);
            tileContainer.reset(screenshotScale);
            tileContainer.setDrawingCacheEnabled(true);
            Bitmap b = Bitmap.createBitmap(tileContainer.getDrawingCache());
            b = resize(b);
            b = trim(b);

            FileOutputStream fos = activity.openFileOutput(filename, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String saveCardImage(Activity activity, int puzzleId) {
        RelativeLayout card = (RelativeLayout) activity.findViewById(R.id.puzzleCard);
        String puzzleName = Puzzle.getPuzzle(puzzleId).getCustomData().getName();

        if (card == null) {
            return "";
        }

        card.setDrawingCacheEnabled(true);
        Bitmap b = Bitmap.createBitmap(card.getDrawingCache());
        return insertImage(activity.getContentResolver(), b, puzzleName + " - CityFlow Puzzle Card");
    }

    public static String readQRImage(Activity activity, Bitmap bitmap) {
        String contents = "";

        BarcodeDetector barcodeDetector = new BarcodeDetector.Builder(activity)
                .setBarcodeFormats(Barcode.QR_CODE)
                .build();

        SparseArray<Barcode> detectedBarcodes = barcodeDetector.detect(new Frame.Builder()
                .setBitmap(bitmap)
                .build());

        if (detectedBarcodes.size() > 0 && detectedBarcodes.valueAt(0) != null) {
            contents = detectedBarcodes.valueAt(0).rawValue;
        }
        return contents;
    }

    private static Bitmap resize(Bitmap image) {
        int maxWidth = screenshotSize;
        int maxHeight = screenshotSize;

        int width = image.getWidth();
        int height = image.getHeight();
        float ratioBitmap = (float) width / (float) height;
        float ratioMax = (float) maxWidth / (float) maxHeight;

        int finalWidth = maxWidth;
        int finalHeight = maxHeight;
        if (ratioMax > 1) {
            finalWidth = (int) ((float) maxHeight * ratioBitmap);
        } else {
            finalHeight = (int) ((float) maxWidth / ratioBitmap);
        }
        image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        return image;
    }

    private static Bitmap trim(Bitmap sourceBitmap) {
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for (int y = 0; y < sourceBitmap.getHeight(); y++) {
            for (int x = 0; x < sourceBitmap.getWidth(); x++) {
                int alpha = (sourceBitmap.getPixel(x, y) >> 24) & 255;
                if (alpha > 0)   // pixel is not 100% transparent
                {
                    if (x < minX)
                        minX = x;
                    if (x > maxX)
                        maxX = x;
                    if (y < minY)
                        minY = y;
                    if (y > maxY)
                        maxY = y;
                }
            }
        }
        if ((maxX < minX) || (maxY < minY))
            return null; // Bitmap is entirely transparent

        // crop bitmap to non-transparent area and return:
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }

    public static String insertImage(ContentResolver cr, Bitmap source, String title) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, title);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis());
        values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());

        try {
            if (source != null) {
                Uri url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                OutputStream imageOut = cr.openOutputStream(url);
                source.compress(Bitmap.CompressFormat.PNG, 0, imageOut);
                imageOut.close();
                return url.toString();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
