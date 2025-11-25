# clj-adk Usage Guide

This guide provides a comprehensive overview of the `clj-adk` library, covering everything from installation to advanced usage patterns.

## Table of Contents

- [Installation](#installation)
- [Client Creation](#client-creation)
- [Content Generation](#content-generation)
- [Schemas](#schemas)
  - [ClientSchema](#clientschema)
  - [GeminiSchema](#geminischema)
  - [ToolSchema](#toolschema)
  - [Agent Schemas](#agent-schemas)
    - [LlmAgentSchema](#llmagentschema)
    - [SequentialAgentSchema](#sequentialagentschema)
    - [ParallelAgentSchema](#parallelagentschema)
    - [LoopAgentSchema](#loopagentschema)

## Installation

`clj-adk` is a Clojure and ClojureScript library that wraps the Google Generative AI SDK. To get started, add the following dependency to your `deps.edn`:

```clojure
clj-adk/clj-adk {:git/url "https://github.com/your-org/clj-adk" :sha "your-sha"}
```

For ClojureScript users, you'll also need to install the `@google/generative-ai` npm package:

```bash
npm install @google/generative-ai
```

## Client Creation

To interact with the Google Generative AI API, you first need to create a client. The `create-client` function takes a configuration map, which is validated against the `ClientSchema`.

### `create-client`

**Signature:** `(create-client config)`

**Parameters:**

- `config`: A map containing the client configuration.

**Example:**

```clojure
(require '[clj-adk.genai :as genai])

(def client
  (genai/create-client {:api-key "YOUR_API_KEY"}))
```

## Content Generation

Once you have a client, you can generate content using the `generate-content` function. This function can be used with both Clojure and ClojureScript.

### `generate-content`

**Signature:** `(generate-content client model-id content & [config])`

**Parameters:**

- `client`: The client created with `create-client`.
- `model-id`: The ID of the model to use (e.g., `"gemini-pro"`).
- `content`: The content to send to the model, as a string.
- `config` (optional): A configuration map for content generation.

**Example:**

```clojure
(let [response (genai/generate-content client "gemini-pro" "Hello, world!")]
  (println response))
```

In ClojureScript, `generate-content` returns a Promise.

## Schemas

`clj-adk` uses Malli to define and validate schemas for various data structures.

### `ClientSchema`

The `ClientSchema` validates the configuration map for the `create-client` function.

**Schema:**

```clojure
[:map
 [:api-key {:optional true} :string]
 [:vertex-ai {:optional true} :boolean]
 [:project {:optional true} :string]
 [:location {:optional true} :string]]
```

### `GeminiSchema`

The `GeminiSchema` validates the configuration for Gemini models.

**Schema:**

```clojure
[:map
 [:model-name :string]
 [:api-key {:optional true} :string]]
```

### `ToolSchema`

The `ToolSchema` validates a tool, which can be any object that implements the `BaseTool` interface in Clojure.

### Agent Schemas

The following schemas are used to validate agent configurations.

#### `LlmAgentSchema`

**Schema:**

```clojure
[:map
 [:name :string]
 [:description {:optional true} :string]
 [:instruction {:optional true} :string]
 [:model {:optional true} [:or :string :any]]
 [:tools {:optional true} [:vector ToolSchema]]]
```

#### `SequentialAgentSchema`

**Schema:**

```clojure
[:map
 [:name :string]
 [:description {:optional true} :string]
 [:agents [:vector AgentSchema]]]
```

#### `ParallelAgentSchema`

**Schema:**

```clojure
[:map
 [:name :string]
 [:description {:optional true} :string]
 [:agents [:vector AgentSchema]]]
```

#### `LoopAgentSchema`

**Schema:**

```clojure
[:map
 [:name :string]
 [:description {:optional true} :string]
 [:agent AgentSchema]
 [:max-iterations {:optional true} :int]]
```
