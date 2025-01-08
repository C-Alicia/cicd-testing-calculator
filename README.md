# Dépôt des sources JAVA pour cours CICD 

Explications des sections :
FROM : Indique l'image de base à utiliser, ici une version allégée d'OpenJDK 11. Alpine est souvent utilisé pour sa légèreté.

ARG : Définit un argument de construction qui peut être utilisé pour spécifier le chemin du fichier JAR.

WORKDIR : Définit le répertoire de travail où les commandes suivantes seront exécutées.

COPY : Copie le fichier JAR et le script d'entrée dans le répertoire de travail.

RUN : Exécute une commande pour modifier les permissions du script d'entrée, le rendant exécutable.

ENTRYPOINT : Définit le script qui sera exécuté lorsque le conteneur démarrera.

Jacoco rapport :
Vérifiez votre pom.xml : Assurez-vous que vous avez ajouté le plugin JaCoCo dans votre fichier pom.xml.

Dans votre terminal, exécutez la commande suivante pour lancer vos tests. Cela génère le fichier jacoco.exec
mvn clean test          # Exécutez les tests et générez le fichier jacoco.exec

Générez le rapport HTML : Une fois que vos tests sont exécutés, exécutez la commande suivante pour générer le rapport HTML :
mvn jacoco:report      # Générez le rapport HTML

Accédez au rapport : Après avoir exécuté la commande ci-dessus, le rapport sera généré dans le répertoire suivant :

Ouvrez le fichier index.html dans votre navigateur pour visualiser le rapport de couverture de code.

