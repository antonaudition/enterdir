# Entersekt Restful ls

A small little cross platform program in Java 8 that exposes a RESTful interface on port
8080 and allows a client application to do only one thing: obtain the full directory listing of a
given directory path on the local filesystem. Include the full path, file size and attribute
information in the result and cater for a directory size of at least 1 000 000. 

## Getting Started

After cloning the project the easiest way to get started is by running the make commands in the 
root of the project

### Prerequisites

The following tools are required to build and run this project

```
jdk 8
maven
docker
make
```

### Installing

The installation steps are contained in a `Makefile` and give the following options 
(if `make` is unavailable the commands in the Makefile should be run)

```
make build (builds the java source using maven)
make run (runs the jar and exposes the local file system on localhost:8080)
make docker-build (creates a docker image called enterdir)
make docker-run (runs the docker and exposes the docker file system on localhost:8080)
```

## Usage

The app exposes the following four endpoints on `localhost:8080`

```
/alive
/stat
/ls
/tree

```

..etc