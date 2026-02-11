# Jenkins agent Docker image

This folder contains a simple Dockerfile you can use to build a Jenkins agent image with:
- OpenJDK 17
- Maven (from Ubuntu package)
- Chromium browser and ChromiumDriver

Build:
```bash
docker build -t selenium-jenkins-agent:latest ./ci/jenkins-agent
```

Run (example):
```bash
# run interactive container for debugging
docker run --rm -it --shm-size=2g selenium-jenkins-agent:latest

# run as Jenkins Docker agent (example entrypoint will keep container alive)
docker run -d --name jenkins-agent --shm-size=2g selenium-jenkins-agent:latest
```

Usage in Jenkins
- In Pipeline job configure Docker Cloud / Docker agents to use this image or use `docker` agent in pipeline:

```groovy
agent {
  docker {
    image 'selenium-jenkins-agent:latest'
    args '--shm-size=2g'
  }
}
```

Notes
- This image is intentionally minimal and installs `chromium-browser` + `chromium-chromedriver` from Ubuntu packages. On some platforms/chrome versions chromedriver compatibility can vary — if you hit version mismatch issues, prefer building an image pinning specific Chrome + matching chromedriver downloads.
- For reproducible builds consider using a multi-stage image or an official base (e.g., adoptopenjdk/maven) and pin package versions.
