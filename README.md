<!--
  Licensed to ObjectStyle LLC under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ObjectStyle LLC licenses
  this file to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
  -->

[![build test deploy](https://github.com/bootique/bootique-linkmove/actions/workflows/maven.yml/badge.svg)](https://github.com/bootique/bootique-linkmove/actions/workflows/maven.yml)
[![Maven Central](https://img.shields.io/maven-central/v/io.bootique.linkmove/bootique-linkmove.svg?colorB=brightgreen)](https://search.maven.org/artifact/io.bootique.linkmove/bootique-linkmove/)

# bootique-linkmove

Provides [LinkMove](https://github.com/nhl/link-move) integration with [Bootique](http://bootique.io). Configures 
connectors and extractor locations via Bootique, enhances LinkMove with advanced connectors (such as HTTP client).

### Prerequisites

Include ```bootique-bom```:
```xml
<dependencyManagement>
    <dependencies>
        <dependency>
            <groupId>io.bootique.bom</groupId>
            <artifactId>bootique-bom</artifactId>
            <version>3.0-M4</version>
            <type>pom</type>
            <scope>import</scope>
        </dependency>
    </dependencies>
</dependencyManagement>
```
      
# Setup

## Add bootique-linkmove to your build tool
**Maven**
```xml
<dependency>
    <groupId>io.bootique.linkmove</groupId>
    <artifactId>bootique-linkmove3</artifactId>
</dependency>

<!-- Optionally, if you need to process JSON sources include JSON module-->
<dependency>
    <groupId>io.bootique.linkmove</groupId>
    <artifactId>bootique-linkmove3-json</artifactId>
</dependency>

<!-- Optionally, if you need to process data coming from REST APIs include REST module-->
<dependency>
    <groupId>io.bootique.linkmove</groupId>
    <artifactId>bootique-linkmove3-rest</artifactId>
</dependency>
```

## Use LinkMove

Now you can inject `LmRuntime` in your code, build and execute `LmTasks`. 

## Example Project

[bootique-linkmove-demo](https://github.com/bootique-examples/bootique-linkmove-demo)

