def gitCommit = ""
pipeline {
    agent any
    tools {
        maven "Maven 3.8.5"
        jdk "JDK 17"
    }
    environment {
        // Email notification settings
        EMAIL_RECIPIENTS = 'harry930006@gmail.com' // Update with your email
        
        // Artifact settings
        ARTIFACT_NAME = "spring-petclinic-${env.BUILD_NUMBER}.jar"
        DEPLOY_DIR = '/opt/spring-petclinic' // Update with your deployment directory
        // docker settings
        APP_NAME = "spring-petclinic"
        REGISTRY_URL = "harry930006" // Update with your Docker registry URL
        IMAGE_NAME = "${REGISTRY_URL}/${APP_NAME}"


    }
    parameters {
        string(name: "BRANCH", defaultValue: "", description: "Branch to build and deploy(e.g., main, test)")
    }
    stages {
        stage('Checkout 原始碼') {
            steps {
                echo '====== Checking out code from repository ======'
                checkout scm
            }
        }
        stage("maven build 編譯打包") {
            steps {
                echo '====== Building project with Maven ======'
                sh "mvn clean package" 
            }
        }
        stage("建立並推送 Docker 映像檔") {
            steps {
                script {
                        gitCommit = sh(script: 'git rev-parse --short HEAD', returnStdout: true).trim()
                    withCredentials([usernamePassword(credentialsId: 'docker-hub-credentials', usernameVariable: "DOCKER_USER", passwordVariable: "DOCKER_PASS")]) {
                        echo '=== 登入 Docker Hub ==='
                        sh 'echo "$DOCKER_PASS" | docker login -u "$DOCKER_USER" --password-stdin'
                        echo '=== 開始建立 Docker 映像檔 ==='
                        sh "docker build -t ${IMAGE_NAME}:build-${gitCommit} ."
                        echo '=== 推送 Docker 映像檔到註冊中心 ==='
                        sh "docker push ${IMAGE_NAME}:build-${gitCommit}"
                        // 3. 清理 Jenkins 本地剛剛建立的暫存映像檔，節約硬碟空間
                        sh "docker rmi ${IMAGE_NAME}:build-${gitCommit}"
                    }
                }    
            }
        }
        stage("部署到測試環境") {
            when {
                expression { params.BRANCH == 'test' }
            }
            steps {
                echo "【測試環境】通知測試伺服器汰換容器..."
                
                // 透過 SSH 隔空對測試伺服器下達 Docker 指令
                 sh """
                    ssh root@\$TEST_SERVER "

                        # 停止並刪除舊的容器（若不存在則忽略，避免錯誤中斷）
                        docker stop \${APP_NAME} || true
                        docker rm \${APP_NAME} || true
                        
                        # 從倉庫拉取剛剛 Jenkins 做好推上去的那顆精準版本 Image
                        docker pull \${IMAGE_NAME}:build-${env.gitCommit}
                        
                        # 啟動新容器：
                        # 結尾注入參數：指定為 production 環境設定
                        docker run -d --name \${APP_NAME} \\
                          -p 8086:8080 \
                          \${IMAGE_NAME}:build-\${gitCommit} --spring.profiles.active=prod
                          
                        # 清理伺服器上沒在使用的舊映像檔（標籤為 <none> 的遺留檔案）
                        docker image prune -f
                    "
                """
                echo '【測試環境】Docker 部署成功！'
            }
        }
        stage("部署到生產環境") {
            when {
                expression { params.BRANCH == 'main' }
            }
             steps {
                echo "【生產環境】通知生產伺服器汰換容器..."
                
                // 透過 SSH 隔空對生產伺服器下達 Docker 指令
                 sh """
                    ssh root@\$PROD_SERVER "

                        # 停止並刪除舊的容器（若不存在則忽略，避免錯誤中斷）
                        docker stop \${APP_NAME} || true
                        docker rm \${APP_NAME} || true
                        
                        # 從倉庫拉取剛剛 Jenkins 做好推上去的那顆精準版本 Image
                        docker pull \${IMAGE_NAME}:build-\${gitCommit}
                        
                        # 啟動新容器：
                        # 結尾注入參數：指定為 production 環境設定
                        docker run -d --name \${APP_NAME} \\
                          -p 8085:8080 \
                          \${IMAGE_NAME}:build-\${gitCommit} --spring.profiles.active=prod
                          
                        # 清理伺服器上沒在使用的舊映像檔（標籤為 <none> 的遺留檔案）
                        docker image prune -f
                    "
                """
                echo '【正式環境】Docker 部署成功！'
            }
        }
    }
    post {
    success { echo '🎉 Docker CI/CD 流程完美結束！' }
    failure { echo '❌ 流程失敗，請檢查看是 Maven 打包錯誤，還是 Docker 推送失敗。' }
    }
}