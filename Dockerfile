FROM sonarqube:6.7.2

ENV SONARQUBE_HOME /opt/sonarqube

#Only install flow plugin. make sure it is build.
RUN rm -f $SONARQUBE_HOME/extensions/plugins/*
COPY sonar-flow-plugin/target/sonar-flow-plugin-1.0.jar $SONARQUBE_HOME/extensions/plugins

VOLUME "$SONARQUBE_HOME/extensions"

WORKDIR $SONARQUBE_HOME
ENTRYPOINT ["./bin/run.sh"]

#Now run docker build . -t sonarqube-flow
#Next run docker run --name sonarqube-flow -p 9000:9000 sonarqube-flow