# Dépôt des sources JAVA pour cours CICD 

Docker Monitoring & CI/CD avec Jenkins et SonarQube
Ce guide explique comment configurer SonarQube pour un projet Java, Dockeriser une application Spring Boot et l’intégrer à Jenkins pour CI/CD.

Partie 1 : Configuration de SonarQube pour un projet Java
Conditions préalables
Docker installé
Maven installé
Étape 1 : Cloner le projet Java
bash

Copier le code
git clone https://github.com/Zerofiltre-Courses/cicd-testing-java-cours
Étape 2 : Corriger les dépendances dans pom.xml
Assurez-vous de mettre à jour la dépendance suivante :

xml

Copier le code
<dependency>
    <groupId>io.github.bonigarcia</groupId>
    <artifactId>webdrivermanager</artifactId>
    <version>5.8.0</version>
</dependency>
Étape 3 : Tirez et exécutez SonarQube
bash

Copier le code
docker pull sonarqube:9.9.1-community
docker run --name sonarqube -e SONAR_ES_BOOTSTRAP_CHECKS_DISABLE=true -p 9000:9000 sonarqube:9.9.1-community
Étape 4 : Générer un jeton dans le tableau de bord SonarQube
Accédez à Sécurité > utilisateurs > jetons.
Générez et copiez le jeton.
Étape 5 : Configurer le settings.xml
Insérez l’extrait de code suivant dans pour utiliser le jeton SonarQube :.m2/settings.xml

xml

Copier le code
<settings>
  <pluginGroups>
    <pluginGroup>org.sonarsource.scanner.maven</pluginGroup>
  </pluginGroups>
  <profiles>
    <profile>
      <id>sonar</id>
      <activation>
        <activeByDefault>true</activeByDefault>
      </activation>
      <properties>
        <sonar.host.url>http://localhost:9000</sonar.host.url>
        <sonar.login>your_token_here</sonar.login>
      </properties>
    </profile>
  </profiles>
</settings>
Étape 6 : Exécuter l’analyse du sondeur
bash

Copier le code
mvn sonar:sonar -s .m2/settings.xml
Vous pouvez également exécuter avec le jeton :

bash

Copier le code
mvn sonar:sonar -s .m2/settings.xml -Dsonar.login=your_token_here
Étape 7 : Corrigez le plugin JaCoCo si nécessaire
Si vous rencontrez des problèmes d’exécution de tests avec Java 17/21, mettez à jour le plugin JaCoCo Maven dans votre :pom.xml

xml

Copier le code
<groupId>org.jacoco</groupId>
<artifactId>jacoco-maven-plugin</artifactId>
<version>0.8.12</version>
Étape 8 : Exécuter les tests
bash

Copier le code
mvn test
Partie 2 : Dockeriser et exécuter l’application
Étape 1 : Construire l’application sans tests
bash

Copier le code
mvn clean package -DskipTests
Étape 2 : Exécuter l’application dans le profil de développement
bash

Copier le code
java -jar -Dspring.profiles.active=dev ./target/calculator.jar
Étape 3 : Dockeriser l’application
Créez un fichier à l’aide de eclipse-temurin :21-alpine pour Java 17+ :Dockerfile

Dockerfile

Copier le code
FROM eclipse-temurin:21-alpine
# Add your Docker instructions here
Étape 4 : Vérifier le format UNIXentrypoint.sh
Utilisez Notepad++ pour convertir les fins de ligne :

Allez dans Modifier → conversion EOL → Convertir au format UNIX.
Étape 5 : Générer l’image Docker
bash

Copier le code
docker build -t calculator-light:1.0.0 .
Étape 6 : Exécuter des conteneurs Docker sur différents ports
bash

Copier le code
docker run --name calculator-lunch1 -e SPRING_ACTIVE_PROFILES=dev -p 8090:8090 calculator-light:1.0.0
docker run --name calculator-lunch2 -e SPRING_ACTIVE_PROFILES=uat -p 8091:8091 calculator-light:1.0.0
docker run --name calculator-lunch3 -e SPRING_ACTIVE_PROFILES=prod -p 8092:8092 calculator-light:1.0.0
Partie 3 : Intégration Jenkins et SonarQube
Étape 1 : Installer Jenkins
bash

Copier le code
docker pull jenkins/jenkins
docker run -u root --name jenkins-lunch1 -p 8081:8080 -p 50000:50000 -v jenkins_lunch1:/var/jenkins_home -v /var/run/docker.sock:/var/run/docker.sock jenkins/jenkins:latest
Étape 2 : Accéder au tableau de bord Jenkins
Récupérez le mot de passe administrateur à partir de la sortie du terminal.
Accédez à Jenkins et installez les outils nécessaires :
Maven : Gérez les outils Jenkins → → les installations de Maven → ajoutez .MavenLatest
Docker : Gérez les outils Jenkins → → les installations Docker → ajoutez .DockerLatest
Étape 3 : Configurer les informations d’identification
Gérer les informations d’identification Jenkins → → Ajouter des informations d’identification pour Docker, SonarQube et GitHub.
Étape 4 : Installer les plugins Jenkins
Docker Plugin
Plugin d’extension d’e-mail
SonarQube Scanner Plugin
Étape 5 : Configurer SonarQube dans Jenkins
Gérez les installations du système Jenkins → → SonarQube.
Nom:.SonarQubeLocalServer
URL du serveur : .http://host.docker.internal:9000
Jeton d’authentification du serveur : sélectionnez le jeton généré précédemment.
Étape 6 : Créer un webhook SonarQube
Dans le tableau de bord SonarQube, allez dans Administration → Configuration → Webhooks → Créer.
Nom:.jenkinsWebhooks
URL:.http://host.docker.internal:8081/sonarqube-webhook/
Étape 7 : Configuration de la notification par e-mail
Pour les comptes Google :

Activez l’authentification en 2 étapes.
Créez un mot de passe d’application pour Jenkins ici.
Configurer la notification par e-mail Jenkins :

SMTP: 587
Jeu de caractères : UTF-8
Partie 4 : Configuration du pipeline Jenkins
Mettez à jour votre fichier Jenkins à l’aide des extraits suivants :

groovy

Copier le code
def ENV_NAME = getEnvName(env.BRANCH_NAME)
def CONTAINER_NAME = "calculator-"+ENV_NAME

def runApp(containerName, tag, dockerHubUser, httpPort, envName) {
    sh "docker pull $dockerHubUser/$containerName:$tag"
    sh "docker run --rm --env SPRING_ACTIVE_PROFILES=$envName -d -p $httpPort:$httpPort --name $containerName $dockerHubUser/$containerName:$tag"
    echo "Application started on port: ${httpPort} (http)"
}
Ce README fournit un guide de configuration complet pour l’intégration de Docker, SonarQube et Jenkins afin de permettre la surveillance et l’intégration continue de votre projet Java.
