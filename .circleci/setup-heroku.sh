#!/bin/bash

set -eu -o pipefail

curl https://cli-assets.heroku.com/install.sh | sh

cat >~/.netrc <<EOF
machine api.heroku.com
    login $HEROKU_EMAIL
    password $HEROKU_API_TOKEN
machine git.heroku.com
    login $HEROKU_EMAIL
    password $HEROKU_API_TOKEN
EOF
