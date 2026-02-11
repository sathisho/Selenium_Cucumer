FROM ubuntu:22.04

ENV DEBIAN_FRONTEND=noninteractive
ENV JAVA_HOME=/usr/lib/jvm/java-17-openjdk-amd64
ENV PATH="$JAVA_HOME/bin:/usr/local/apache-maven/bin:$PATH"

RUN apt-get update && apt-get install -y --no-install-recommends \
    ca-certificates \
    curl \
    gnupg \
    unzip \
    wget \
    git \
    xvfb \
    fonts-liberation \
    libgtk-3-0 \
    libxss1 \
    libasound2 \
    openjdk-17-jdk \
    maven \
    chromium-browser \
    chromium-chromedriver \
 && rm -rf /var/lib/apt/lists/*

# Create a user for Jenkins agent work
RUN useradd -m -s /bin/bash jenkins
WORKDIR /home/jenkins
RUN chown -R jenkins:jenkins /home/jenkins

USER jenkins

ENV MAVEN_OPTS="-Xmx1024m"

# Default command prints versions (Jenkins will run containers and execute commands)
CMD ["/bin/bash","-lc","java -version && mvn -version && chromium-browser --version && chromedriver --version || true && sleep infinity"]
