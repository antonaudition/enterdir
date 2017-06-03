build:
	mvn clean install

docker-build:
	docker build -t enterdir .

run:
	docker run -p 8080:4567 enterdir

