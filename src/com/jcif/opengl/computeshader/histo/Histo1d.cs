#define GROUP_COUNT 4096
#define GROUP_SIZE 512
#define GRID_SIZE   (GROUP_COUNT*GROUP_SIZE)
layout(local_size_x=GROUP_SIZE) in;  

const int globalId=int(gl_GlobalInvocationID.x);   
    
    
layout(std430, binding = 0) volatile coherent buffer SSBO_0 {	
	int counts[];
};


layout(std430, binding = 1) restrict readonly buffer SSBO_1 {	
	uint bitmask[];
};

layout(std430, binding = 2) restrict readonly buffer SSBO_2 {	
	 float values[];
};



layout(location=0) uniform ivec2 UNI_0;
const int pulseCount = UNI_0.x;         
const int binCount  = UNI_0.y; 
layout(location=1) uniform vec2 UNI_1;
const float factor= UNI_1.x;        
const float increment = UNI_1.y; 
     



                                     
void main(void)                      
{                                           
const int lastBin= binCount-1;                
for(int id=globalId;id<pulseCount;id+=GRID_SIZE)   
{                                                
if((bitmask[id])!=0)             
{                                              
const float value=values[id];                  
const float pos=fma(value,factor,increment);   
const int bin=clamp(int(floor(pos)),0,lastBin);
atomicAdd(counts[bin],1);

                    
}

                                             
}                                                
}                                         