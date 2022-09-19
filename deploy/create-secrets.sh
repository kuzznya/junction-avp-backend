#!/bin/bash

kubectl --namespace junction create secret generic junction-secrets --from-env-file .env
