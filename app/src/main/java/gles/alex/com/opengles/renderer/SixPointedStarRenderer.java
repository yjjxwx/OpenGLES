package gles.alex.com.opengles.renderer;

import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import gles.alex.com.opengles.util.ShaderUtil;

/**
 * Created by alex on 15-9-19.
 */
public class SixPointedStarRenderer implements GLSurfaceView.Renderer{

    protected Context mContext;
    private int mProgram;
    private String mVertexShaderSource;
    private String mFragmentShaderSource;

    public SixPointedStarRenderer(Context context) {
        mContext = context;
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        mVertexShaderSource = ShaderUtil.loadFromAssetsFile(getVertexShaderPath(), mContext.getResources());
        mFragmentShaderSource = ShaderUtil.loadFromAssetsFile(getFragmentShaderPath(), mContext.getResources());
        mProgram = ShaderUtil.createProgram(mVertexShaderSource, mFragmentShaderSource);

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        GLES20.glClearColor(0.0f, 1.0f, 0.0f, 1.0f);
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT);
        GLES20.glUseProgram(mProgram);
        ShaderUtil.checkGLError("yjjxwx", "use program");
        GLES20.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);

    }

    protected String getVertexShaderPath() {
        return "sixPointedStar/vertex_tex_x.fs";
    }
    protected String getFragmentShaderPath() {
        return "sixPointedStar/fragment_shader.fs";
    }
}
