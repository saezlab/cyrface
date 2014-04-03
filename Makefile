# Makefile 

# Copyright (C) 2014 - Saez-Rodriguez Lab
# Cyrface 3

# Author: Emanuel Goncalves <emanuel@ebi.ac.uk>

# This program is free software, you can redistribute it and/or
# modify it under the terms of the new-style BSD license.

# You should have received a copy of the BSD license along with this
# program. If not, see <http://www.debian.org/misc/bsd.license>.

CYRFACE_NAME=cyrface
CYRFACE_VERSION=2.0.0

default: mvn-build
	mv target/$(CYRFACE_NAME)-$(CYRFACE_VERSION).jar /Users/emanuel/CytoscapeConfiguration/3/apps/installed/

mvn-build:
	mvn clean install

mvn-rserve:
	mvn install:install-file -DgroupId=net.rforge -DartifactId=Rserve -Dversion=1.8 -Dpackaging=jar -Dfile=$(CURDIR)/lib/RserveEngine.jar
	mvn install:install-file -DgroupId=net.rforge -DartifactId=REngine -Dversion=1.8 -Dpackaging=jar -Dfile=$(CURDIR)/lib/REngine.jar