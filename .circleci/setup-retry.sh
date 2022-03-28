#!/bin/bash

set -eu -o pipefail

sudo curl https://raw.githubusercontent.com/kadwanev/retry/1.0.1/retry -o /usr/bin/retry
sudo chmod +x /usr/bin/retry
