pipeline {
    agent {
        docker {
            image 'maven:3.5.4-jdk-8-alpine' 
            args '-v /var/jenkins_home/.m2:/root/.m2 -v /root/.ssh:/root/.ssh' 
        }
    }
    stages {
		stage('Prepare') {
		    steps {
			sh 'apk update'
			sh 'apk add rsync openssh mariadb mariadb-client openrc git'
		    } 
		}
	
		stage('Build dependencies') {
		    steps {
			sh 'rm -rf /root/git'
			sh 'mkdir -p /root/git'
	    }
		}

		stage('Build') { 
			steps {
				sh 'cd jpa && mvn -T 1C -B -DskipTests clean install'
            }

		}
	
	    stage('Deploy') {
	       parallel {

				stage('Deploy') {
	        		steps {
	        			sh 'cd jpa/odata-jpa-processor && mvn deploy:deploy-file -Dfile=target/odata-jpa-processor-0.2.10-SNAPSHOT.jar -DpomFile=pom.xml -DrepositoryId=archiva.snapshots -Durl=http://jenkins:8081/repository/snapshots'
	   					sh 'cd jpa/odata-jpa-annotation && mvn deploy:deploy-file -Dfile=target/odata-jpa-annotation-0.2.10-SNAPSHOT.jar -DpomFile=pom.xml -DrepositoryId=archiva.snapshots -Durl=http://jenkins:8081/repository/snapshots'
	   					sh 'cd jpa/odata-jpa-metadata && mvn deploy:deploy-file -Dfile=target/odata-jpa-metadata-0.2.10-SNAPSHOT.jar -DpomFile=pom.xml -DrepositoryId=archiva.snapshots -Durl=http://jenkins:8081/repository/snapshots'
	   					sh 'cd jpa && mvn deploy:deploy-file -DpomFile=pom.xml -Dfile=pom.xml -DrepositoryId=archiva.snapshots -Durl=http://jenkins:8081/repository/snapshots'
	   				}
	   			}
	   			stage('Archive') {
	   			    steps {
	   			        archiveArtifacts artifacts: 'jpa/**/target/**/*.jar', fingerprint: true
	   			    	archiveArtifacts artifacts: '**/pom.xml', fingerprint: true
	   			    }

	   			    
	   			}

	  
	   		}	
	    }
    }
}