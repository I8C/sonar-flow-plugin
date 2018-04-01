FROM sonarqube:latest

ENV SONARQUBE_HOME /opt/sonarqube

VOLUME "$SONARQUBE_HOME/extensions"

WORKDIR $SONARQUBE_HOME
ENTRYPOINT ["./bin/run.sh"]