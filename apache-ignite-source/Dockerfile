
FROM openjdk:17-oracle
# Set environment variables
ENV IGNITE_HOME /opt/ignite
ENV IGNITE_VERSION 2.16.0
# Copy Apache Ignite binary folder to container
COPY apache-ignite-${IGNITE_VERSION}-bin /opt/ignite
# Set working directory
WORKDIR $IGNITE_HOME
# Expose ports
EXPOSE 11211 47100 47500 49112
# Run Apache Ignite
CMD ["bin/ignite.sh"]
