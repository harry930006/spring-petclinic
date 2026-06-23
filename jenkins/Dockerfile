FROM jenkins/jenkins:latest

# 切換到 root 身份安裝 Docker CLI
USER root

# 💡 超精簡安裝法：直接安裝 Debian 內建維護的 docker.io 套件（自帶 docker 指令）
RUN apt-get update && apt-get install -y docker.io

# 2. 您原本的自動化與環境變數設定
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false -Dpermissive-script-security.enabled=true"
ENV CASC_JENKINS_CONFIG=/var/jenkins_casc_configs/jenkins.yaml

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY jenkins.yaml /var/jenkins_casc_configs/
COPY job_dsl.groovy /var/jenkins_casc_configs/

# 3. 執行外掛安裝
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
