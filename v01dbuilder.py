"""
MIT License

Copyright (c) 2024 - 2025 v01d

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
"""

from argparse import ArgumentParser
from logging import DEBUG, INFO, basicConfig, getLogger
from os import mkdir
from pathlib import Path
from re import match
from shutil import copy, copytree, make_archive, rmtree
from subprocess import Popen


LOGGER_LEVEL = DEBUG if __debug__ else INFO
LOGGER_FORMAT = "%(asctime)s %(message)s"
BUILD_FOLDER = Path("./app/build").resolve()


basicConfig(format=LOGGER_FORMAT, level=LOGGER_LEVEL)


parser = ArgumentParser()
parser.add_argument("-j", "--jre-path", help="Path to JRE", required=True)
logger = getLogger(__name__)


def main():
    argv = parser.parse_args()
    jre_path = Path(argv.jre_path).resolve()

    output_folder = BUILD_FOLDER / "launch4j"

    version = get_project_version()
    project_name = get_executable_name(output_folder)

    logger.info(f"Building {project_name} v{version}!")

    copy_jre(jre_path, output_folder)
    copy_jar(version, output_folder)
    copy_docs(output_folder)

    path_to_archive = create_archive(output_folder, BUILD_FOLDER)

    make_release(project_name, version, path_to_archive)

    logger.info("Done.")


def get_project_version() -> str:
    for file in (BUILD_FOLDER / "libs").iterdir():
        res = match(r"app-(\d\.\d\.\d)\.jar", file.name)
        if res:
            return res.group(1)

    raise RuntimeError("Failed to parse project version")


def get_executable_name(output_folder: Path) -> str:
    for file in output_folder.iterdir():
        if file.suffix == ".exe":
            return file.with_suffix("").name

    raise RuntimeError("Failed to parse executable name")


def copy_jre(jre_path: Path, output_folder: Path):
    jre_folder_name = jre_path.name
    jre_output_path = output_folder / jre_folder_name
    logger.info(f"Copying JRE from {jre_path} to {jre_output_path}")
    copytree(jre_path, jre_output_path, dirs_exist_ok=True)


def copy_jar(project_version: str, output_folder: Path):
    output_lib_folder = output_folder / "lib"
    logger.info(f"Removing {output_lib_folder}")
    rmtree(output_lib_folder)

    mkdir(output_lib_folder)

    jar_full_path = BUILD_FOLDER / "libs" / f"app-{project_version}.jar"
    logger.info(f"Copying {jar_full_path} to {output_lib_folder}")
    copy(jar_full_path, output_lib_folder)


def copy_docs(output_folder: Path):
    logger.info(f"Copying docs to {output_folder}")
    copy(Path(".").resolve() / "README.md", output_folder)


def create_archive(source_folder: Path, target_folder: Path) -> Path:
    logger.info("Creating archive")

    return Path(make_archive(str(target_folder / "Release"), "zip", source_folder))


def make_release(project_name: str, project_version: str, path_to_archive: Path):
    logger.info("Releasing!")

    with Popen(
        [
            "gh",
            "release",
            "create",
            project_version,
            "--target",
            "main",
            "-t",
            f"{project_name} v{project_version}",
            "--notes",
            "Release",
            path_to_archive,
        ]
    ) as gh:
        gh.wait()


if __name__ == "__main__":
    main()
