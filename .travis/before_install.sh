#!/bin/bash

if [ "$TRAVIS_PULL_REQUEST" != "false" ]; then
  # Check how to run this script
  echo "It's pull request!"
else
  # Without snapshot
  openssl aes-256-cbc -K $encrypted_9f6ec11ac5b3_key -iv $encrypted_9f6ec11ac5b3_iv -in travis.enc -out .travis.ssh -d
  chmod 600 .travis.ssh
  echo -e "Host github.com\n\tStrictHostKeyChecking no\nIdentityFile .travis.ssh\n" >> ~/.ssh/config
fi