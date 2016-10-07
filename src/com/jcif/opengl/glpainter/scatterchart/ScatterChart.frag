#version 430 core

varying vec4 varying_Color;

void main (void) {
   // float alpha = 1 - smoothstep(0, 1, 2 * distance(gl_PointCoord.st, vec2(0.5, 0.5)));

   // gl_FragColor = vec4(varying_Color.rgb, varying_Color.a * alpha);
   
   
    gl_FragColor =varying_Color;

}


