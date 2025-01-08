#!/bin/sh

# Affiche un message indiquant que l'application est en cours de démarrage
echo "The app is starting ..."

# Exécute l'application Java en utilisant le profil Spring spécifié dans la variable d'environnement
exec java -jar -Dspring.profiles.active=${SPRING_ACTIVE_PROFILES} "calculator.jar"