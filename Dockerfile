FROM goyalzz/ubuntu-java-8-maven-docker-image
MAINTAINER Oscar Gonzalez (oscar.gonzalezdedios@telefonica.com)
ARG GIT_REVISION=unknown
LABEL git-revision=$GIT_REVISION    
LABEL version="1.3.4"
LABEL description="BGP Peer"
LABEL source_url="https://github.com/telefonicaid/netphony-topology"

#copy code files
COPY . /usr/src/app

#compile
RUN mvn package -P bgp-ls-speaker assembly:single -X -f /usr/src/app/pom.xml
RUN mv /usr/src/app/target/bgp-ls-speaker-jar-with-dependencies.jar /usr/src/app/target/BGPPeer.jar
RUN cp -r /usr/src/app/src/test/resources/ /usr/src/app/examplesConf/
WORKDIR /usr/src/app/

