Json2Java4Idea
=====
[![CircleCI](https://circleci.com/gh/t28hub/json2java4idea/tree/master.svg?style=shield&circle-token=30a68b03ab00d912be2f9f93b619ca8f4e36f061)](https://circleci.com/gh/t28hub/json2java4idea/tree/master)
[![Apache 2.0](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/t28hub/json2java4idea/blob/master/LICENSE)

**Json2Java4Idea** is a plugin which generates Java class from JSON.

## Table of Contents
* [Background](#background)
* [Installation](#installation)
* [Troubleshooting](#troubleshooting)
* [License](#license)

<a name="background"></a>
## Background
There are several JSON Serializing and deserializing library something like Jackson or Gson, and you might use one of these library.<br/>
However implementing a class which is converted from JSON is a bored stuff.<br/>
Json2Java4Idea could be a solution for the stuff.<br/>
Also this plugin supports following libraries.<br/>
* [Jackson](https://github.com/FasterXML/jackson)
* [GSON](https://github.com/google/gson)
* [Moshi](https://github.com/square/moshi)

<a name="installation"></a>
## Installation
1. Open the **Settings/Preferences** dialog
1. In the left pane, select **Plugins**.
1. In the right pane, click the **Browse repositories...** button.
1. In the dialog which opens, enter **Json2Java4Idea** into the search box.
1. In the right pane, click the **Install** button.
1. After installation, restart your IDE.

<a name="troubleshooting"></a>
## Troubleshooting
Please [raise an issue](https://github.com/t28hub/json2java4idea/issues/new) on this repository.

<a name="license"></a>
## License
```
Copyright (c) 2017 Tatsuya Maki

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```