# Utilise l'image de base OpenJDK 11 avec Alpine JRE pour une empreinte légère
FROM adoptopenjdk/openjdk11:alpine-jre

# Définit un argument de construction pour le chemin du fichier JAR
ARG JAR_FILE=target/calculator.jar

# Définit le répertoire de travail dans le conteneur
WORKDIR /opt/app

# Copie le fichier JAR construit dans le répertoire de travail
COPY ${JAR_FILE} calculator.jar

# Copie le script d'entrée dans le répertoire de travail
COPY entrypoint.sh entrypoint.sh

# Copie le script d'entrée dans le répertoire de travail
RUN chmod 755 entrypoint.sh

# Définit le point d'entrée du conteneur, en exécutant le script d'entrée
ENTRYPOINT ["./entrypoint.sh"]