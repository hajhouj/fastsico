# Usage

FASTSICO is a Java library for calculating the similarity score between a query string and a list of target strings using different algorithms. In addition to being used as a library, it can also be used as a command-line utility. This document provides detailed instructions on how to use FASTSICO in both modes.

## Using FASTSICO as a Library

The primary way to use FASTSICO is as a Java library. You can call the method `calculateSimilarity(query, targets, algorithm)` to calculate the similarity score between the query string and each target string in the list of targets using the specified algorithm. The method signature is as follows:

```java
public List<Result> calculateSimilarity(String query, List<String> targets, String algorithm);
```

* `query` is the query string.
* `targets` is the list of target strings.
* `algorithm` is the name of the algorithm to use for string similarity calculation. For version 1.0.0, only Edit distance algorithm is present in the library, and you can pass `IConstants.EDIT_DISTANCE` to use this algorithm.

The method returns a list of `Result` objects, each containing a target string and its similarity score to the query string. The list is ordered in ascending order by similarity score.

By default, FASTSICO tries to find the best OpenCL device available on your host to perform the computation, leveraging the parallelism capability of OpenCL to speed up the computation. However, if you want to use a specific device, you can pass a Java system property called `use-device` with the query string indicating which type of device you want to use and which one exactly by giving its index. For example, if you want to use the second OpenCL device present on your machine, which is of type GPU and index 1, you can set the `use-device` property to `GPU.1`.

Here is an example of how to use the `calculateSimilarity` method with a specific OpenCL device:

```java
System.setProperty("use-device", "GPU.1"); // Set the device to use
String query = "apple";
List<String> targets = Arrays.asList("banana", "orange", "pear");
String algorithm = IConstants.EDIT_DISTANCE;
List<Result> results = calculateSimilarity(query, targets, algorithm);
```

## Using FASTSICO as a Command-Line Utility

FASTSICO can also be used as a command-line utility to find the most similar strings to a query string from a file containing a list of strings. The command has the following syntax:

```sh
java -cp lib/*:fastsico-1.0.0.jar com.hajhouj.oss.fastsico.tools.Find <data-file> <query> <top-n> <output-format>
```

* `<data-file>` is the path to the file containing the list of target strings.
* `<query>` is the query string.
* `<top-n>` is the number of top results to return.
* `<output-format>` is the output format, which can be `json`, `xml`, or `csv`.

By default, the command will use the default OpenCL device available with the best configuration. However, if you need to specify a specific device, you can use the `use-device` system property, as shown in the example below:

```sh
java -Duse-device=GPU.0 -cp lib/*:fastsico-1.0.0.jar com.hajhouj.oss.fastsico.tools.Find <data-file> <query> <top-n> <output-format>
```

Here, `GPU.0` means to use the first OpenCL device of type GPU.

## DevicesList Utility Command

The DevicesList utility command is a helpful tool for listing the available OpenCL devices on the host machine, and for determining the query string to use when specifying a particular device using the `use-device` system property. The DevicesList utility command list the available OpenCL devices on the host machine, along with their associated query strings.&#x20;

To use the DevicesList utility command, run the following command:

```sh
java -cp lib/*:fastsico-1.0.0.jar com.hajhouj.oss.fastsico.tools.DevicesList
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
