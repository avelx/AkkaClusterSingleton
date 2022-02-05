#!/usr/bin/env bash

set -x

sbt "runMain Server -Dakka.http.server.default-http-port=8002 -Dakka.remote.artery.canonical.port=2552"
