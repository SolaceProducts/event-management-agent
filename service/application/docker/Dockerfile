ARG BASE_IMAGE
FROM ${BASE_IMAGE}

VOLUME /tmp

RUN mkdir -p /opt/ema && chmod 777 /opt/ema && mkdir -p /opt/ema/terraform && chmod 777 /opt/ema/terraform
WORKDIR /opt/ema

ARG PLATFORM=linux_amd64

# Do not update, this is the latest MPL-licensed Terraform version
ADD terraform_1.5.5_${PLATFORM}.tar.gz /opt/ema/terraform

ARG SOLACE_PROVIDER_VERSION=0.9.0
ADD terraform-provider-solacebroker_${SOLACE_PROVIDER_VERSION}_${PLATFORM}.tar.gz /opt/ema/terraform

COPY .terraformrc /root/.terraformrc

ENV PATH $PATH:/opt/ema/terraform

ARG GITHASH
ARG GITBRANCH
ARG BUILD_TIMESTAMP

ENV GITHASH="${GITHASH}" \
    GITBRANCH="${GITBRANCH}" \
    BUILD_TIMESTAMP="${BUILD_TIMESTAMP}"

ARG JAR_FILE
ADD ${JAR_FILE} app.jar

ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /opt/ema/app.jar --spring.config.location=file:/config/ema.yml"]
