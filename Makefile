build:
	mvn install

run: build
	java -jar ./target/entersekt-dir-1.0-SNAPSHOT-jar-with-dependencies.jar

docker-build: build
	docker build -t enterdir .

docker-run: docker-build
	docker run -p 8080:4567 enterdir

