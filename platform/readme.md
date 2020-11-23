# Projet IOT - Platform

Pour la platforme on utilise différents packages python pour se faciliter la vie
- `peewee` pour l'accès à la base de donnée
- `flask` pour le serveur web

pour les installer il suffit de lancer la commande `pip install -R ./requirements.txt`


De plus pour que le serveur web fonctionne il faut lancer les commandes `npm i`et `npm run build` dans le dossier `web/front` (il faut que [npm](https://nodejs.org/en/download/) soit installer sur le pc)

Enfin pour lancer le serveur il faut utiliser la commande `python main.py`