def giturl = "https://github.com/spring-projects/spring-petclinic.git"
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
        cpsScm {
            scm {
                git {
                    remote {
                        url("$giturl")
                    }
                    branches("*/main")
                    extensions {
                        cleanBeforeCheckout()
                    }
                }
            }
            scriptPath("Jenkinsfile")
        }
    }
} 