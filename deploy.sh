rm ../jingnan_web-1.0-SNAPSHOT.jar
mvn clean
mvn package -Dmaven.test.skip=true
mv ./jingnan_web/target/jingnan_web-1.0-SNAPSHOT.jar ../
