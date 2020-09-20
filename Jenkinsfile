pipeline {
    agent {
    kubernetes {
                label "simple-client-${UUID.randomUUID().toString()}"
                idleMinutes 0      //Timeout for longer running slaves
                defaultContainer 'maven'
                yaml """
apiVersion: v1
kind: Pod
metadata:
  labels:
    app: api
    build: jenkins_slave
spec:
  serviceAccountName: helm-sa
  containers:
    - name: maven
      image: maven:3.6.3-jdk-8-slim
      command: ["tail", "-f", "/dev/null"]  # this or any command that is bascially a noop is required, this is so that you don't overwrite the entrypoint of the base container
      imagePullPolicy: Always # use cache or pull image for agent
      resources:  # limits the resources your build contaienr
        requests:
          memory: "300Mi"
          cpu: "100m"
        limits:
          memory: "500Mi"
          cpu: "500m"
    - name: kaniko
      image: gcr.io/kaniko-project/executor:debug
      command:
      - /busybox/cat
      tty: true
      volumeMounts:
      - name: kaniko-secret
        mountPath: /secret
      resources:  # limits the resources your build contaienr
        requests:
          memory: "200Mi"
          cpu: "100m"
        limits:
          memory: "500Mi"
          cpu: "500m"
      env:
      - name: GOOGLE_APPLICATION_CREDENTIALS
        value: /secret/kaniko-secret.json
    - name: helm
      image: lachlanevenson/k8s-helm:v3.1.1
      command:
      - cat
      tty: true
      resources:  # limits the resources your build contaienr
        requests:
          memory: "200Mi"
          cpu: "100m"
        limits:
          memory: "500Mi"
          cpu: "500m"
  volumes:
  - name: kaniko-secret
    secret:
      secretName: kaniko-secret
        """
            }
        }
    environment{
        helm_release_name = 'ms1-simple-client'
    }

    stages {
            stage('Package Stage') {
               steps {  // no container directive is needed as the maven container is the default
                    sh "mvn clean package"   
                }
            }
        stage('Docker Image Build'){
            steps {
                container(name: 'kaniko', shell: '/busybox/sh'){
                    sh '/kaniko/executor -f `pwd`/Dockerfile -c `pwd` --insecure --skip-tls-verify --cache=true --destination=eu.gcr.io/loans-278211/my-image:master'
                }      
            }
        }
        stage('Deploy'){
            steps{
                container(name: 'helm'){
                    sh 'helm version'
                      sh 'helm upgrade --install --wait --set image.repository=eu.gcr.io/loans-278211/my-image,image.tag=master ${helm_release_name} `pwd`/helm'
                }
            }
        }
    }
}
