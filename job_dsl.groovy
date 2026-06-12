def giturl = "https://github.com/spring-projects/spring-petclinic.git"
def email = "harry930006@gmail.com"
freeStyleJob("spring-petclinic") {
    displayName("Spring Petclinic")
    scm {
        git {
            remote {
                url("$giturl")
            }
            branch("*/main")
        }
    }
    triggers {
        scm('@daily')
    }
    logRotator {
        daysToKeep(10)
        numToKeep(20)
        artifactNumToKeep(10)
        artifactDaysToKeep(10)
    }
    steps {
        maven {
            mavenInstallation("Maven 3.8.5")
            goals("clean package")
        }
    }
    publishers {
        archiveArtifacts("target/*.jar")
        extendedEmail {
            recipientList("$email")
            defaultSubject('Oops')
            defaultContent('Something broken')  
            triggers {
                failure {
                    sendTo {
                        recipientList("$email")
                    }
                }
            }     
        }
    }
} 