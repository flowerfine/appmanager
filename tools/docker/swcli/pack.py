    import shutil
    import tempfile
    import os
    import argparse

    parser = argparse.ArgumentParser(description='package tool')
    parser.add_argument("-s",'--src', type=str, dest="src", required=True)
    parser.add_argument("-d",'--dest', type=str, dest="dest", required=True)
    args = parser.parse_args()

    packagePath = tempfile.mkdtemp()
    targetPath = packagePath + "/target"

    shutil.copytree(args.src, targetPath)

    for path in os.listdir(targetPath):
        if path.endswith(".zip.dir"):
            shutil.make_archive(targetPath + "/" + path.split(".zip")[0], 'zip', targetPath + "/" + path)
            shutil.rmtree(targetPath + "/" + path)


    shutil.make_archive(args.dest.split(".zip")[0], 'zip', targetPath + "/")