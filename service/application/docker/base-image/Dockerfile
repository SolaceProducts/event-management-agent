# syntax=docker/dockerfile:1
FROM ubuntu:20.04
RUN /bin/sh -c set -eux; apt-get update; DEBIAN_FRONTEND=noninteractive apt-get install -y --no-install-recommends ca-certificates curl netbase wget tzdata openssl fontconfig gzip tar vim locales; rm -rf /var/lib/apt/lists/*
CMD ["/bin/bash"]
ENV LANG=en_US.UTF-8 LANGUAGE=en_US:en LC_ALL=en_US.UTF-8
ENV JAVA_VERSION=jdk-11.0.16+8
RUN /bin/sh -c set -ex; mkdir -p /opt/java/openjdk; cd /opt/java/openjdk; wget https://github.com/adoptium/temurin11-binaries/releases/download/jdk-11.0.16%2B8/OpenJDK11U-jdk_x64_linux_hotspot_11.0.16_8.tar.gz.sha256.txt; wget https://github.com/adoptium/temurin11-binaries/releases/download/jdk-11.0.16%2B8/OpenJDK11U-jdk_x64_linux_hotspot_11.0.16_8.tar.gz; if echo "$(cat OpenJDK11U-jdk_x64_linux_hotspot_11.0.16_8.tar.gz.sha256.txt | awk '{print $1;}') OpenJDK11U-jdk_x64_linux_hotspot_11.0.16_8.tar.gz" | sha256sum --check --strict --status; then echo "JDK binary verified"; else   echo "Failed to verify JDK binary";  exit 1; fi; tar -xf OpenJDK11U-jdk_x64_linux_hotspot_11.0.16_8.tar.gz  --strip-components=1; rm OpenJDK11U-jdk_x64_linux_hotspot_11.0.16_8.tar.gz; rm OpenJDK11U-jdk_x64_linux_hotspot_11.0.16_8.tar.gz.sha256.txt; echo "en_US.UTF-8 UTF-8" > /etc/locale.gen; locale-gen
ENV JAVA_HOME=/opt/java/openjdk PATH=/opt/java/openjdk/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin
RUN /bin/sh -c echo Verifying install ... && echo java --version && java --version && echo Complete.