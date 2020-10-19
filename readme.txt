WATER PIPELINE SYSTEM:

This application was created to answer the following questions:
1) Does the route between two points (id-A and id-B) exist?
2) If the answer is yes, then calculate the minimal route length between id-A and id-B. 

 We use three parameters to describe wateer pipeline system: starting point id,
endpoint id, and pipe length. 

Description:
1) The program load two csv files.The first describes the water pipeline
system, this program also load it into H2 database. The second keeps a set of points, between which we need to find the route.
2) After that, the program finds routes between points, using dijkstra's algorithm, and saves a file with the results.
3) Then shows application`s UI, based on JavaFX, where we see the results of the program. Also we can add to the csv files datas,
using this interface.