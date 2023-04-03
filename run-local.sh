#!/usr/bin/env bash

#run stream server
JAVAENV="-Dlog4j.configurationFile=log4j2.properties"

java $JAVAENV -jar target/iva-test-proj-1.0-SNAPSHOT-jar-with-dependencies.jar
