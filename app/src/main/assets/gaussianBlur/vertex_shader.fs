attribute vec4 a_position;
attribute vec2 a_texCoord;
varying vec2 outTexCoords;

void main()
{
    outTexCoords = a_texCoord;
    gl_Position = a_position;
}