#!/usr/bin/env bash
mvn clean -U package -P $PROFILE  -Dmaven.test.skip=$SKIP_TEST