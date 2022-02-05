#!/usr/bin/env bash

set -x

sbt "runMain Server -Dakka.http.server.default-http-port=8001 -Dakka.remote.artery.canonical.port=2551"
