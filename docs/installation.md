# Installation

Steps for installing the FASTSICO library as a Maven dependency in your project:

* Ensure that you have Maven installed on your machine. You can check this by running the command `mvn -v` in a terminal or command prompt. If Maven is not installed, you can download it from the official website at [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi).
* Create a new Maven project or open an existing one where you want to add the FASTSICO library as a dependency.
* Inside the `pom.xml` file, add the following XML code inside the `<dependencies>` section:

```
<dependency>
    <groupId>com.hajhouj.oss.fastsico</groupId>
    <artifactId>fastsico</artifactId>
    <version>1.0.0</version>
</dependency>
```

* Run the command `mvn install` in a terminal or command prompt in the root directory of your project. This will download the FASTSICO library and install it to your local Maven repository.

After following these steps, you can use the FASTSICO library in your Java code by importing the classes you need and calling their methods.
