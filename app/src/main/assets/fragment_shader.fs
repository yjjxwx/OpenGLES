precision mediump float;
const float ZERO = 0.0;
const float ONE = 1.0;
const float TWO = 2.0;
const float FOUR = 4.0;

const int ZERO_I = 0;
const int ONE_I = 1;
const int THREE_I = 3;
const int SIXTEEN = 16;
const int FOUR_I = 4;
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
// action = 0, x blur
// action = 1, y blur
// action = 2, scale
uniform int action;

varying vec2 outTexCoords;
const float SIGMA = 0.25;
float getGaussianWeight(float x) {
    return ONE/sqrt(TWO*M_PI*SIGMA*SIGMA) * exp(-(x*x)/(TWO*SIGMA*SIGMA));
}

// Calculate the weight.
// Note that len must less 16
mat4 getWeights(int len) {
    mat4 result = mat4(1.0);
    float sum = 0.0;
    for(int x = 0; x < len; x++) {
        float value = getGaussianWeight(float(x));
        result[x/4][int(mod(float(x),4.0))] = value;
        sum += value;
    }
    for(int x = 0; x < len; x++) {
        result[x/4][int(mod(float(x),4.0))] = result[x/4][int(mod(float(x),4.0))]/(sum - result[0][0] + sum);
    }
    return result;
}

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

void main()
{
    if (control == ONE_I) {
        if (action == ZERO_I || action == ONE_I) {
            vec4 sum = vec4(ZERO);
            sum += texture2D(blurSource, outTexCoords) * weights1[0][0];
            int len = samplerRadius;
            float sv = 0.0;
            for(int i = 1; i < len; i++) {
                if (action == 0) { // x direction blur
                    sum += texture2D(blurSource, getUV(outTexCoords.x + samplerStepX1[i/4][int(mod(float(i),4.0))] , outTexCoords.y)) * weights1[i/4][int(mod(float(i),4.0))];
                    sum += texture2D(blurSource, getUV(outTexCoords.x - samplerStepX1[i/4][int(mod(float(i),4.0))], outTexCoords.y)) * weights1[i/4][int(mod(float(i),4.0))];
                } else if (action == 1){ // y direction blur
                    sum += texture2D(blurSource, getUV(outTexCoords.x, outTexCoords.y + samplerStepY1[i/4][int(mod(float(i),4.0))])) * weights1[i/4][int(mod(float(i),4.0))];
                    sum += texture2D(blurSource, getUV(outTexCoords.x, outTexCoords.y - samplerStepY1[i/4][int(mod(float(i),4.0))])) * weights1[i/4][int(mod(float(i),4.0))];
                }
            }
            if (samplerRadius > 16) {
                len = samplerRadius - 16;
                for(int i = 0; i < len; i++) {
                    if (action == 0) { // x direction blur
                        sum += texture2D(blurSource, getUV(outTexCoords.x + samplerStepX2[i/4][int(mod(float(i),4.0))] , outTexCoords.y)) * weights2[i/4][int(mod(float(i),4.0))];
                        sum += texture2D(blurSource, getUV(outTexCoords.x - samplerStepX2[i/4][int(mod(float(i),4.0))], outTexCoords.y)) * weights2[i/4][int(mod(float(i),4.0))];
                    } else if (action == 1){ // y direction blur
                        sum += texture2D(blurSource, getUV(outTexCoords.x, outTexCoords.y + samplerStepY2[i/4][int(mod(float(i),4.0))])) * weights2[i/4][int(mod(float(i),4.0))];
                        sum += texture2D(blurSource, getUV(outTexCoords.x, outTexCoords.y - samplerStepY2[i/4][int(mod(float(i),4.0))])) * weights2[i/4][int(mod(float(i),4.0))];
                    }
                }
            }
            //gl_FragColor = sum / float(samplerRadius/2);
            gl_FragColor = sum;
        } else if (action == 2){
            gl_FragColor = texture2D(blurSource, outTexCoords);
        } else if (action == 3) {
            gl_FragColor = texture2D(blurSource, outTexCoords);
        }
    } else {
        gl_FragColor = texture2D(blurSource, outTexCoords);
    }
}