FROM i386/openjdk:8-jdk-slim

RUN apt-get update
RUN apt-get install -y gcc g++ cmake python-pip git
RUN pip install ply
RUN useradd -ms /bin/bash travis

ADD --chown=travis:travis . /home/travis/kamp/

RUN chmod a+x /home/travis/kamp/gradlew
RUN chmod a+x /home/travis/kamp/build.sh

WORKDIR /home/travis/

USER travis

ENTRYPOINT [ "/home/travis/kamp/build.sh" ]
