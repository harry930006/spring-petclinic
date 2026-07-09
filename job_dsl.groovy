def giturl = "https://github.com/harry930006/spring-petclinic.git"
def email = "harry930006@gmail.com"
pipelineJob("spring-petclinic") {
    displayName("CICD Pipeline for Spring Petclinic")
    properties {
        githubProjectUrl("$giturl")
    }

    triggers {
        scm('@daily')
        // GitHub webhook trigger
        githubPush()
    }
    logRotator {
        daysToKeep(10)
        numToKeep(20)
        artifactNumToKeep(10)
        artifactDaysToKeep(10)
    }
    definition {
        cps {
            // 💡 移除 readFileFromWorkspace，改用 new File().text
            script(new File('/var/jenkins_casc_configs/Jenkinsfile').text)
            sandbox(true)
        }
    }
} 