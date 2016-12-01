package com.zis.musapp.gh.features.choosesong.compileImages;

import android.graphics.Bitmap;
import android.media.MediaCodec;
import android.media.MediaCodecInfo;
import android.media.MediaFormat;
import android.media.MediaMuxer;
import android.media.effect.Effect;
import android.media.effect.EffectContext;
import android.net.Uri;
import android.opengl.EGL14;
import android.opengl.EGLConfig;
import android.opengl.EGLContext;
import android.opengl.EGLDisplay;
import android.opengl.EGLExt;
import android.opengl.EGLSurface;
import android.opengl.GLES20;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;
import com.yatatsu.autobundle.AutoBundleField;
import com.zis.musapp.base.android.BaseActivity;
import com.zis.musapp.gh.R;
import com.zis.musapp.gh.model.clip.Clip;
import com.zis.musapp.gh.model.clip.Part;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class CompileImageToVideoActivity extends BaseActivity {
  private static final String TAG = "WRITE MOVIE";
  private static final File OUTPUT_DIR = Environment.getExternalStorageDirectory();

  @AutoBundleField Clip clip;

  @Override protected boolean hasArgs() {
    return true;
  }

  private int durationInSec;

  private static final String MIME_TYPE = "video/avc";    // H.264 Advanced Video Coding
  private static final int FRAME_RATE = 10;               // 10fps
  private static final int IFRAME_INTERVAL = 5;           // 5 seconds between I-frames
  private CodecInputSurface mInputSurface;
  private MediaMuxer mMuxer;
  private MediaCodec mEncoder;
  private MediaCodec.BufferInfo mBufferInfo;
  private int mBitRate = -1;
  private int mTrackIndex;
  private boolean mMuxerStarted;

  private int mWidth;
  private int mHeight;
  private int mImageWidth;
  private int mImageHeight;

  private long presentationTime;
  private long durationInNanosec;

  private int texId = -1;
  private EffectContext mEffectcontext;
  private Effect mEffect;
  private TextureRenderer mTexRenderer = new TextureRenderer();

  private GLEnv mEnv;

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_write_movie);

    //if (Objects.equals(size, "small")) {
    //  mBitRate = 1000000;
    //  if (Objects.equals(orientation, "portrait")) {
    //    mWidth = 144;
    //    mHeight = 176;
    //  } else {
    //    mWidth = 176;
    //    mHeight = 144;
    //  }
    //} else if (Objects.equals(size, "default")) {
    //  mBitRate = 2000000;
    //  if (Objects.equals(orientation, "portrait")) {
    //    mWidth = 240;
    //    mHeight = 320;
    //  } else {
    //    mWidth = 320;
    //    mHeight = 240;
    //  }
    //} else {
    //  mBitRate = 6000000;
    //  if (Objects.equals(orientation, "portrait")) {
    //    mWidth = 720;
    //    mHeight = 1280;
    //  } else {
    //    mWidth = 1280;
    //    mHeight = 720;
    //  }
    //}

    //BitmapFactory.Options resizeOptions = new BitmapFactory.Options();
    //resizeOptions.inSampleSize = 1; // decrease size 1 time
    //resizeOptions.inScaled = true;

    mBitRate = 1000000;
    mWidth = 144;
    mHeight = 176;

    mEnv = new GLEnv();
    mEnv.makeCurrent();
    mEffectcontext = EffectContext.createWithCurrentGlContext();
    mTexRenderer.init();

    try {
      createMovie();
    } catch (Throwable throwable) {
      throwable.printStackTrace();
    }
  }

  @Override protected void initializeInjector() {

  }

  private Bitmap scaleBitmap(Bitmap bm) {
    int width = bm.getWidth();
    int height = bm.getHeight();

    //Log.v("Pictures", "Width and height are " + width + "--" + height);

    if (width > height) {
      // landscape
      float ratio = (float) width / mWidth;
      width = mWidth;
      height = (int) (height / ratio);
    } else if (height > width) {
      // portrait
      float ratio = (float) height / mHeight;
      height = mHeight;
      width = (int) (width / ratio);
    } else {
      // square
      height = mHeight;
      width = mWidth;
    }

    //Log.v("Pictures", "after scaling Width and height are " + width + "--" + height);

    bm = Bitmap.createScaledBitmap(bm, width, height, true);
    return bm;
  }

  public void createMovie() throws Throwable {

    Log.v(TAG, "CREATING MOVIE");

    try {
      prepareEncoder();

      presentationTime = 0;

      for (Part part : clip.parts()) {

        drainEncoder(false);

        generateSurfaceFrame(part.media().getContentUri());

        mInputSurface.setPresentationTime(presentationTime);

        Log.v(TAG, "sending frame ...");
        mInputSurface.swapBuffers();

        //Convert frame duration from seconds to nanoseconds
        durationInNanosec = (long) (1 * 1_000_000_000);

        presentationTime += durationInNanosec;
      }
      drainEncoder(true);
    } catch (Throwable e) {
      Log.e(TAG, e.getMessage(), e);
    } finally {
      // release encoder, muxer, and input Surface
      releaseEncoder();
      Log.v(TAG, "VIDEO CREATED");
      Toast.makeText(this, "Video Created!", Toast.LENGTH_LONG).show();
    }
  }

  private void generateSurfaceFrame(Uri uri) throws IOException {

    mEnv.makeCurrent();

    if (texId >= 0) {
      mEnv.releaseTexture(texId);
    }
    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
    Bitmap scaledBitmap = scaleBitmap(bitmap);
    texId = loadTextures(scaledBitmap);
    //initAndApplyEffect();
    renderResult(texId);

    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    ByteBuffer pixelBuffer =
        ByteBuffer.allocateDirect(mImageWidth * mImageHeight * 4).order(ByteOrder.nativeOrder());
    GLES20.glReadPixels(0, 0, mImageWidth, mImageHeight, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE,
        pixelBuffer);
    GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    mEnv.checkForEGLErrors("store Pixels");

    mInputSurface.makeCurrent();
  }

  //private void initAndApplyEffect() {
  //  EffectFactory effectFactory = mEffectcontext.getFactory();
  //  if (mEffect != null) {
  //    mEffect.release();
  //  }
  //  mEffect = effectFactory.createEffect(EffectFactory.EFFECT_BRIGHTNESS);
  //  mEffect.setParameter("brightness", 2.0f);
  //  mEffect.apply(mTextures[0], mWidth, mHeight, mTextures[1]);
  //}

  private int loadTextures(Bitmap bitmap) {
    int[] mTextures = new int[1];
    // Generate textures
    GLES20.glGenTextures(1, mTextures, 0);

    mImageWidth = bitmap.getWidth();
    mImageHeight = bitmap.getHeight();
    mTexRenderer.updateTextureSize(mImageWidth, mImageHeight);
    mTexRenderer.updateViewSize(mImageWidth, mImageHeight);
    // Upload to texture
    GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
    GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);

    // Set texture parameters
    GLToolbox.initTexParams();

    return mTextures[0];
  }

  private void renderResult(int texId) {
    mTexRenderer.renderTexture(texId);
    //mTexRenderer.renderTexture(mTextures[0]);
  }

  private void prepareEncoder() {
    mBufferInfo = new MediaCodec.BufferInfo();

    MediaFormat format = MediaFormat.createVideoFormat(MIME_TYPE, mWidth, mHeight);

    // Set some properties.  Failing to specify some of these can cause the MediaCodec
    // configure() call to throw an unhelpful exception.
    format.setInteger(MediaFormat.KEY_COLOR_FORMAT,
        MediaCodecInfo.CodecCapabilities.COLOR_FormatSurface);
    format.setInteger(MediaFormat.KEY_BIT_RATE, mBitRate);
    format.setInteger(MediaFormat.KEY_FRAME_RATE, FRAME_RATE);
    format.setInteger(MediaFormat.KEY_I_FRAME_INTERVAL, IFRAME_INTERVAL);
    Log.v(TAG, "format: " + format);

    // Create a MediaCodec encoder, and configure it with our format.  Get a Surface
    // we can use for input and wrap it with a class that handles the EGL work.
    //
    // If you want to have two EGL contexts -- one for display, one for recording --
    // you will likely want to defer instantiation of CodecInputSurface until after the
    // "display" EGL context is created, then modify the eglCreateContext call to
    // take eglGetCurrentContext() as the share_context argument.
    try {
      mEncoder = MediaCodec.createEncoderByType(MIME_TYPE);
    } catch (IOException e) {
      e.printStackTrace();
    }
    mEncoder.configure(format, null, null, MediaCodec.CONFIGURE_FLAG_ENCODE);
    mInputSurface = new CodecInputSurface(mEncoder.createInputSurface());
    mEncoder.start();

    // Output filename.  Ideally this would use Context.getFilesDir() rather than a
    // hard-coded output directory.
    String outputPath = new File(OUTPUT_DIR, "test." + mWidth + "x" + mHeight + ".mp4").toString();
    Log.d(TAG, "output file is " + outputPath);

    // Create a MediaMuxer.  We can't add the video track and start() the muxer here,
    // because our MediaFormat doesn't have the Magic Goodies.  These can only be
    // obtained from the encoder after it has started processing data.
    //
    // We're not actually interested in multiplexing audio.  We just want to convert
    // the raw H.264 elementary stream we get from MediaCodec into a .mp4 file.
    try {
      mMuxer = new MediaMuxer(outputPath, MediaMuxer.OutputFormat.MUXER_OUTPUT_MPEG_4);
    } catch (IOException ioe) {
      throw new RuntimeException("MediaMuxer creation failed", ioe);
    }

    mTrackIndex = -1;
    mMuxerStarted = false;
  }

  private void releaseEncoder() {
    Log.v(TAG, "releasing encoder objects");
    if (mEncoder != null) {
      mEncoder.stop();
      mEncoder.release();
      mEncoder = null;
    }
    if (mInputSurface != null) {
      mInputSurface.release();
      mInputSurface = null;
    }
    if (mMuxer != null) {
      mMuxer.stop();
      mMuxer.release();
      mMuxer = null;
    }
  }

  private void drainEncoder(boolean endOfStream) {
    final int TIMEOUT_USEC = 10000;
    Log.v(TAG, "drainEncoder(" + endOfStream + ")");

    if (endOfStream) {
      Log.v(TAG, "sending EOS to encoder");
      mEncoder.signalEndOfInputStream();
    }

    ByteBuffer[] encoderOutputBuffers = mEncoder.getOutputBuffers();
    while (true) {
      int encoderStatus = mEncoder.dequeueOutputBuffer(mBufferInfo, TIMEOUT_USEC);
      if (encoderStatus == MediaCodec.INFO_TRY_AGAIN_LATER) {
        // no output available yet
        if (!endOfStream) {
          break;      // out of while
        } else {
          Log.v(TAG, "no output available, spinning to await EOS");
        }
      } else if (encoderStatus == MediaCodec.INFO_OUTPUT_BUFFERS_CHANGED) {
        // not expected for an encoder
        encoderOutputBuffers = mEncoder.getOutputBuffers();
      } else if (encoderStatus == MediaCodec.INFO_OUTPUT_FORMAT_CHANGED) {
        // should happen before receiving buffers, and should only happen once
        if (mMuxerStarted) {
          throw new RuntimeException("format changed twice");
        }
        MediaFormat newFormat = mEncoder.getOutputFormat();
        Log.d(TAG, "encoder output format changed: " + newFormat);

        // now that we have the Magic Goodies, start the muxer
        mTrackIndex = mMuxer.addTrack(newFormat);
        mMuxer.start();
        mMuxerStarted = true;
      } else if (encoderStatus < 0) {
        Log.w(TAG, "unexpected result from encoder.dequeueOutputBuffer: " + encoderStatus);
        // let's ignore it
      } else {
        ByteBuffer encodedData = encoderOutputBuffers[encoderStatus];
        if (encodedData == null) {
          throw new RuntimeException("encoderOutputBuffer " + encoderStatus +
              " was null");
        }

        if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_CODEC_CONFIG) != 0) {
          // The codec config data was pulled out and fed to the muxer when we got
          // the INFO_OUTPUT_FORMAT_CHANGED status.  Ignore it.
          Log.v(TAG, "ignoring BUFFER_FLAG_CODEC_CONFIG");
          mBufferInfo.size = 0;
        }

        if (mBufferInfo.size != 0) {
          if (!mMuxerStarted) {
            throw new RuntimeException("muxer hasn't started");
          }

          // adjust the ByteBuffer values to match BufferInfo (not needed?)
          encodedData.position(mBufferInfo.offset);
          encodedData.limit(mBufferInfo.offset + mBufferInfo.size);

          mMuxer.writeSampleData(mTrackIndex, encodedData, mBufferInfo);
          Log.v(TAG, "sent " + mBufferInfo.size + " bytes to muxer");
        }

        mEncoder.releaseOutputBuffer(encoderStatus, false);

        if ((mBufferInfo.flags & MediaCodec.BUFFER_FLAG_END_OF_STREAM) != 0) {
          if (!endOfStream) {
            Log.w(TAG, "reached end of stream unexpectedly");
          } else {
            Log.v(TAG, "end of stream reached");
          }
          break;      // out of while
        }
      }
    }
  }

  private static class CodecInputSurface {
    private static final int EGL_RECORDABLE_ANDROID = 0x3142;

    private EGLDisplay mEGLDisplay = EGL14.EGL_NO_DISPLAY;
    private EGLContext mEGLContext = EGL14.EGL_NO_CONTEXT;
    private EGLSurface mEGLSurface = EGL14.EGL_NO_SURFACE;

    private Surface mSurface;

    /**
     * Creates a CodecInputSurface from a Surface.
     */
    public CodecInputSurface(Surface surface) {
      if (surface == null) {
        throw new NullPointerException();
      }
      mSurface = surface;

      eglSetup();
    }

    /**
     * Prepares EGL.  We want a GLES 2.0 context and a surface that supports recording.
     */
    private void eglSetup() {
      mEGLDisplay = EGL14.eglGetDisplay(EGL14.EGL_DEFAULT_DISPLAY);
      if (mEGLDisplay == EGL14.EGL_NO_DISPLAY) {
        throw new RuntimeException("unable to get EGL14 display");
      }
      int[] version = new int[2];
      if (!EGL14.eglInitialize(mEGLDisplay, version, 0, version, 1)) {
        throw new RuntimeException("unable to initialize EGL14");
      }

      // Configure EGL for recording and OpenGL ES 2.0.
      int[] attribList = {
          EGL14.EGL_RED_SIZE, 8, EGL14.EGL_GREEN_SIZE, 8, EGL14.EGL_BLUE_SIZE, 8,
          EGL14.EGL_ALPHA_SIZE, 8, EGL14.EGL_RENDERABLE_TYPE, EGL14.EGL_OPENGL_ES2_BIT,
          EGL_RECORDABLE_ANDROID, 1, EGL14.EGL_NONE
      };
      EGLConfig[] configs = new EGLConfig[1];
      int[] numConfigs = new int[1];
      EGL14.eglChooseConfig(mEGLDisplay, attribList, 0, configs, 0, configs.length, numConfigs, 0);
      checkEglError("eglCreateContext RGB888+recordable ES2");

      // Configure context for OpenGL ES 2.0.
      int[] attrib_list = {
          EGL14.EGL_CONTEXT_CLIENT_VERSION, 2, EGL14.EGL_NONE
      };
      mEGLContext =
          EGL14.eglCreateContext(mEGLDisplay, configs[0], EGL14.eglGetCurrentContext(), attrib_list,
              0);
      checkEglError("eglCreateContext");

      // Create a window surface, and attach it to the Surface we received.
      int[] surfaceAttribs = {
          EGL14.EGL_NONE
      };
      mEGLSurface =
          EGL14.eglCreateWindowSurface(mEGLDisplay, configs[0], mSurface, surfaceAttribs, 0);
      checkEglError("eglCreateWindowSurface");
    }

    /**
     * Discards all resources held by this class, notably the EGL context.  Also releases the
     * Surface that was passed to our constructor.
     */
    public void release() {
      if (mEGLDisplay != EGL14.EGL_NO_DISPLAY) {
        EGL14.eglMakeCurrent(mEGLDisplay, EGL14.EGL_NO_SURFACE, EGL14.EGL_NO_SURFACE,
            EGL14.EGL_NO_CONTEXT);
        EGL14.eglDestroySurface(mEGLDisplay, mEGLSurface);
        EGL14.eglDestroyContext(mEGLDisplay, mEGLContext);
        EGL14.eglReleaseThread();
        EGL14.eglTerminate(mEGLDisplay);
      }

      mSurface.release();

      mEGLDisplay = EGL14.EGL_NO_DISPLAY;
      mEGLContext = EGL14.EGL_NO_CONTEXT;
      mEGLSurface = EGL14.EGL_NO_SURFACE;

      mSurface = null;
    }

    /**
     * Makes our EGL context and surface current.
     */
    public void makeCurrent() {
      EGL14.eglMakeCurrent(mEGLDisplay, mEGLSurface, mEGLSurface, mEGLContext);
      checkEglError("eglMakeCurrent");
    }

    /**
     * Calls eglSwapBuffers.  Use this to "publish" the current frame.
     */
    public boolean swapBuffers() {
      boolean result = EGL14.eglSwapBuffers(mEGLDisplay, mEGLSurface);
      checkEglError("eglSwapBuffers");
      return result;
    }

    /**
     * Sends the presentation time stamp to EGL.  Time is expressed in nanoseconds.
     */
    public void setPresentationTime(long nsecs) {
      EGLExt.eglPresentationTimeANDROID(mEGLDisplay, mEGLSurface, nsecs);
      checkEglError("eglPresentationTimeANDROID");
    }

    /**
     * Checks for EGL errors.  Throws an exception if one is found.
     */
    private void checkEglError(String msg) {
      int error;
      if ((error = EGL14.eglGetError()) != EGL14.EGL_SUCCESS) {
        throw new RuntimeException(msg + ": EGL error: 0x" + Integer.toHexString(error));
      }
    }
  }
}
