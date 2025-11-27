# clj-adk

A Clojure and ClojureScript library that wraps the [Google Generative AI SDK](https://ai.google.dev/docs) (Gemini).

## Description

`clj-adk` provides a convenient interface for interacting with Google's Gemini models from both Clojure (JVM) and ClojureScript. It simplifies client creation, content generation, and structured data handling using Malli schemas.

## Installation

### Clojure

Add the following dependency to your `deps.edn`:

```clojure
com.google.adk/google-adk {:mvn/version "0.3.0"}
```

*Note: Check for the latest version on Maven Central or use a git dependency if you are building from source.*

```clojure
clj-adk/clj-adk {:git/url "https://github.com/your-org/clj-adk" :sha "HEAD"}
```

### ClojureScript

You will also need to install the `@google/generative-ai` npm package:

```bash
npm install @google/generative-ai
```

## Usage

See the [Usage Guide](docs/usage.md) for detailed documentation.

### Quick Start

```clojure
(require '[clj-adk.genai :as genai])

;; Create a client
(def client (genai/create-client {:api-key "YOUR_API_KEY"}))

;; Generate content
(genai/generate-content client "gemini-pro" "Hello, world!")
```

## Documentation

- [Usage Guide](docs/usage.md): Comprehensive guide on installation, client creation, content generation, and schemas.
- [Clojure Docs](docs/clojure.md): Notes for developers working on this project.

## License

This project is licensed under the Apache License 2.0 - see the [LICENSE](LICENSE) file for details.
