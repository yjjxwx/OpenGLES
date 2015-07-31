package gles.alex.com.opengles.renderer;

import android.opengl.GLES20;
import android.support.v4.app.ShareCompat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;

/**
 * Created by alex on 15-7-30.
 */
public class Framebuffer {
    private int mFbo;
    private int mColorTexture;
    private int mDepthTexture; // not need the depth texture
    private int mWidth, mHeight;

    public Framebuffer(int width, int height) {
        mWidth = width;
        mHeight = height;
        createFramebuffer();
    }

    private void createFramebuffer() {
        int [] fbo = new int[1];
        GLES20.glGenFramebuffers(1, fbo, 0);
        mFbo = fbo[0];
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFbo);
        int [] tex = new int[1];
        GLES20.glGenTextures(1, tex, 0);
        mColorTexture = tex[0];
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, mColorTexture);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);

        int[] buf = new int[mWidth * mHeight];
        IntBuffer texBuffer = ByteBuffer.allocateDirect(buf.length
                * 4).order(ByteOrder.nativeOrder()).asIntBuffer();
        GLES20.glTexImage2D(GLES20.GL_TEXTURE_2D, 0, GLES20.GL_RGBA, mWidth, mHeight, 0, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, texBuffer);

        GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, GLES20.GL_COLOR_ATTACHMENT0, GLES20.GL_TEXTURE_2D, mColorTexture, 0);
        int state = GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER);
        if ( state != GLES20.GL_FRAMEBUFFER_COMPLETE) {
            switch (state) {
                case GLES20.GL_FRAMEBUFFER_UNSUPPORTED:
                    throw new RuntimeException("unsupported");
                case GLES20.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT:
                    throw new RuntimeException("GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT");
            }
            throw new RuntimeException("FrameBuffer not complete");
        }
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public int getFBO() {
        return mFbo;
    }

    public int getColorTexture() {
        return mColorTexture;
    }

    public int getDepthTexture() {
        // do not use
        if (true) {
            throw new RuntimeException("FrameBuffer create error");
        }
        return mDepthTexture;
    }

    public void bind() {
        GLES20.glViewport(0, 0, mWidth, mHeight);
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, mFbo);
    }

    public void unbind() {
        GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
    }

    public void destory() {
        GLES20.glDeleteFramebuffers(1, new int[]{mFbo}, 0);
        GLES20.glDeleteTextures(1, new int[]{mColorTexture}, 0);
    }
}
