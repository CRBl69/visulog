# Visulog

*Tool for analysis and visualization of git logs*

## Presentation

Visulog is a tool that extracts data from git repositories. It can display
charts in a web browser or output the data in JSON format. It's the ideal tool
for contribution analysis, and can be a key to identify a great workflow for
your team. It can also be used in the study of work performance, and study how
a project has evolved from the start to the end. Visulog is intended for power
users, with a minimum knowledge of the command line. It integrates very useful
features like extensive configuration through a config file, and command line
arguments. It is quite performant, and is able to process a milion commits and
generate the data in about 3 minutes (tested on the linux repo).

## Already existing similar tools

- [gitstats](https://pypi.org/project/gitstats/)

## Usage

### Building the project

1. Clone the repository

    ```
    git clone git@gaufre.informatique.univ-paris-diderot.fr:crisan/visulog.git
    ```

    or

    ```
    git clone https://gaufre.informatique.univ-paris-diderot.fr/crisan/visulog.git
    ```

2. Enter the project folder

    ```
    cd visulog
    ```
3. Only if you are on a SCRIPT computer (in one of the TP rooms):

    ```
    source SCRIPT/envsetup
    ```

    This will setup the `GRADLE_OPTS` environment variable so that gradle uses the
    SCRIPT proxy for downloading its dependencies. It will also use a custom trust
    store (the one installed in the system is apparently broken... ).

4. Run gradle wrapper (it will download all dependencies, including gradle itself)

    ```
    ./gradlew build
    ```

### Running the software

In order to run `visulog`, you have to do the following:

```
$ ./gradlew installDist
$ ./cli/build/install/cli/bin/cli
```

## Documentation

The documentation can be found in the `docs` folder

