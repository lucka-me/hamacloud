#!/usr/bin/env python3
# coding: utf-8

"""
Gutenberg Text Cleaner
Author: Lucka
"""
# Modules
import sys

def main():

    if len(sys.argv) < 2:
        print("Error: Argument missing, use main.py filename.")
        exit()
    inputFilename = sys.argv[1]
    outputFilename = "cleaned-" + inputFilename

    try:
        inputFileLines = open(inputFilename).readlines()
        outputFile = open(outputFilename, "w+")
    except Exception as error:
        print("Error: Failed to open the file.")
        exit()

    start = False
    for line in inputFileLines:
        if not start:
            if "*** START OF THIS PROJECT GUTENBERG EBOOK " in line:
                start = True
            continue
        else:
            if "*** END OF THIS PROJECT GUTENBERG EBOOK " in line:
                break

        line = line.lower()

        for char in line:
            if char is " " or char is "\n" or char.isalpha():
                outputFile.write(char)
            else:
                outputFile.write(" ")

    outputFile.close()


if __name__ == '__main__':
    main()
