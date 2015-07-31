package gles.alex.com.opengles.util;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

/**
 * Created by alex on 15-7-7.
 */
public class BufferUtils {
    public static FloatBuffer getFloatBuffer(float[] datas) {
        FloatBuffer fb = ByteBuffer.allocateDirect(datas.length * 4).order(ByteOrder.nativeOrder()).asFloatBuffer();
        fb.put(datas);
        fb.position(0);
        return fb;
    }
}
