FROM jenkins/jenkins:latest

# 切換到 root 身份安裝 Docker CLI
USER root

# 💡 超精簡安裝法：直接安裝 Debian 內建維護的 docker.io 套件（自帶 docker 指令）
RUN apt-get update && apt-get install -y docker.io

# 👇 關鍵防護：設定自動生成金鑰，並在容器內自動關閉首次連線的 (yes/no) 提問
RUN mkdir -p /root/.ssh && \
    chmod 700 /root/.ssh && \
    echo "Host *\n\tStrictHostKeyChecking no\n" >> /root/.ssh/config

# 💡 使用 Jenkins 官方映像檔提供的入口腳本機制，在容器「啟動時」檢查並自動生成金鑰
# 這樣一來，金鑰一旦生成，就會透過共享硬碟立刻同步給 Ubuntu 容器！
RUN echo '#!/bin/bash\nif [ ! -f /root/.ssh/id_rsa ]; then\n  ssh-keygen -t rsa -b 4096 -f /root/.ssh/id_rsa -N ""\nfi\nexec /usr/local/bin/jenkins.sh' > /usr/local/bin/jenkins-entrypoint.sh && \
    chmod +x /usr/local/bin/jenkins-entrypoint.sh

# 覆蓋原本的 Entrypoint，改跑我們的自動生成腳本
ENTRYPOINT ["/usr/local/bin/jenkins-entrypoint.sh"]
# 2. 您原本的自動化與環境變數設定
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false -Dpermissive-script-security.enabled=true"
ENV CASC_JENKINS_CONFIG=/var/jenkins_casc_configs/jenkins.yaml

COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY jenkins.yaml /var/jenkins_casc_configs/
COPY job_dsl.groovy /var/jenkins_casc_configs/

# 3. 執行外掛安裝
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt
