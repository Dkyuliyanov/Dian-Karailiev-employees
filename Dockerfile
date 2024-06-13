#
# Build intermediary ubuntu image with java, maven and graalvm
#
# (this image is cached after first build)
#
FROM ubuntu:jammy AS jammy-graalvm21

RUN apt update -y \
  && apt install -y build-essential zlib1g-dev curl ca-certificates \
  && mkdir -p /tools/jdk /tools/graalvm /tools/maven

RUN curl 'https://download.oracle.com/java/21/latest/jdk-21_linux-x64_bin.tar.gz' -o /tmp/openjdk.tar.gz \
  && tar -zxf /tmp/openjdk.tar.gz --directory /tools/jdk --strip-components 1

RUN curl 'https://download.oracle.com/graalvm/21/latest/graalvm-jdk-21_linux-x64_bin.tar.gz' -o /tmp/graalvm.tar.gz \
  && tar -zxf /tmp/graalvm.tar.gz --directory /tools/graalvm --strip-components 1

RUN curl 'https://dlcdn.apache.org/maven/maven-3/3.9.6/binaries/apache-maven-3.9.6-bin.tar.gz' -o /tmp/apache-maven.tar.gz \
  && tar -zxf /tmp/apache-maven.tar.gz --directory /tools/maven --strip-components 1

ENV GRAALVM_HOME=/tools/graalvm
ENV JAVA_HOME=/tools/jdk
ENV PATH=/tools/jdk/bin:/tools/maven/bin:/usr/local/sbin:/usr/local/bin:/usr/sbin:/usr/bin:/sbin:/bin

#
# Build the native image
#
FROM jammy-graalvm21 AS builder

COPY . /build
WORKDIR /build

RUN mvn clean package
RUN cp -rp native target/
RUN mvn -Pnative -Dagent package

#
# Finally export the built binary to host
#
# Run with: docker build -f Dockerfile -o . .
#
#
FROM scratch AS export

COPY --from=builder /build/target/Dian-Karailiev-employees /
