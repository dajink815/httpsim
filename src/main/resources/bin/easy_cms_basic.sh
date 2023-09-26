#!/bin/bash
HOME=/home/uhttpsim/uhttpsim

PATH_TO_JAR=$HOME/lib/httpsim-jar-with-dependencies.jar
JAVA_OPT="-Dlogback.configurationFile=$HOME/config/logback.xml"

# Scenario
SCENARIO_PATH=$HOME/scenario
SCENARIO_FILE=$SCENARIO_PATH/easy_cms_basic.xml

# HTTP Configuration
HTTP_HOST=192.168.5.224
HTTP_PORT=8080

# Performance
THREAD_SIZE=3
MAX_TRANSACTION=10

java $JAVA_OPT $DEBUG -classpath $PATH_TO_JAR com.uangel.sim.HttpSimMain -sf $SCENARIO_FILE -hh $HTTP_HOST -hp $HTTP_PORT -ts $THREAD_SIZE -mt $MAX_TRANSACTION 2>> stderr