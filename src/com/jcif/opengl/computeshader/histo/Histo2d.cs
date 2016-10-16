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
	 float valuesA[];
};

layout(std430, binding = 3) restrict readonly buffer SSBO_3 {	
	 float valuesB[];
};

layout(location=0) uniform ivec3 UNI_0;
const int pulseCount = UNI_0.x;         
const ivec2 binCount  = UNI_0.yz; 
layout(location=1) uniform vec4 UNI_1;
const vec2 factor= UNI_1.xy;        
const vec2 increment = UNI_1.zw; 
     

                               
void main(void)                      
{
const ivec2 firstBin=ivec2(0);                                            
const ivec2 lastBin=binCount-ivec2(1);               
for(int id=globalId;id<pulseCount;id+=GRID_SIZE)   
{                                                
if((bitmask[id])!=0)             
{                               
const vec2 value=vec2(valuesA[id],valuesB[id]);                     
const vec2 pos=fma(value,factor,increment);                
const ivec2 bin=clamp(ivec2(floor(pos)),firstBin,lastBin); 
const int binId=bin.y*binCount.x+bin.x;                    
atomicAdd(counts[binId],1);                                                          
}

                                             
}                                                
}                                         