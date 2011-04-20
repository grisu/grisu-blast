grisu-blast
=========
 
grisu-blast is a grid client which submits blast/mpi-blast jobs the the NZ compute grid.

Supporting documentation
-------------------------------------------

For Grisu/the grisu-client library:

- [Wiki](https://github.com/grisu/grisu/wiki)
- [Javadoc](http://grisu.github.com/grisu/javadoc/)
- [SOAP/REST API documentaion](https://compute.services.bestgrid.org/)

Prerequisites
---------------------

In order to build grisu-blast from the git sources, you need: 

- Sun Java Development Kit (version ≥ 6)
- [git](http://git-scm.com) 
- [Apache Maven](http://maven.apache.org) (version >=2)


Checking out sourcecode
----------------------------------------

 `git clone git://github.com/grisu/grisu-blast.git`

Building Grisu using Maven
------------------------------------------

To build one of the above modules, cd into the module root directory of the module to build and execute: 

    cd grisu-blast
    mvn clean install


