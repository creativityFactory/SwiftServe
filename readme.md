# SwiftServe Framework
SwiftServe framework is a web framework based on jakarta EE and follow the pattern MVC2. It can also automate the creation of web services in form of rest api basing only on models.

### Goals
* Follow the pattern MVC2.
* Robust routing.
* Automate the creation of web services.
* Simplicity.
* Easy way for set up middleware.
* Executable for generating applications quickly.

### Download
For the moment there is only jar version, we are working to provide it in form of maven dependency.

### Requirements
#### Minimum Java version
-  Java 8 +
#### Dependencies
- jakarta.servlet: [jakarta.servlet-api:5.0.0](https://github.com/jakartaee/servlet)
- com.google.code.gson: [gson:2.10.1](https://github.com/google/gson)
- org.reflections: [reflections:0.10.2](https://github.com/ronmamo/reflections)
- org.atteo: [evo-inflector:1.3](https://github.com/atteo/evo-inflector)

### Documentation
* [API Javadoc](): Documentation for the current release.
* [User guide](UserGuide.md): This guide contains examples on how to use our framework.
* [Design document](DesignDoc.md): This document discusses issues we faced while designing this framework.

### Building

SwiftServe uses Maven to build the project:
```
mvn clean verify
```

### License

SwiftServe is released under the [Apache 2.0 license](LICENSE).

```
Copyright 2023 Amine EL HALILI and Marouane BENABDELKADER.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    https://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
