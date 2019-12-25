#!/usr/bin/env bash
export WORK_PATH="/opt/athena-livu/apps"

function run() {

    JAVA_CMD="java"

    if [ $PROFILE == "local" ] || [ $PROFILE == "test" ] || [ $PROFILE == "staging" ];then
       JVM_DEBUG="-Xdebug -Xrunjdwp:transport=dt_socket,suspend=n,server=y,address=8411"
    else
       JVM_DEBUG=""
    fi

    EXEC="exec"
    CONTEXT=/

    JVM_ARGS="-server -Dfile.encoding=UTF-8 -Dsun.jnu.encoding=UTF-8 -Djava.io.tmpdir=/tmp -Djava.net.preferIPv6Addresses=false"
    JVM_HEAP="-XX:MetaspaceSize=256M -XX:MaxMetaspaceSize=256M -XX:+AlwaysPreTouch -XX:ReservedCodeCacheSize=128m -XX:InitialCodeCacheSize=128m -XX:+HeapDumpOnOutOfMemoryError -XX:+HeapDumpBeforeFullGC -XX:HeapDumpPath=/opt/athena-livu/logs/fullgc.dump"
    JVM_SIZE="-Xmx6g -Xms6g"
    JVM_GC="-XX:+UseG1GC -XX:G1HeapRegionSize=4M -XX:InitiatingHeapOccupancyPercent=40 -XX:MaxGCPauseMillis=100 -XX:+TieredCompilation -XX:CICompilerCount=4 -XX:-UseBiasedLocking -XX:+PrintGCDetails -XX:+PrintHeapAtGC -XX:+PrintTenuringDistribution -XX:+PrintGCTimeStamps -XX:+PrintGCDateStamps -XX:+PrintStringTableStatistics -XX:+PrintAdaptiveSizePolicy -XX:+PrintGCApplicationStoppedTime -XX:+PrintFlagsFinal -XX:-UseGCLogFileRotation -XX:NumberOfGCLogFiles=10 -XX:GCLogFileSize=10M"
    EXEC_JAVA="$EXEC $JAVA_CMD $JVM_ARGS $JVM_SIZE $JVM_HEAP $JVM_JIT $JVM_GC $JVM_DEBUG"

    if [ "$UID" = "0" ]; then
        ulimit -n 1024000
        umask 000
    else
        echo $EXEC_JAVA
    fi
    cd $WORK_PATH
    pwd
    #exec nohup java -jar athena-quickchat-userpool-server-1.0.0.jar --spring.profiles.active=test & >/dev/null 2>&1
    $EXEC_JAVA -jar ./athena-quickchat-userpool-server-1.0.0.jar >/dev/null 2>&1
}

# ------------------------------------
# actually work
# ------------------------------------
run
