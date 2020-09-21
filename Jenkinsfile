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
          memory: "600Mi"
          cpu: "100m"
        limits:
          memory: "700Mi"
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
    	helm_release_name = "ms1-hystrix-client-${BRANCH_NAME}"
    	docker_image = "eu.gcr.io/loans-278211/${helm_release_name}"
        docker_image_tag = "1.0"
        ingress_domain = "client-${BRANCH_NAME}.sbx.lushlife.in"
        tls_secret_name = "k8s-tls-secret"
    }

    stages {
            stage('Package Stage') {
               steps {  // no container directive is needed as the maven container is the default
               		sh 'printenv'
                    sh "mvn clean package"   
                }
            }
        stage('Docker Image Build'){
            steps {
                container(name: 'kaniko', shell: '/busybox/sh'){
                    sh "/kaniko/executor -f `pwd`/Dockerfile -c `pwd` --insecure --skip-tls-verify --cache=true --destination=${docker_image}:${docker_image_tag}"
                }      
            }
        }
        stage('Deploy'){
            steps{
                container(name: 'helm'){
                      sh "helm upgrade --install --wait --set image.repository=${docker_image},image.tag=${docker_image_tag},ingress.hosts[0].host=${ingress_domain},ingress.hosts[0].paths[0]='/',ingress.tls[0].hosts[0]=${ingress_domain},ingress.tls[0].secretName=${tls_secret_name} ${helm_release_name} `pwd`/helm"
                }
            }
        }
    }
}
