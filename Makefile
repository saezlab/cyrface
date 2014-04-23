# Makefile 

# Copyright (C) 2014 - Saez-Rodriguez Lab
# Cyrface

# Author: Emanuel Goncalves <emanuel@ebi.ac.uk>

# This program is free software, you can redistribute it and/or
# modify it under the terms of the new-style BSD license.

# You should have received a copy of the BSD license along with this
# program. If not, see <http://www.debian.org/misc/bsd.license>.

CYRFACE_NAME=cyrface
CYRFACE_VERSION=2.0.0

default: mvn-build
	cp target/$(CYRFACE_NAME)-$(CYRFACE_VERSION).jar /Users/emanuel/CytoscapeConfiguration/3/apps/installed/

lsof:
	lsof -i | grep Rserve

mvn-build:
	mvn clean install

mvn-rserve:
	mvn install:install-file -DgroupId=net.rforge -DartifactId=Rserve -Dversion=1.8 -Dpackaging=jar -Dfile=$(CURDIR)/lib/RserveEngine.jar
	mvn install:install-file -DgroupId=net.rforge -DartifactId=REngine -Dversion=1.8 -Dpackaging=jar -Dfile=$(CURDIR)/lib/REngine.jar

mvn-batik:
	mvn install:install-file -DgroupId=batik -DartifactId=batik-all -Dversion=1.6 -Dpackaging=jar -Dfile=$(CURDIR)/lib/batik-all-1.6.jar

mvn-sbml:
	mvn install:install-file -DgroupId=uk.ac.ebi -DartifactId=jsbml -Dversion=1.0 -Dpackaging=jar -Dfile=$(CURDIR)/lib/jsbml-1.0-a1-with-dependencies.jar
	mvn install:install-file -DgroupId=uk.ac.ebi -DartifactId=jsbmlqual -Dversion=2.1 -Dpackaging=jar -Dfile=$(CURDIR)/lib/jsbml-qual-2.1-b1.jar

mvn-sbfc:
	mvn install:install-file -DgroupId=uk.ac.ebi -DartifactId=sbfc -Dversion=0.1 -Dpackaging=jar -Dfile=$(CURDIR)/lib/sbfc-api.jar