Json2Java4Idea
=====
[![CircleCI](https://circleci.com/gh/t28hub/json2java4idea/tree/master.svg?style=shield&circle-token=30a68b03ab00d912be2f9f93b619ca8f4e36f061)](https://circleci.com/gh/t28hub/json2java4idea/tree/master)
[![Apache 2.0](https://img.shields.io/badge/license-Apache%202.0-blue.svg)](https://github.com/t28hub/json2java4idea/blob/master/LICENSE)

**Json2Java4Idea** is a plugin which generates Java class from JSON.
This plugin supports the following libraries for serializing and deserializing JSON.
* [Jackson](https://github.com/FasterXML/jackson)
* [GSON](https://github.com/google/gson)
* [Moshi](https://github.com/square/moshi)

## Table of Contents
* Background
* Installation
* Example
* Trouble shooting
* License

## Background

## Installation

## Example
```json
{
  "id": 1,
  "title": "Introducing Json2Java4Idea",
  "private": false,
  "comments": [
    {
      "id": 1,
      "body": "Awesome!"
    },
    {
      "id": 2,
      "body": "Looks nice to me"
    }
  ]
}
```

```java
public class Post {
    private final int id;
    private final String title;
    private final boolean aPrivate;
    private final List<Comments> comments;

    public Post(int id, String title, boolean aPrivate, List<Comments> comments) {
        this.id = id;
        this.title = title;
        this.aPrivate = aPrivate;
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public boolean isPrivate() {
        return aPrivate;
    }

    public List<Comments> getComments() {
        return comments;
    }

    public static class Comments {
        private final int id;
        private final String body;

        public Comments(int id, String body) {
            this.id = id;
            this.body = body;
        }

        public int getId() {
            return id;
        }

        public String getBody() {
            return body;
        }
    }
}
```

## Trouble shooting
Please raise an issue on this repository.

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