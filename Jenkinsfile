#!/usr/bin/env groovy
@Library(['piper-lib'])

def dockerImage = 'piper-docker-maven'

stage( 'test' ) {
    node {
        checkout scm
        executeDocker( dockerImage: dockerImage) {
            sh "mvn test"
        }
    }
}