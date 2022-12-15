build:
	mvn clean package

build-run: build
	java -jar target/kp.jar

run:
	java -jar target/kp.jar