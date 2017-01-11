# Subway-Map

## overview
this application, written in java, loads a map of the subway system in nyc, and relatively accurately marks the estimated location of all active subway trains on the map (currently only lines 1, 2 and 3 are functioning, but the other lines are almost finished). the locations are determined by pre existing mta records of scheduled arrival and departure times, which were sorted, formatted and improved in python. in addition, the application requires internet access in order to access the mta website to fetch registered delays and scheduled changes, which also factor into its positioning and timing of subways. in addition, clicking on individual subways will activate a hud that shows that subways estimated arrival times for all upcoming stations. the application uses the systems time, but there is a slider at the bottom that allows the user to input a preferred time.

## images
this is a screenshot of the subway map. the small blue circles on the red subway lines are graphical depictions of where physical subways are estimated to be.
 ![Alt Text](https://s3-us-west-2.amazonaws.com/stefanblairpersonalsite/Java/Screen+Shot+2015-08-17+at+8.53.52+PM.png)
