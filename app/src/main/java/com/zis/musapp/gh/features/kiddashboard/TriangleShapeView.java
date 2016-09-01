package com.zis.musapp.gh.features.kiddashboard;

/**
 * Created by mikhail on 29/08/16.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import com.zis.musapp.gh.R;

public class TriangleShapeView extends View {

  private static final int ARROW_WIDTH = 45;
  private final Paint mPathPaint;
  private Path mPath;

  public TriangleShapeView(final Context context) {
    this(context, null);
  }

  public TriangleShapeView(final Context context, final AttributeSet attrs) {
    super(context, attrs);
    mPathPaint = new Paint();
    mPathPaint.setColor(getContext().getResources().getColor(R.color.gross_blue));
  }

  @Override protected void onSizeChanged(final int w, final int h, final int oldw, final int oldh) {
    super.onSizeChanged(w, h, oldw, oldh);
    mPath = new Path();
    mPath.moveTo(w/2 - ARROW_WIDTH / 2, 0);
    mPath.lineTo(w/2 + ARROW_WIDTH / 2, 0);
    mPath.lineTo(w/2, h);
    mPath.lineTo(w/2 - ARROW_WIDTH / 2, 0);
    mPath.close();
  }

  protected void onDraw(final Canvas canvas) {
    super.onDraw(canvas);
    canvas.drawPath(mPath, mPathPaint);
  }
}