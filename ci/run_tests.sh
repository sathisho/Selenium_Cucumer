#!/usr/bin/env bash
set -euo pipefail

# Usage: ./ci/run_tests.sh "@e2e" "additional mvn args"
TAG_FILTER=${1:-"@e2e"}
ADDITIONAL_ARGS=${2:-""}

echo "Running tests with Cucumber tags: ${TAG_FILTER}"

# run maven tests (non-interactive)
mvn -B -Dcucumber.filter.tags="${TAG_FILTER}" test ${ADDITIONAL_ARGS}

EXIT_CODE=$?
echo "mvn exit code: ${EXIT_CODE}"
exit ${EXIT_CODE}
