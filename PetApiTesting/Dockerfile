from ubuntu:16.04

# In order to run tests in Ubuntu Docker containers, either we can use similar to this Dockerfile or we can add ready-made image of Java and Maven from Docker hub in this Dockerfile
# In order to run tests using Dockerfile, command is

RUN apt-get autoclean
RUN apt-get clean

# Install OpenJDK-8 and maven
RUN apt-get update && \
    apt-get install -y openjdk-8-jdk && \
    apt-get install -y ant && \
    apt-get install -y maven && \
    apt-get clean;

# Fix certificate issues
RUN apt-get update && \
    apt-get install ca-certificates-java && \
    apt-get clean && \
    update-ca-certificates -f;

# Setup JAVA_HOME -- useful for docker commandline
ENV JAVA_HOME /usr/lib/jvm/java-8-openjdk-amd64/
RUN export JAVA_HOME

COPY . /usr/src/app

WORKDIR /usr/src/app

ENTRYPOINT ["mvn", "clean", "install", "-Dtestng.dtd.http=true"]






