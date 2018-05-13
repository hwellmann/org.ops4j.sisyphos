FROM jboss/wildfly:12.0.0.Final
LABEL maintainer="Harald Wellmann <harald.wellmann@gmx.de>"

# deploy the WAR from the Maven build
ADD maven/sisyphos.war /opt/jboss/wildfly/standalone/deployments/sisyphos.war

CMD ["/opt/jboss/wildfly/bin/standalone.sh", "-b", "0.0.0.0"]



