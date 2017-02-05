# jcif
Java Computing Intensive Function



1-With data source tab, define  you  dataset like a white noise , sinus .... or house hold power consumption data.

2-Generate more than one million of record to build a large dataset.

3-Put your large dataset in a Structure Of Array.

4-Use a direct bytebuffer for each array.
At this step, you can display bytebuffer directly with opengl drawArrays. 
The rendering of arrays with a GPU is  more effective than with CPU (java2d,javafx)
Direct bytebuffer, vertex and fragment shaders are the first step of java computing intensive function.

5-To obtain the second step, of java computing intensive function you have to use GP-GPU. 
In demo i use an histogram2d compute shader to obtain a graphical reduction of my large dataset.



![alt text](https://github.com/eskoy/jcif/blob/master/doc/demo.png "Logo Title Text 1")

Works with 

modern opengl
http://jogamp.org/jogl/www/


Background with :

geojson-jackson
https://github.com/opendatalab-de/geojson-jackson.git

shapefilereader
https://sourceforge.net/projects/javashapefilere/

