FROM jenkins/jenkins:latest
ENV JAVA_OPTS="-Djenkins.install.runSetupWizard=false -Dpermissive-script-security.enabled=true"
ENV CASC_JENKINS_CONFIG=/var/jenkins_casc_configs/jenkins.yaml
COPY plugins.txt /usr/share/jenkins/ref/plugins.txt
COPY jenkins.yaml /var/jenkins_casc_configs/
COPY job_dsl.groovy /var/jenkins_home/init.groovy.d/
RUN jenkins-plugin-cli -f /usr/share/jenkins/ref/plugins.txt