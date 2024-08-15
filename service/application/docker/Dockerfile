ARG BASE_IMAGE
FROM ${BASE_IMAGE}

# default user name is "emauser"
ARG USER=emauser
ENV HOME /home/$USER

VOLUME /tmp

# create user and directory setup
RUN adduser -D $USER && \
    mkdir -p /opt/ema && \
    chmod 777 /opt/ema && \
    mkdir -p /opt/ema/terraform && \
    chmod 777 /opt/ema/terraform && \
    chown -R $USER:$USER $HOME && \
    chown -R $USER:$USER /opt/ema/    

WORKDIR /opt/ema

ARG PLATFORM=linux_amd64

COPY tofu_1.7.0_amd64.apk /opt/ema/terraform
RUN apk --update add --allow-untrusted /opt/ema/terraform/tofu_1.7.0_amd64.apk

# switch to the created user from root
USER $USER

COPY .terraformrc $HOME/.terraformrc
RUN printf '#!/bin/ash\ntofu $*' > /opt/ema/terraform/terraform
RUN chmod +x /opt/ema/terraform/terraform
ENV PATH $PATH:/opt/ema/terraform

ARG SOLACE_PROVIDER_VERSION=0.9.2-rc.2
ADD terraform-provider-solacebroker_${SOLACE_PROVIDER_VERSION}_${PLATFORM}.tar.gz /opt/ema/terraform

ARG GITHASH
ARG GITBRANCH
ARG BUILD_TIMESTAMP

ENV GITHASH="${GITHASH}" \
    GITBRANCH="${GITBRANCH}" \
    BUILD_TIMESTAMP="${BUILD_TIMESTAMP}"

ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /opt/ema/app.jar $CMD_LINE_ARGS --spring.config.location=file:/config/ema.yml"]
