#!/bin/bash

set -e

rm -Rf ./emoji-cheat-sheet.com
rm -Rf ./src/main/resources/icons
git submodule update --init

mkdir -p ./src/main/resources/icons

for file in "./emoji-cheat-sheet.com/public/graphics/emojis"/*
do
  filename=`basename "$file"`
  name="${filename%.*}"
  # this program require ImageMagick!
  convert "$file" -resize 50% ./src/main/resources/icons/"$name"@2x.png
  convert "$file" -resize 25% ./src/main/resources/icons/"$filename"
done

