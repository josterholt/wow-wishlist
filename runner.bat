mvn package
java -Dorg.eclipse.jetty.LEVEL=DEBUG -jar war\dependency\jetty-runner.jar war\*.war