def CONTAINER_NAME = "calculator"
def ENV_NAME = getEnvName(env.BRANCH_NAME)
def CONTAINER_TAG = getTag(env.BUILD_NUMBER, env.BRANCH_NAME)
def HTTP_PORT = getHTTPPort(env.BRANCH_NAME)
def EMAIL_RECIPIENTS = "alcharef@gmail.com"

node {
    try {
        stage('Initialize') {
            def dockerHome = tool 'DockerLatest'
            def mavenHome = tool 'MavenLatest'
            env.PATH = "${dockerHome}/bin:${mavenHome}/bin:${env.PATH}"
        }

        stage('Checkout') {
            checkout scm // Récupère le code source
        }

        stage('Build with test') {
            // Effectue le build et exécute les tests
            sh "mvn clean install"
        }

        stage('Sonarqube Analysis') {
            withSonarQubeEnv('SonarQubeLocalServer') {
                sh "mvn sonar:sonar -Dintegration-tests.skip=true -Dmaven.test.failure.ignore=true"
            }
            timeout(time: 2, unit: 'MINUTES') { // Augmentation du timeout si nécessaire
                def qg = waitForQualityGate()
                if (qg.status != 'OK') {
                    error "Pipeline aborted due to quality gate failure: ${qg.status}"
                }
            }
        }

        stage("Image Prune") {
            imagePrune(CONTAINER_NAME) // Supprime les images non utilisées
        }

        stage('Image Build') {
            imageBuild(CONTAINER_NAME, CONTAINER_TAG) // Crée l'image Docker
        }

        stage('Push to Docker Registry') {
            withCredentials([usernamePassword(credentialsId: 'DockerhubCredentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                pushToImage(CONTAINER_NAME, CONTAINER_TAG, USERNAME, PASSWORD)
            }
        }

        stage('Run App') {
            withCredentials([usernamePassword(credentialsId: 'DockerhubCredentials', usernameVariable: 'USERNAME', passwordVariable: 'PASSWORD')]) {
                runApp(CONTAINER_NAME, CONTAINER_TAG, USERNAME, HTTP_PORT, ENV_NAME)
            }
        }

    } finally {
        deleteDir() // Nettoie l'environnement de travail
        sendEmail(EMAIL_RECIPIENTS) // Envoie un email avec le résultat du build
    }
}

// Fonction pour supprimer les images inutilisées
def imagePrune(containerName) {
    try {
        sh "docker image prune -f"
        sh "docker stop $containerName"
    } catch (ignored) {
        // Ignorer les erreurs de suppression
    }
}

// Fonction pour construire l'image Docker
def imageBuild(containerName, tag) {
    sh "docker build -t $containerName:$tag -t $containerName --pull --no-cache ."
    echo "Image build complete"
}

// Fonction pour pousser l'image Docker dans le registre
def pushToImage(containerName, tag, dockerUser, dockerPassword) {
    sh "echo $dockerPassword | docker login -u $dockerUser --password-stdin"
    sh "docker tag $containerName:$tag $dockerUser/$containerName:$tag"
    sh "docker push $dockerUser/$containerName:$tag"
    echo "Image push complete"
}

// Fonction pour exécuter l'application Docker
def runApp(containerName, tag, dockerHubUser, httpPort, envName) {
    sh "docker pull $dockerHubUser/$containerName"
    sh "docker run --rm --env SPRING_ACTIVE_PROFILES=$envName -d -p $httpPort:$httpPort --name $containerName $dockerHubUser/$containerName:$tag"
    echo "Application started on port: ${httpPort} (http)"
}

// Fonction pour envoyer un email
def sendEmail(recipients) {
    mail(
        to: recipients,
        subject: "Build ${env.BUILD_NUMBER} - ${currentBuild.currentResult} - (${currentBuild.fullDisplayName})",
        body: "Check console output at: ${env.BUILD_URL}/console" + "\n"
    )
}

// Fonction pour déterminer le nom de l'environnement en fonction de la branche
String getEnvName(String branchName) {
    if (branchName == 'main') {
        return 'prod'
    } else if (branchName.startsWith("release-") || branchName.startsWith("hotfix-") || branchName == 'ready') {
        return 'uat'
    }
    return 'dev'
}

// Fonction pour obtenir le port HTTP en fonction de la branche
String getHTTPPort(String branchName) {
    if (branchName == 'main') {
        return '9001'
    } else if (branchName.startsWith("release-") || branchName.startsWith("hotfix-") || branchName == 'ready') {
        return '9002'
    }
    return '9003'
}

// Fonction pour générer une balise en fonction du numéro de build et de la branche
String getTag(String buildNumber, String branchName) {
    if (branchName == 'main') {
        return buildNumber + '-unstable'
    }
    return buildNumber + '-stable'
}
