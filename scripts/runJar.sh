#!/bin/bash

# Script for running application from assembly. Filter values will put in build artifact name.
java -jar ../lib/${project.artifactId}-${project.version}.${project.packaging} -i $1
