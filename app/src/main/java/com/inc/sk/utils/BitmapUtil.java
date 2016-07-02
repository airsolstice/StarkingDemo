package com.inc.sk.utils;


        import android.content.Context;
        import android.content.res.Resources;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.graphics.Canvas;
        import android.graphics.Matrix;
        import android.graphics.Paint;
        import android.graphics.PorterDuff;
        import android.graphics.PorterDuffXfermode;
        import android.graphics.Rect;
        import android.graphics.RectF;
        import android.util.Log;

        import java.io.File;
        import java.io.FileInputStream;
        import java.io.FileNotFoundException;
        import java.io.InputStream;

/**
 * Created by Holy-Spirit on 2015/11/28.
 * BaseClass for compres pictures(Bitmap)
 */

public class BitmapUtil {

    public static Bitmap makeRoundCorner(Bitmap bitmap, int px) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(0, 0, width, height);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, px, px, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static Bitmap makeRoundCorner(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int left = 0, top = 0, right = width, bottom = height;
        float roundPx = height / 2;
        if (width > height) {
            left = (width - height) / 2;
            top = 0;
            right = left + height;
            bottom = height;
        } else if (height > width) {
            left = 0;
            top = (height - width) / 2;
            right = width;
            bottom = top + width;
            roundPx = width / 2;
        }
        Log.i("asdsa", "ps:" + left + ", " + top + ", " + right + ", " + bottom);
        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        int color = 0xff424242;
        Paint paint = new Paint();
        Rect rect = new Rect(left, top, right, bottom);
        RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            while ((halfHeight / inSampleSize) > reqHeight &&
                    (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap zoomBitmap(Bitmap srcBitmap, int tagWidth, int tagHeight) {


        final int width = srcBitmap.getWidth();
        final int height = srcBitmap.getHeight();
        float scaleWidth = ((float) tagWidth) / width;
        float scaleHeight = ((float) tagHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);

        return Bitmap.createBitmap(srcBitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap decodeResource(
            Resources res, int resId, int reqWidth, int reqHeight) {
        System.out.println("-->>wid="+reqWidth+",hei="+reqHeight);

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        System.out.println("-->>size="+options.inSampleSize);
        options.inJustDecodeBounds = false;
        options.inPurgeable = true;// 同时设置才会有效
        options.inInputShareable = true;//。当系统内存不够时候图片自动被回收
        Bitmap bm = BitmapFactory.decodeResource(res, resId, options);
        if(bm == null){
            System.out.println("-->>bitmap is null");
            return null;
        }
        return zoomBitmap(bm,reqWidth,reqHeight);
    }

    public static Bitmap decodeFileStream(File f, int reqWidth, int reqHeight) {
        try {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, opt);
            opt.inSampleSize = calculateInSampleSize(opt, reqWidth, reqHeight);
            opt.inJustDecodeBounds = false;
            opt.inPurgeable = true;// 同时设置才会有效
            opt.inInputShareable = true;//。当系统内存不够时候图片自动被回收
            Bitmap bm = BitmapFactory.decodeStream(new FileInputStream(f), null, opt);
            return zoomBitmap(bm, reqWidth, reqHeight);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Bitmap decodeByteArray(byte[] datas, int reqWidth, int reqHeight) {

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(datas, 0, datas.length, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeByteArray(datas, 0, datas.length, options);
    }

    public static Bitmap decodeStream(InputStream inStream1, InputStream inStream2
            , int reqWidth, int reqHeight) {


        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(inStream1, null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeStream(inStream2, null, options);
    }

    public static Bitmap decodeRawFile(Context context, int resId, int reqWidth,
                                       int reqHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(context.getResources().openRawResource(resId), null, options);
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inJustDecodeBounds = false;
        return BitmapFactory
                .decodeStream(context.getResources().openRawResource(resId), null, options);
    }


}

