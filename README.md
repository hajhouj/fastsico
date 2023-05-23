# What is FASTSICO?

FASTSICO (for Fast String Similarity Computation) is a Java library that provides various functionalities to perform String similarity calculations. The library contains different algorithms to compute the similarity between a given string and a set of target strings. It has a user-friendly interface that allows easy selection of an OpenCL device for faster computation of similarity scores. FASTSICO library is capable of working with both CPU and GPU devices.

The primary objective of FASTSICO is enhancing the speed of string similarity computation for large string datasets. FASTSICO leverages the benefits of parallel computation and is capable of using both CPU and GPU via OpenCL. The library aims to provide a robust tool for developers and researchers to overcome the challenges associated with the time-consuming process of finding near-identical strings in voluminous datasets such as vocabulary files, genomic datasets, and others. The library's effectiveness lies in its ability to expedite the similarity search process and generate quick results, enabling users to handle larger datasets with greater ease and efficiency.

# Table of contents

* [Requirements](#r)
* [Installation](#i)
* [Usage](#u)
* [Benchmarking FASTSICO Library on OpenCL Devices](#b)
* [Troubleshooting](#t)
* [Version History](#vh)
* [Project Background and Acknowledgements](#ack)
* [Contributing](#c)

# <a id="r"></a>Requirements

Software and hardware requirements necessary to use the FASTSICO library:

Software:

* JDK 1.8 or later

Hardware:

* A device with OpenCL support

Note that the specific hardware requirements will vary depending on the size of the dataset and the performance demands of the application. It's also important to ensure that your OpenCL drivers are up to date to avoid compatibility issues.


# <a id="i"></a>Installation

Steps for installing the FASTSICO library as a Maven dependency in your project:

* Ensure that you have Maven installed on your machine. You can check this by running the command `mvn -v` in a terminal or command prompt. If Maven is not installed, you can download it from the official website at [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi).
* Create a new Maven project or open an existing one where you want to add the FASTSICO library as a dependency.
* Inside the `pom.xml` file, add the following XML code inside the `<dependencies>` section:

```
<dependency>
    <groupId>com.hajhouj.fastsico</groupId>
    <artifactId>fastsico</artifactId>
    <version>1.0.1</version>
</dependency>
```

* Run the command `mvn install` in a terminal or command prompt in the root directory of your project. This will download the FASTSICO library and install it to your local Maven repository.

After following these steps, you can use the FASTSICO library in your Java code by importing the classes you need and calling their methods.
    
# <a id="u"></a>Usage

FASTSICO is a Java library for calculating the similarity score between a query string and a list of target strings using different algorithms. In addition to being used as a library, it can also be used as a command-line utility. This document provides detailed instructions on how to use FASTSICO in both modes.

## Using FASTSICO as a Library

The primary way to use FASTSICO is as a Java library. You can call the method `calculateSimilarity(query, targets, algorithm)` to calculate the similarity score between the query string and each target string in the strings dataset using the specified algorithm. The method signature is as follows:

```java
public List<Result> calculateSimilarity(String query, String dataset, String algorithm);
```

* `query` is the query string.
* `dataset` a large dataset of strings.
* `algorithm` is the name of the algorithm to use for string similarity calculation. For version 1.0.0, only Edit distance algorithm is present in the library, and you can pass `IConstants.EDIT_DISTANCE` to use this algorithm.

The method returns a list of `Result` objects, each containing a target string and its similarity score to the query string. The list is ordered in ascending order by similarity score.

By default, FASTSICO tries to find the best OpenCL device available on your host to perform the computation, leveraging the parallelism capability of OpenCL to speed up the computation. However, if you want to use a specific device, you can pass a Java system property called `use-device` with the query string indicating which type of device you want to use and which one exactly by giving its index. For example, if you want to use the second OpenCL device present on your machine, which is the second OpenCL device in platform 0, you can set the `use-device` property to `0.1`.

Here is an example of how to use the `calculateSimilarity` method with a specific OpenCL device:

```java
System.setProperty("use-device", "0.1"); // Set the device to use
String query = "apple";
String algorithm = IConstants.EDIT_DISTANCE;
String dataset = "fruits.txt";
List<Result> results = calculateSimilarity(query, dataset, algorithm);
```

## Using FASTSICO as a Command-Line Utility

FASTSICO can also be used as a command-line utility to find the most similar strings to a query string from a file containing a list of strings. The command has the following syntax:

```sh
java -cp "lib/*":fastsico-1.0.1.jar com.hajhouj.fastsico.tools.Find <data-file> <query> <top-n> <output-format>
```

* `<data-file>` is the path to the file containing the list of target strings.
* `<query>` is the query string.
* `<top-n>` is the number of top results to return.
* `<output-format>` is the output format, which can be `json`, `xml`, or `csv`.

By default, the command will use the default OpenCL device available with the best configuration. However, if you need to specify a specific device, you can use the `use-device` system property, as shown in the example below:

```sh
java -Duse-device=0.0 -cp "lib/*":fastsico-1.0.1.jar com.hajhouj.fastsico.tools.Find <data-file> <query> <top-n> <output-format>
```

Here, `0.0` means to use the first OpenCL device in platform 0.

## DevicesList Utility Command

The DevicesList utility command is a helpful tool for listing the available OpenCL devices on the host machine, and for determining the query string to use when specifying a particular device using the `use-device` system property. The DevicesList utility command list the available OpenCL devices on the host machine, along with their associated query strings.&#x20;

To use the DevicesList utility command, run the following command:

```sh
java -cp lib/*:fastsico-1.0.1.jar com.hajhouj.oss.fastsico.tools.DevicesList
```

This will list the available OpenCL devices on the host machine, along with their associated query strings, in the following format:

```
DEVICE QUERY | DEVICE NAME
-------------+-------------
<query-1>    | <device-name-1>
<query-2>    | <device-name-2>
<query-3>    | <device-name-3>
...

```

# <a id="b"></a>Benchmarking FASTSICO Library on OpenCL Devices

To benchmark the FASTSICO Library on your OpenCL devices, follow these steps:

1. Download the latest release of [FASTSICO](https://github.com/hajhouj/fastsico/releases/latest).
2. Before running the benchmark script, you need to download the lyrics data from the following [link](https://www.kaggle.com/datasets/neisse/scrapped-lyrics-from-6-genres?resource=download&select=lyrics-data.csv). The lyrics data contains 15 million lines corresponding to lyrics from a vast number of songs across various genres.
3. After downloading the lyrics data, place it in the same location as the benchmark script.
4. Unzip the downloaded release archive.
5. Run the benchmark script based on your operating system:
       * For Linux, execute the benchmark.sh script.
       * For Windows, execute the benchmark.cmd script.
6. When prompted, select the device you want to benchmark by entering its corresponding query device.
7. The script will search for the query words "love of story" within the downloaded lyrics data and provide the top 10 most similar strings along with their similarity scores.

# <a id="t"></a>Troubleshooting

## Device not available

* __Symptom__: You receive an error stating that the OpenCL device is not available.
* __Solution__: This means the specific OpenCL device you're trying to use is not available. It could be used by another process, or it might not support the OpenCL features you're trying to use. Try using a different device.

## CL_OUT_OF_HOST_MEMORY error

* __Symptom__: You encounter an error stating "CL_OUT_OF_HOST_MEMORY".
* __Solution__: This error occurs when the host's memory available for OpenCL is insufficient. You may need to free up some system resources. Try closing unnecessary applications, or consider upgrading your system's memory.
    
# <a id="vh"></a>Version History

A list of changes made to the library in each version.

## 1.0.1
* Correct bug in device selection
* Add output formatting (xml, csv, json)

## 1.0.0
* First version
* Support of Edit Distance Algorithm

# <a id="ack"></a>Project Background and Acknowledgements

This library was developed as part of a doctoral thesis supervised by [Professor Mounia Abik](http://ensias.um5.ac.ma/professor/mme-mounia-abik) from the École Nationale Supérieure d’Informatique et d’Analyse des Systèmes ([ENSIAS](http://ensias.um5.ac.ma/)) of the Mohamed V University of Rabat. 

The development and testing of this library would not have been possible without the support of the Centre National pour la Recherche Scientifique et Technique ([CNRST](https://www.cnrst.ma/fr/)) and the team responsible for the Data Center HPC MARWAN, who generously provided the necessary access to computing machines for testing the library.

We extend our deepest gratitude to these individuals and institutions for their support and contributions to this project. The Mohamed V University of Rabat and ENSIAS continue to be pivotal in the development of cutting-edge research in the field of information technology and systems analysis. For more information about the university, you can visit their official website [here](http://www.um5.ac.ma/).

# <a id="c"></a>Contributing

FASTSICO  is a library for efficient string similarity computations. If you wish to add a new string similarity algorithm to FASTSICO, here are some guidelines to help you get started:

1. Create a new class that implements the `StringSimilarityAlgorithm` interface: The first step is to create a new class that implements the `StringSimilarityAlgorithm` interface. This interface has two methods: `calculateSimilarity(String query, String target)` and `calculateSimilarity(String query, String[] targets)`. Implement the `calculateSimilarity` method to compute the similarity between the query string and a single target string. Implement the `calculateSimilarity` method that takes an array of target strings, to compute the similarity between the query string and multiple target strings.
2. Add a new constant to the `IConstants` interface: In the `IConstants` interface, add a new constant that represents the new string similarity algorithm. Choose a unique name for the constant that describes the new algorithm.
3. Update the `AlgorithmFactory` class: In the `AlgorithmFactory` class, update the `getAlgorithm` method to create an instance of your new string similarity algorithm implementation when the `algorithm` parameter matches the name of the new constant you added to the `IConstants` interface.
4. Implement the new algorithm: Implement the new string similarity algorithm in the class you created in step 1. You can use the existing algorithms as a reference for how to implement the new algorithm. Make sure that the algorithm is efficient and can handle large datasets.
5. Write unit tests: Write unit tests for the new algorithm to ensure that it returns correct results. You can use the existing unit tests as a reference for how to write unit tests.
6. Add documentation: Add documentation to the new algorithm implementation that explains how the algorithm works and how to use it. Also, update the documentation for the `StringSimilarity` class to describe the new algorithm.
7. Submit a pull request: Once you have completed the above steps, submit a pull request to the FastSico project with your new implementation. Make sure that your code follows the project's coding style and standards. Your pull request should include the new implementation, unit tests, and documentation.

