package com.zis.musapp.gh.features.choosesong.compileImages;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.opengl.GLES20;
import android.provider.MediaStore;
import android.util.Log;
import com.android.grafika.ContentManager;
import com.android.grafika.GeneratedMovie;
import com.android.grafika.MainActivity;
import com.android.grafika.gles.FullFrameRect;
import com.android.grafika.gles.GeneratedTexture;
import com.android.grafika.gles.Texture2dProgram;
import com.zis.musapp.gh.model.clip.Clip;
import com.zis.musapp.gh.model.clip.Part;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

/**
 * Generates a simple movie, featuring two small rectangles that slide across the screen.
 */
public class MovieSliders extends GeneratedMovie {
  private static final String TAG = MainActivity.TAG;

  private static final String MIME_TYPE = "video/avc";
  private static final int WIDTH = 240;       // note 480x640, not 640x480
  private static final int HEIGHT = 320;
  private static final int BIT_RATE = 5000000;
  private static final int FRAMES_PER_SECOND = 2;
  private Context context;
  private Clip clip;
  private SurfaceTexture mSurfaceTexture;

  //TextureRenderer textureRenderer;
  public MovieSliders(Context context, Clip clip) {
    this.context = context;
    this.clip = clip;
  }
  //{
  //  textureRenderer = new TextureRenderer();
  //  textureRenderer.init();
  //}

  private final float[] mSTMatrix = new float[16];
  @Override public void create(File outputFile, ContentManager.ProgressUpdater prog) {
    if (mMovieReady) {
      throw new RuntimeException("Already created");
    }

    final int NUM_FRAMES = 10;

    try {
      prepareEncoder(MIME_TYPE, WIDTH, HEIGHT, BIT_RATE, FRAMES_PER_SECOND, outputFile);

      for (Part part : clip.parts()) {
        drainEncoder(false);
        Uri contentUri = part.media().getContentUri();

        Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), contentUri);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true);
        int bytes = scaledBitmap.getAllocationByteCount();
        //or we can calculate bytes this way. Use a different value than 4 if you don't use 32bit images.
        //int bytes = b.getWidth()*b.getHeight()*4;

        ByteBuffer buffer = ByteBuffer.allocateDirect(bytes); //Create a new buffer
        scaledBitmap.copyPixelsToBuffer(buffer); //Move the byte data to the buffer
       // ByteBuffer byteBuffer = ByteBuffer.allocateDirect(WIDTH * HEIGHT * 4);

        buffer.position(0);
        //texId = loadTextures(scaledBitmap);
        //initAndApplyEffect();

        //GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
        //ByteBuffer pixelBuffer =
        //    ByteBuffer.allocateDirect(WIDTH * HEIGHT * 4).order(ByteOrder.nativeOrder());
        //GLES20.glReadPixels(0, 0, WIDTH, HEIGHT, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE,
        //    pixelBuffer);


        //int imageTexture = GeneratedTexture.createTestTexture(GeneratedTexture.Image.FINE);
        FullFrameRect fullFrameRect =
            new FullFrameRect(new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_2D));

        //GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);

        int imageTexture = GlUtil.createImageTexture(buffer, WIDTH, HEIGHT, GLES20.GL_RGBA);

        int textureObject = fullFrameRect.createTextureObject();
        mSurfaceTexture = new SurfaceTexture(textureObject);

       // int textureObject = fullFrameRect.createTextureObject();
        mSurfaceTexture.getTransformMatrix(mSTMatrix);
        fullFrameRect.drawFrame(imageTexture,mSTMatrix);
        //TextureRenderer textureRenderer = new TextureRenderer();
        //textureRenderer.renderTexture();
        //textureRenderer.renderTexture(imageTexture);
        int frameIndex = clip.parts().indexOf(part);
        submitFrame(computePresentationTimeNsec(frameIndex));

        prog.updateProgress(frameIndex * 100 / NUM_FRAMES);
      }

      //for (int i = 0; i < NUM_FRAMES; i++) {
      //  // Drain any data from the encoder into the muxer.
      //  drainEncoder(false);
      //
      //  // Generate a frame and submit it.
      //  generateFrame(i);
      //  submitFrame(computePresentationTimeNsec(i));
      //
      //  prog.updateProgress(i * 100 / NUM_FRAMES);
      //}

      // Send end-of-stream and drain remaining output.
      drainEncoder(true);
    } catch (IOException ioe) {
      throw new RuntimeException(ioe);
    } finally {
      releaseEncoder();
      mSurfaceTexture.release();
      mSurfaceTexture = null;
    }

    Log.d(TAG, "MovieEightRects complete: " + outputFile);
    mMovieReady = true;
  }

  /**
   * Generates a frame of data using GL commands.
   */
  private void generateFrame(int frameIndex) {
    final int BOX_SIZE = 80;
    frameIndex %= 240;
    int xpos, ypos;

    int absIndex = Math.abs(frameIndex - 120);
    xpos = absIndex * WIDTH / 120;
    ypos = absIndex * HEIGHT / 120;

    float lumaf = absIndex / 120.0f;

    GLES20.glClearColor(lumaf, lumaf, lumaf, 1.0f);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);

    GLES20.glEnable(GLES20.GL_SCISSOR_TEST);
    GLES20.glScissor(BOX_SIZE / 2, ypos, BOX_SIZE, BOX_SIZE);
    GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    GLES20.glScissor(xpos, BOX_SIZE / 2, BOX_SIZE, BOX_SIZE);
    GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
    GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
    GLES20.glDisable(GLES20.GL_SCISSOR_TEST);
  }

  /**
   * Generates the presentation time for frame N, in nanoseconds.  Fixed frame rate.
   */
  private static long computePresentationTimeNsec(int frameIndex) {
    final long ONE_BILLION = 1000000000;
    return frameIndex * ONE_BILLION / FRAMES_PER_SECOND;
  }
}
