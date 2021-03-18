#!/bin/bash -x


ULTIMO_COMMIT_REPO=$(git log -1 --format=%cd)
ULTIMO_COMMIT_CLIENTES=$(git log -1 --format=%cd microservice-clientes)

if [[ $ULTIMO_COMMIT_REPO = $ULTIMO_COMMIT_CLIENTES ]]
then
    echo "Pasta clientes foi alterada"
else
    echo "Pasta não alterada"
fi