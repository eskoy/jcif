in vec3  attribute_Position; 

uniform mat4 viewMatrix, projMatrix;

uniform vec4 color;

out vec4 varying_Color;


void main(void) {
    gl_PointSize = 5f;
  gl_Position = projMatrix * viewMatrix *  vec4(attribute_Position.x,attribute_Position.y, attribute_Position.z, 1.0);
    varying_Color = color;
}
