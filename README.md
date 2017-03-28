Json2Java4Idea
=====
[![CircleCI](https://circleci.com/gh/t28hub/json2java4idea/tree/master.svg?style=shield&circle-token=30a68b03ab00d912be2f9f93b619ca8f4e36f061)](https://circleci.com/gh/t28hub/json2java4idea/tree/master)
[![Apache 2.0](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/t28hub/json2java4idea/blob/master/LICENSE)
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-Json2Java4Idea-blue.svg)](https://android-arsenal.com/details/1/5450)
[![Codacy Grade](https://api.codacy.com/project/badge/Grade/9fe5c57cf41a4cefb571dccdb68de994)](https://www.codacy.com/app/t28/json2java4idea?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=t28hub/json2java4idea&amp;utm_campaign=Badge_Grade)
[![Codacy Coverage](https://api.codacy.com/project/badge/Coverage/9fe5c57cf41a4cefb571dccdb68de994)](https://www.codacy.com/app/t28/json2java4idea?utm_source=github.com&utm_medium=referral&utm_content=t28hub/json2java4idea&utm_campaign=Badge_Coverage)

**Json2Java4Idea** is a plugin which generates Java class from JSON.

![Demo](demo.gif)

## Table of Contents
* [Background](#background)
* [Installation](#installation)
* [Settings](#settings)
* [Troubleshooting](#troubleshooting)
* [License](#license)

<a name="background"></a>
## Background
There are several JSON Serializing and deserializing library something like Jackson or Gson.<br/>
However implementing a class which is converted from JSON is a bored stuff.<br/>
Json2Java4Idea could be a solution for the stuff.<br/>
The plugin generates immutable Java class from JSON and supports following libraries.<br/>
* [Jackson](https://github.com/FasterXML/jackson)
* [GSON](https://github.com/google/gson)
* [Moshi](https://github.com/square/moshi)

<a name="installation"></a>
## Installation
1. Open the **Settings/Preferences** dialog.
1. In the left pane, select **Plugins**.
1. In the right pane, click the **Browse repositories...** button.
1. In the dialog which opens, enter **Json2Java4Idea** into the search box.
1. In the right pane, click the **Install** button.
1. After installation, restart your IDE.

<a name="settings"></a>
## Settings
1. Open the **Settings/Preferences** dialog.
1. In the left pane, select **Other Settings**.
1. In the right pane, click the **Json2Java4Idea** link.
1. After configuration changing finished, click the **Apply** button.

Currently there are 3 types of settings you can configure.
1. Class style
1. Prefix of class name
1. Suffix of class name

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