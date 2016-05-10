# StationXML-Validation

A utility to validate station xml documents against a set of rules, look https://seiscode.iris.washington.edu/projects/stationxml-validator/wiki for more information.

java -jar validation-client.jar station.xml

to build:

1. download

2. cd to directory StationXML-Validation

3. mvn install


4. cd validation-client

5. mvn clean assembly:assembly you can choose to skip testing with: -Dmaven.test.skip=true

6. your jar is under the target directory: validation-client-1.0.1-jar-with-dependencies.jar

<<<<<<< HEAD


=======
2. cd StationXML-Validation

3. mvn install

4. cd validation-client

5. mvn clean assembly:assembly you can choose to skip testing with: -Dmaven.test.skip=true

6. cd target
 
7. cp validation-client-1.0.1-jar-with-dependencies.jar
>>>>>>> 547b4c30936ec423b74aa4b59154b1776452ecae
