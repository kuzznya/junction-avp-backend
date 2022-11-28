#!/bin/bash

docker run --rm -v "${PWD}:/local" openapitools/openapi-generator-cli generate \
    -i https://junction.kuzznya.com/v3/api-docs \
    -g typescript-axios \
    -o /local/src/api/generated
