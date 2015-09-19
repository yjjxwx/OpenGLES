precision mediump float;
const float ZERO = 0.0;
const float ONE = 1.0;
const float TWO = 2.0;
const float FOUR = 4.0;

const int ZERO_I = 0;
const int ONE_I = 1;
const int TWO_I = 2;
const int THREE_I = 3;
const int FOUR_I = 4;
const int SIXTEEN_I = 16;
const float M_PI = 3.14159265359;

uniform sampler2D blurSource;
uniform float screen_width;
uniform float screen_height;
uniform int control;
uniform int samplerRadius;
uniform mat4 weights1;
uniform mat4 weights2;
uniform mat4 samplerStepX1;
uniform mat4 samplerStepX2;
uniform mat4 samplerStepY1;
uniform mat4 samplerStepY2;
uniform float alpha;
// action = 0, x blur
// action = 1, y blur
// action = 2, scale
uniform int action;
varying vec2 outTexCoords;

const mat4 offset = mat4(0.0, 1.0, 2.0, 3.0,
                            4.0, 5.0, 6.0, 7.0,
                            8.0, 9.0, 10.0, 11.0,
                            12.0, 13.0, 14.0, 15.0);
const mat4 weight = mat4(0.2270270270, 0.1945945946, 0.1216216216, 0.0540540541,
                            0.0162162162, 0.0, 0.0, 0.0,
                            0.0, 0.0, 0.0, 0.0,
                            0.0, 0.0, 0.0, 0.0);

vec2 getUV(float u, float v) {
    if (u < ZERO) {
        u = fract(ZERO - u);
    } else if (u > ONE) {
        u = fract(TWO - u);
    }
    if (v < ZERO) {
        v = fract(ZERO - v);
    } else if (v > ONE) {
        v = fract(TWO - v);
    }

    return vec2(u,v);
}

vec2 getUV(vec2 v) {
    return getUV(v.x, v.y);
}

void main(void)
{

    if (control == ONE_I) {
        if (action == ZERO_I || action == ONE_I) {

            vec4 sum = vec4(ZERO);
            sum += (texture2D(blurSource, outTexCoords) * weights1[ZERO_I][ZERO_I]);
            int len = samplerRadius;
            if (samplerRadius > SIXTEEN_I) {
                len = SIXTEEN_I;
            }
            for(int i = ONE_I; i < len; i++) {

                if (action == ZERO_I) { // x direction blur
                    vec4 sv = samplerStepX1[i/FOUR_I];
                    float stemp = sv[int(mod(float(i),FOUR))];
                    vec4 wv = weights1[i/FOUR_I];
                    float wtemp = wv[int(mod(float(i),FOUR))];
                    sum += (texture2D(blurSource, getUV(outTexCoords.x + stemp, outTexCoords.y))) * wtemp;
                    sum += (texture2D(blurSource, getUV(outTexCoords.x - stemp, outTexCoords.y))) * wtemp;
                } else if (action == ONE_I){ // y direction blur
                    vec4 sv = samplerStepY1[i/FOUR_I];
                    float stemp = sv[int(mod(float(i),FOUR))];
                    vec4 wv = weights1[i/FOUR_I];
                    float wtemp = wv[int(mod(float(i),FOUR))];
                    sum += (texture2D(blurSource, getUV(outTexCoords.x, outTexCoords.y + stemp))) * wtemp;
                    sum += (texture2D(blurSource, getUV(outTexCoords.x, outTexCoords.y - stemp))) * wtemp;
                }
            }
            if (samplerRadius > SIXTEEN_I) {
                len = samplerRadius - SIXTEEN_I;
                for(int i = ZERO_I; i < len; i++) {
                    if (action == ZERO_I) { // x direction blur
                        vec4 sv = samplerStepX2[i/FOUR_I];
                        float stemp = sv[int(mod(float(i),FOUR))];
                        vec4 wv = weights2[i/FOUR_I];
                        float wtemp = wv[int(mod(float(i),FOUR))];
                        sum += (texture2D(blurSource, getUV(outTexCoords.x + stemp , outTexCoords.y))) * wtemp;
                        sum += (texture2D(blurSource, getUV(outTexCoords.x - stemp, outTexCoords.y))) * wtemp;
                    } else if (action == ONE_I){ // y direction blur
                        vec4 sv = samplerStepY2[i/FOUR_I];
                        float stemp = sv[int(mod(float(i),FOUR))];
                        vec4 wv = weights2[i/FOUR_I];
                        float wtemp = wv[int(mod(float(i),FOUR))];
                        sum += (texture2D(blurSource, getUV(outTexCoords.x, outTexCoords.y + stemp))) * wtemp;
                        sum += (texture2D(blurSource, getUV(outTexCoords.x, outTexCoords.y - stemp))) * wtemp;
                    }
                }
            }
            sum.a *= alpha;
            gl_FragColor = sum;
        } else if (action == TWO_I){
            gl_FragColor = texture2D(blurSource, outTexCoords);
        } else if (action == THREE_I) {
            gl_FragColor = texture2D(blurSource, outTexCoords);
        }
    } else {
        gl_FragColor = texture2D(blurSource, outTexCoords);
    }
}