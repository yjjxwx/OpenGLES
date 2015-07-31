package gles.alex.com.opengles.renderer;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.GLUtils;
import android.util.Log;

import java.nio.FloatBuffer;
import java.util.Objects;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import gles.alex.com.opengles.R;
import gles.alex.com.opengles.util.BufferUtils;
import gles.alex.com.opengles.util.GaussianFunction;
import gles.alex.com.opengles.util.ShaderUtil;

/**
 * Created by alex on 15-6-25.
 */
public class MainViewRenderer implements GLSurfaceView.Renderer {

    private int program = 0;

    private String vertextShaderSource;
    private String fragmentShaderSource;
    private FloatBuffer mVertices;
    private FloatBuffer mCoords;
    private Activity mActivity;
    private volatile int control;
    private float mTexelWidthOffset =  0.05f;
    private float mTexelHeightOffset = 0.05f;
    private int mWidth;
    private int mHeight;
    private int[] mTextures;

    private float [] mVerticeData = new float[] {
            -1.0f, -1.0f, 0.0f,
            1.0f, -1.0f, 0.0f,
            1.0f, 1.0f, 0.0f,
            -1.0f, 1.0f, 0.0f
    };
    private float [] mCoordsData = new float[] {
            0, 0,
            1, 0,
            1, 1,
            0, 1
    };


    public MainViewRenderer(Activity activity, String vertextShaderSource, String fragmentShaderSource) {
        mActivity = activity;
        this.vertextShaderSource = vertextShaderSource;
        this.fragmentShaderSource = fragmentShaderSource;
        mVertices = BufferUtils.getFloatBuffer(mVerticeData);
        mCoords = BufferUtils.getFloatBuffer(mCoordsData);
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        program = ShaderUtil.createProgram(vertextShaderSource, fragmentShaderSource);
    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        mWidth = width;
        mHeight = height;
        GLES20.glViewport(0, 0, width, height);
        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        mTextures = new int[1];
        GLES20.glGenTextures(mTextures.length, mTextures, 0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
        Bitmap bmp = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.girl);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bmp, 0);
        bmp.recycle();
        texW = 256;
        texH = texW * mHeight / mWidth;
        //texH = texW;
        fbScale = new Framebuffer(texW, texH);
        fbH = new Framebuffer(texW,texH);
        fbV = new Framebuffer(texW, texH);
    }
    int texW;
    int texH;
    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(program);
        int positionLoc = GLES20.glGetAttribLocation(program, "a_position");
        int coordsLoc = GLES20.glGetAttribLocation(program, "a_texCoord");
        GLES20.glEnableVertexAttribArray(positionLoc);
        GLES20.glVertexAttribPointer(positionLoc, 3, GLES20.GL_FLOAT, false, 0, mVertices);
        GLES20.glEnableVertexAttribArray(coordsLoc);
        GLES20.glVertexAttribPointer(coordsLoc, 2, GLES20.GL_FLOAT, false, 0, mCoords);

        GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "control"), control);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "screen_width"), mWidth);
        GLES20.glUniform1f(GLES20.glGetUniformLocation(program, "screen_height"), mHeight);

        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mTextures[0]);
        GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "blurSource"), 0);

        GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "action"), 1);


        if (control == 1) {

            float[] weights = getWeights(getRadius());
            GLES20.glUniform1i(GLES20.glGetUniformLocation(program,"samplerRadius"), (int)getRadius());
            float[] samplerStepXs = getSamplerStepX((int)getRadius());
            float[] samplerStepYs = getSamplerStepY((int)getRadius());

            float[] tmp = new float[16];
            System.arraycopy(weights, 0, tmp, 0, weights.length > 16 ? 16: weights.length);
            GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "weights1"), 1, false, tmp, 0);

            System.arraycopy(samplerStepXs, 0, tmp, 0, samplerStepXs.length > 16 ? 16: samplerStepXs.length);
            GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "samplerStepX1"), 1, false, tmp, 0);

            System.arraycopy(samplerStepYs, 0, tmp, 0, samplerStepYs.length > 16 ? 16: samplerStepYs.length);
            GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "samplerStepY1"), 1, false, tmp, 0);

            if (getRadius() > 16) {
                System.arraycopy(weights, 16, tmp, 0, weights.length - 16);
                GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "weights2"), 1, false, tmp, 0);

                System.arraycopy(samplerStepXs, 16, tmp, 0, samplerStepXs.length - 16);
                GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "samplerStepX2"), 1, false, tmp, 0);

                System.arraycopy(samplerStepYs, 16, tmp, 0, samplerStepYs.length - 16);
                GLES20.glUniformMatrix4fv(GLES20.glGetUniformLocation(program, "samplerStepY2"), 1, false, tmp, 0);

            }

            // create render target
            GLES20.glViewport(0, 0, texW, texH);
            fbScale.bind();
            {
                GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "action"), 2);
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
            }

            fbH.bind();
            {
                GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "action"), 0);
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 1);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fbScale.getColorTexture());
                GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "blurSource"), 1);
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
            }

            fbV.bind();
            {
                GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "action"), 1);
                GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 2);
                GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fbH.getColorTexture());
                GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "blurSource"), 2);
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
            }
            fbV.unbind();

            GLES20.glViewport(0, 0, mWidth, mHeight);
            GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "action"), 3);
            GLES20.glActiveTexture(GLES20.GL_TEXTURE0 + 3);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, fbV.getColorTexture());
            GLES20.glUniform1i(GLES20.glGetUniformLocation(program, "blurSource"), 3);
        }
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, 4);
        GLES20.glDisableVertexAttribArray(positionLoc);
        GLES20.glDisableVertexAttribArray(coordsLoc);
    }
    Framebuffer fbScale;
    Framebuffer fbV;
    Framebuffer fbH;
    public void changeControl() {
        if (control == 0) {
            control = 1;
        } else {
            control = 0;
        }
        if (am == null) {
            am = ObjectAnimator.ofFloat(1.0f, 32.0f);
            am.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    float value = (float)animation.getAnimatedValue();
                    mRadius = value;
                }
            });
            am.setDuration(2000);
            am.start();
        } else {
            am.end();
            am.start();
        }
    }

    ValueAnimator am;

    public float getRadius() {
        return mRadius;
    }
    volatile float mRadius = 8f;

    public static float[] getWeights(float radius) {
        if (radius > 32) {
            throw new RuntimeException("SampleCount must less equals than 32");
        }
        float weights[] = GaussianFunction.getWeights(radius);
        return weights;
    }

    public float[] getSamplerStepX(int samplerCount) {
        float[] ssx = new float[samplerCount];
        for (int i = 0; i < samplerCount; ++i) {
            ssx[i] = i * 1.0f / texW;
        }
        return ssx;
    }

    public float[] getSamplerStepY(int samplerCount) {
        float[] ssy = new float[samplerCount];
        for (int i = 0; i < samplerCount; ++i) {
            ssy[i] = i * 1.0f / texH;
        }
        return ssy;
    }

    private void printArray(String name, float[] array) {
        Log.d("yjjxwx", "Start ------------------" + name + "------------------");
        StringBuilder sb = new StringBuilder();
        float sum = 0.0f;
        for(float i : array) {
            sum += i;
            sb.append(i).append(' ');
        }
        Log.d("yjjxwx", sb.toString());
        Log.d("yjjxwx", "End ------------------" + name    +"------------------" + sum);
    }
}
