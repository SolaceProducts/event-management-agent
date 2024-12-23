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

COPY tofu_1.8.7_amd64.apk /opt/ema/terraform
RUN apk --update add --allow-untrusted /opt/ema/terraform/tofu_1.8.7_amd64.apk

COPY .terraformrc $HOME/.terraformrc
RUN printf '#!/bin/ash\ntofu $*' > /opt/ema/terraform/terraform
RUN chmod +x /opt/ema/terraform/terraform
ENV PATH $PATH:/opt/ema/terraform

ARG SOLACE_PROVIDER_VERSION=1.1.0
ADD terraform-provider-solacebroker_${SOLACE_PROVIDER_VERSION}_${PLATFORM}.zip /opt/ema/terraform

RUN unzip /opt/ema/terraform/terraform-provider-solacebroker_${SOLACE_PROVIDER_VERSION}_${PLATFORM}.zip -d /opt/ema/terraform && \
    rm /opt/ema/terraform/terraform-provider-solacebroker_${SOLACE_PROVIDER_VERSION}_${PLATFORM}.zip

ARG GITHASH
ARG GITBRANCH
ARG BUILD_TIMESTAMP

ENV GITHASH="${GITHASH}" \
    GITBRANCH="${GITBRANCH}" \
    BUILD_TIMESTAMP="${BUILD_TIMESTAMP}"

# switch to the created user from root
USER $USER

ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /opt/ema/app.jar $CMD_LINE_ARGS --spring.config.location=file:/config/ema.yml"]
