ARG BASE_IMAGE
FROM ${BASE_IMAGE}

VOLUME /tmp

RUN mkdir -p /opt/ema && chmod 777 /opt/ema
WORKDIR /opt/ema

ARG GITHASH
ARG GITBRANCH
ARG BUILD_TIMESTAMP

ENV GITHASH="${GITHASH}" \
    GITBRANCH="${GITBRANCH}" \
    BUILD_TIMESTAMP="${BUILD_TIMESTAMP}"

ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /opt/ema/app.jar --spring.config.location=file:/config/ema.yml"]
