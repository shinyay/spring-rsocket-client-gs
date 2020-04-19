#!/usr/bin/env fish
## https://docs.spring.io/initializr/docs/current/reference/html/#initializr-documentation

set -g GROUP_ID com.google.shinyay
set -g ARTIFACT_ID spring-boot-demo
set -g NAME $ARTIFACT_ID
set -g DESCRIPTION "Sample project for Spring Boot"
set -g PACKAGE $GROUP_ID
set -g DEPENDENCY web,actuator,thymeleaf

curl https://start.spring.io/starter.zip --create-dirs -o ./spring/spring.zip \
#        -d bootVersion=2.3.0.M4 \
	-d type=gradle-project \
	-d language=kotlin \
	-d groupId=$GROUP_ID \
	-d artifactId=$ARTIFACT_ID \
	-d name=$NAME \
	-d description=$DESCRIPTION \
	-d packageName=$PACKAGE \
	-d packaging=jar \
	-d javaVersion=11 \
	-d dependencies=$DEPENDENCY
