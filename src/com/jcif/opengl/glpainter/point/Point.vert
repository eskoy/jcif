in vec2  attribute_Position; 
in vec4  attribute_Color;
uniform float pointSize;
out vec4 varying_Color;

void main(void) {
    mat4    uniform_Projection = mat4(1);

    gl_PointSize = pointSize;
  //  gl_Position = uniform_Projection * attribute_Position; 
  //  gl_Position =  uniform_Projection * vec4(attribute_Position.x, attribute_Position.y, 0, 1);
  
   gl_Position = vec4(attribute_Position.x,attribute_Position.y, 0, 1.0);
    varying_Color = attribute_Color;
}
