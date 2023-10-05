
# docker-compose with mysql and phpMyAdmin

## Install Docker and Docker-Compose

https://docs.docker.com/compose/install/

## Start mySQL and phpMyAdmin via IntelliJ

In your IntelliJ IDEA, click on 'Edit run/debug configurations' it's located on the top right.
Select 'MySQL' under 'Run Configurations' now click on run, docker-compose will start now.



## Another way to start docker-compose

On folder that contains `docker-compose.yml` type one of this.

```
docker-compose up
```


## Stop server

Shutdown database and remove container.
```
docker-compose down
```


## MySQL credential

- Username: root
- Password: root


## Connect to MySQL Via phpMyAdmin (like pgAdmin)
Go to http://localhost:9090

- Username: root
- Password: root

## Import a sql dump
1. Go to http://localhost:9090/
2. Click on "db" on the left.
2. Click on "Import" on the top.
3. Select your file with "Choose File".
4. Click on "Import" on the bottom.


