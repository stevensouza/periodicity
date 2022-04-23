# App that let's you play with periodicity by default where each pixel is 1 hour of the day and 365 days are displayed (24*365)

To run: 
java -jar target/periodicity-0.0.1-SNAPSHOT.jar
   
   or

mvn spring-boot:run   

This will pull up a command shell and from here you can execute commands.

Then you can enter 'help', 'help <command>'. The main command is 'run' (which also can be invoked with an 'r').  To see a default example just type 'run' followed by a return and an image will display.  Look at the various *.txt files for further examples or type 'fh' (further help) for more information about the program.

![Example1](https://github.com/stevensouza/periodicity/blob/master/24hourmod_calendar1.png)

Other 'run' commands generated the following images.

![Example2](https://github.com/stevensouza/periodicity/blob/master/periodicity1.png)
![Example3](https://github.com/stevensouza/periodicity/blob/master/24hourmod_calendar_neighbors1.png)

It also comes with another class called Paints. Paints is a standalone class that displays 
surprisingly complex images by altering the arguments.  For example:

java -classpath .  com.stevesouza.surpriseimages.Paints 0
![Example4](https://github.com/stevensouza/periodicity/blob/master/0.png)

java -classpath .  com.stevesouza.surpriseimages.Paints 1
![Example5](https://github.com/stevensouza/periodicity/blob/master/1.png)

You can also simply run the folloiwng and it will show some other possibilities

java -classpath .  com.stevesouza.surpriseimages.Paints 


