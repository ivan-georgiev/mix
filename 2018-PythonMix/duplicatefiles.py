#!/usr/bin/env python3

import os, sys
from pathlib import Path
import hashlib
from multiprocessing import Pool, cpu_count
from functools import reduce
import logging, argparse
from datetime import datetime


#date validator for argparse
def validDate(datestring):
    try:
        return datetime.strptime(datestring, "%Y-%m-%d")
    except ValueError:
        msg = "Not a valid date: '{0}'.".format(datestring)
        raise argparse.ArgumentTypeError(msg)


# Generates file md5 hash
def hashFile(path: str, blocksize=65536) -> str:
    hasher = hashlib.md5()

    with open(path, 'rb') as afile:
        buf = afile.read(blocksize)
        while len(buf) > 0:
            hasher.update(buf)
            buf = afile.read(blocksize)

    return hasher.hexdigest()


# Checks if files are duplicates; returns in format: original_file: [duplicate1, duplicate2, ...]
def findDuplicates(files: list) -> dict:

    hashDict = {}
    duplicates = {}

    if len(files) > 1:
        for f in files:
            try:
                h = hashFile(str(f))

                if h in hashDict.keys():
                    duplicates.setdefault(hashDict[h], []).append(str(f))
                else:
                    hashDict[h] = str(f)
            except Exception as e:
                logging.error(e)
    return duplicates


def getFilesRecursively(directories: list) -> dict:

    filesSizeDict = {}
    allFiles = []

    #get all items
    for i in directories:
        allFiles.extend(list(Path(i).rglob("*")))

    #check type and genereate dict
    for i in allFiles:
        try:
            if i.is_file() and not i.is_symlink():
                filesSizeDict.setdefault(i.stat().st_size, []).append(i)
        except PermissionError as e:
            logging.error(e)
        except:
            raise

    return filesSizeDict


# Groups the files with same size and generates hashes if more then one. Runs in several processes
def optimizedDuplicatedFinder(directories: list, upToDate: datetime)-> dict:

    # get all files in dict: size - [(Path)file1, file2, ...]
    filesSizeDict = getFilesRecursively(directories)

    # if no files return empty dict
    if not filesSizeDict:
        return {}

    # sort by creation time, older files is original
    for k, v in filesSizeDict.items():
        v.sort(key=lambda f: f.stat().st_ctime)

        # remove file set if the newest file creation time is older than the timeline defined by -d arg
        if upToDate:
            if datetime.fromtimestamp(v[-1].stat().st_ctime) < upToDate:
                v.clear()

    # start generating hashes in separate processes (cpu count) for same size files
    with Pool(cpu_count()) as p:
        data = p.map(findDuplicates, [v for k, v in filesSizeDict.items()])

    # merge the dictionaries from the previous step
    return reduce((lambda x, y: {**x, **y}), data)


def main(args: argparse.Namespace):

    #logger config to read from env
    logging.basicConfig(level=os.environ.get('LOGLEVEL', 'WARNING').upper())

    for i in args.directories:
        if not os.path.isdir(i):
            raise Exception("No such directory: {}".format(i))

    #Get the duplicates
    duplicates = optimizedDuplicatedFinder(directories = args.directories, upToDate = args.d)

    #print result
    for k, v in duplicates.items():
        print("Original: {} \nDuplicates: {} \n".format(k, ', '.join(v)))


if __name__ == '__main__':

    #sys.argv = ["", r"/home", r"/tmp", "-d", "2018-10-21"]

    #parse arguments
    parser = argparse.ArgumentParser(description='Process list of directories. Syntax dir1 [dir2 dir3 ...] [-d yyyy-mm-dd]')
    parser.add_argument('-d', nargs='?', type=validDate, required=False, help='date in format yyyy-mm-dd')
    parser.add_argument('directories', type=str, nargs='+',help='List of directories, space separated')
    args = parser.parse_args()

    main(args)
