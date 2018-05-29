package com.khsm.app.presentation.ui.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class PrintBitmapBuilder {
    private static final int PRINT_WIDTH = 620;
    private static final int PRINT_HIGHT = 877;
    private static final int TEXT_SIZE = 15;

    private final Context context;
    private final LinearLayout linearLayout;

    private int textSizeScale = 1;

    private ReceiptTextAlign textAlign = ReceiptTextAlign.LEFT;

    public PrintBitmapBuilder(@NonNull Context context) {
        this.context = context;

        linearLayout = new LinearLayout(context);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setBackgroundColor(Color.WHITE);
        linearLayout.setPadding(20, 20, 20, 20);
    }

    public void appendString(String text) {
        TextView textView = new TextView(context);

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        textView.setLayoutParams(linearLayoutParams);

        int textAlignment = TextView.TEXT_ALIGNMENT_CENTER;

        if (textAlign != null) {
            switch (textAlign) {
                case LEFT:
                    textAlignment = TextView.TEXT_ALIGNMENT_TEXT_START;
                    break;
                case CENTER:
                    textAlignment = TextView.TEXT_ALIGNMENT_CENTER;
                    break;
                case RIGHT:
                    textAlignment = TextView.TEXT_ALIGNMENT_TEXT_END;
                    break;
            }
        }

        textView.setTextColor(Color.BLACK);
        textView.setText(text);
        textView.setBackgroundColor(Color.WHITE);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, TEXT_SIZE * textSizeScale);
        textView.setTypeface(Typeface.create(Typeface.MONOSPACE, Typeface.NORMAL));
        textView.setTextAlignment(textAlignment);

        linearLayout.addView(textView);
    }

    public void appendBitmap(Bitmap bitmap) {
        ImageView imageView = new ImageView(context);

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        linearLayoutParams.gravity = Gravity.CENTER_HORIZONTAL;

        imageView.setImageBitmap(bitmap);
        imageView.setLayoutParams(linearLayoutParams);
        imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

        linearLayout.addView(imageView);
    }

    public void setTextAlign(ReceiptTextAlign textAlign) {
        this.textAlign = textAlign;
    }

    public void setTextSizeScale(int textSizeScale) {
        this.textSizeScale = textSizeScale;
    }

    public Bitmap build() {
        int widthSpec = View.MeasureSpec.makeMeasureSpec (PRINT_WIDTH, View.MeasureSpec.EXACTLY);
        int heightSpec = View.MeasureSpec.makeMeasureSpec (PRINT_HIGHT, View.MeasureSpec.EXACTLY);
        linearLayout.measure(widthSpec, heightSpec);
        linearLayout.layout(0, 0, linearLayout.getMeasuredWidth(), linearLayout.getMeasuredHeight());

        int width = linearLayout.getWidth();
        int height = linearLayout.getHeight();
        Bitmap bitmap = Bitmap.createBitmap(
                width, height,
                Bitmap.Config.RGB_565);

        Canvas canvas = new Canvas(bitmap);

        linearLayout.draw(canvas);

        return bitmap;
    }

    public enum ReceiptTextAlign {
        LEFT,
        CENTER,
        RIGHT;

        public static ReceiptTextAlign valueOf(int textAlign) {
            return ReceiptTextAlign.values()[textAlign];
        }
    }
}

