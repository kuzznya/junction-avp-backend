#!/bin/bash

kubectl create secret generic junction-secrets --from-env-file .env
