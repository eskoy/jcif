in vec2  attribute_Position; 

uniform vec4 color;

out vec4 varying_Color;


void main(void) {
    gl_PointSize = 10f;
   gl_Position = vec4(attribute_Position.x,attribute_Position.y, 0, 1.0);
    varying_Color = color;
}
