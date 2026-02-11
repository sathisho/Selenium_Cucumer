# Jenkins pipeline setup

This repository now includes a `Jenkinsfile` and a small CI helper script (`ci/run_tests.sh`). Follow these steps to run tests from Jenkins jobs only.

1) Create a new Pipeline Job in Jenkins
  - Pipeline type: Pipeline
  - From: SCM → Git
  - Repository URL: `https://github.com/sathisho/Selenium_Cucumer.git`
  - Credentials: (add if private)
  - Branch: `*/main`
  - Script Path: `Jenkinsfile`

2) Configure agents/tools
  - Ensure Jenkins agent has Java (JDK 17+), Maven, Chrome (or headless browser), and ChromeDriver available.
  - If using Docker agents, use an image with JDK + Maven + Chrome + chromedriver installed.

3) Credentials & environment
  - If tests send email or require secrets, add Jenkins Credentials (username/password or secret text) and expose them as environment variables in the job or via Credentials Binding.
  - To disable in-test email sending during CI, either set the email properties in `src/test/resources/email.properties` to disabled or set a JVM property via `MVN_ADDITIONAL_ARGS` param (e.g. `-Demail.enabled=false`).

4) Running tests
  - By default the pipeline runs tests with `CUCUMBER_TAGS=@e2e` (changeable in job parameters).
  - To run different tags, trigger job with parameter `CUCUMBER_TAGS="@regression"` or update `MVN_ADDITIONAL_ARGS`.

5) Artifacts and reports
  - The pipeline archives `test-output/**` and `target/surefire-reports/*.xml`. Use Jenkins JUnit publisher to show test results.
  - Screenshots and Extent/Cucumber reports are archived as artifacts.

6) Recommended improvements
  - Add a dedicated Maven `settings.xml` credential server or Jenkins tool configuration for reproducible builds.
  - Use a Dockerized Jenkins agent for consistent environment.
  - Add a healthcheck stage and optional parallel stages for different tag sets.

7) Triggering
  - Create a GitHub webhook or Jenkins GitHub plugin to trigger the pipeline on push/PR.

Example manual run
```
# run Jenkins job parameterized: run only e2e
CUCUMBER_TAGS='@e2e'
```
