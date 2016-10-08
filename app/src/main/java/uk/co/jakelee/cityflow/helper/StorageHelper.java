package uk.co.jakelee.cityflow.helper;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileOutputStream;
import java.util.EnumMap;
import java.util.Map;

import uk.co.jakelee.cityflow.R;
import uk.co.jakelee.cityflow.components.ZoomableViewGroup;

public class StorageHelper {
    private static final float screenshotScale = 0.1f;
    private static final int screenshotSize = 300;

    public final static int WHITE = 0xFFFFFFFF;
    public final static int BLACK = 0xFF000000;
    public final static int WIDTH = 400;
    public final static int HEIGHT = 400;

    public static void fillWithQrDrawable(ImageView imageView, String text) {
        try {
            Bitmap bitmap = encodeAsBitmap(text);
            imageView.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private static Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.MARGIN, 1);
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

    public static void saveCustomPuzzleImage(Activity activity, int puzzleId, boolean forceSave) {
        ZoomableViewGroup tileContainer = (ZoomableViewGroup)activity.findViewById(R.id.tileContainer);
        String filename = "puzzle_" + puzzleId + ".png";
        boolean existsAlready = activity.getFileStreamPath(filename).exists();
        Log.d("Exists?", (existsAlready ? "Yep " : "Nope ") + puzzleId);
        if (tileContainer == null || (!forceSave && existsAlready)) {
            return;
        }

        try {
            tileContainer.setBackgroundColor(Color.TRANSPARENT);
            tileContainer.setScaleFactor(screenshotScale);
            tileContainer.setDrawingCacheEnabled(true);
            Bitmap b = Bitmap.createBitmap(tileContainer.getDrawingCache());
            b = resize(b);
            b = trim(b);

            FileOutputStream fos = activity.openFileOutput(filename, Context.MODE_PRIVATE);
            Log.d("Saved", "Puzzle image: " + puzzleId);
            b.compress(Bitmap.CompressFormat.PNG, 0, fos);
            fos.flush();
            fos.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String saveCardImage(Activity activity, int puzzleId) {
        RelativeLayout card = (RelativeLayout)activity.findViewById(R.id.puzzleCard);

        if (card == null || !PermissionHelper.confirmPermissions(activity, PermissionHelper.WRITE_STORAGE)) {
            return "";
        }

        card.setDrawingCacheEnabled(true);
        Bitmap b = Bitmap.createBitmap(card.getDrawingCache());
        return MediaStore.Images.Media.insertImage(activity.getContentResolver(), b , "CityFlow_puzzle_" + puzzleId + ".png", null);

    }

    public static String readQRImage(Bitmap bMap) {
        String contents = "";

        int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];
        bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());

        LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

        MultiFormatReader reader = new MultiFormatReader();
        try {
            Result result = reader.decode(bitmap);
            contents = result.getText();
        } catch (Exception e) {

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
            finalWidth = (int) ((float)maxHeight * ratioBitmap);
        } else {
            finalHeight = (int) ((float)maxWidth / ratioBitmap);
        }
        image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
        return image;
    }

    private static Bitmap trim(Bitmap sourceBitmap)
    {
        int minX = sourceBitmap.getWidth();
        int minY = sourceBitmap.getHeight();
        int maxX = -1;
        int maxY = -1;
        for(int y = 0; y < sourceBitmap.getHeight(); y++)
        {
            for(int x = 0; x < sourceBitmap.getWidth(); x++)
            {
                int alpha = (sourceBitmap.getPixel(x, y) >> 24) & 255;
                if(alpha > 0)   // pixel is not 100% transparent
                {
                    if(x < minX)
                        minX = x;
                    if(x > maxX)
                        maxX = x;
                    if(y < minY)
                        minY = y;
                    if(y > maxY)
                        maxY = y;
                }
            }
        }
        if((maxX < minX) || (maxY < minY))
            return null; // Bitmap is entirely transparent

        // crop bitmap to non-transparent area and return:
        return Bitmap.createBitmap(sourceBitmap, minX, minY, (maxX - minX) + 1, (maxY - minY) + 1);
    }
}
