layout (location=0) in  vec4 inVertex;
uniform vec4 color;


out vec4 varying_Color;
void main(void)
{
    gl_Position =   inVertex;
 
    varying_Color = color;
}