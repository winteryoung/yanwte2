#!/bin/bash

git config core.hooksPath .githooks

script_list=(
    ".githooks/pre-commit"
    ".githooks/post-commit"
    ".githooks/setup-hooks.sh"
)

for script in "${script_list[@]}"; do
    git update-index --chmod=+x "$script"
done