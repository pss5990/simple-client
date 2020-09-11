pipeline {
    agent {
    kubernetes {
                label "simple-client"
                idleMinutes 0      //Timeout for longer running slaves
                defaultContainer maven
                yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    app: api
    build: jenkins_slave
spec:
  containers:
    - name: maven
      image: maven:3.5.4-jdk-8-slim
      command: ["tail", "-f", "/dev/null"]  # this or any command that is bascially a noop is required, this is so that you don't overwrite the entrypoint of the base container
      imagePullPolicy: Always # use cache or pull image for agent
      resources:  # limits the resources your build contaienr
        requests:
          memory: "8Gi"
          cpu: "500m"
        limits:
          memory: "8Gi"
        """
            }
        }

    stages {
            stage('Compile Stage') {
                steps {  // no container directive is needed as the maven container is the default
                    sh "mvn clean compile"   
                }
            }
            
            stage('Testing Stage') {
                steps {  // no container directive is needed as the maven container is the default
                    sh "mvn test"   
                }
            }
            stage('Compile Stage') {
                steps {  // no container directive is needed as the maven container is the default
                    sh "mvn clean package"   
                }
            }
        

    }
}
