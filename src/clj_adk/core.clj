(ns clj-adk.core
  (:require [malli.core :as m]
            [camel-snake-kebab.core :as csk]
            [clojure.java.data.builder :as builder]
            [clj-adk.schemas :as schemas])
  (:import [com.google.adk.agents LlmAgent SequentialAgent ParallelAgent LoopAgent]
           [com.google.adk.models Gemini]
           [com.google.adk.tools FunctionTool]
           [java.util Map List]))

;; Helper

(defn- ->camel-case-map [m]
  (update-keys m (comp keyword csk/->camelCaseString name)))

(defn- props->java [builder-class builder-instance props]
  (let [java-props (->camel-case-map props)]
    (builder/to-java builder-class builder-instance java-props {})))

;; Factories

(defn gemini-model
  "Creates a Gemini model instance."
  [config]
  (if (m/validate schemas/GeminiSchema config)
    (props->java Gemini (Gemini/builder) config)
    (throw (ex-info "Invalid Gemini configuration" (m/explain schemas/GeminiSchema config)))))

(defn function-tool
  "Creates a function tool."
  [clazz method-name]
  (FunctionTool/create clazz method-name))

(defn llm-agent
  "Creates an LlmAgent instance."
  [config]
  (if (m/validate schemas/LlmAgentSchema config)
    (props->java LlmAgent (LlmAgent/builder) config)
    (throw (ex-info "Invalid LlmAgent configuration" (m/explain schemas/LlmAgentSchema config)))))

(defn sequential-agent
  "Creates a SequentialAgent instance."
  [config]
  (if (m/validate schemas/SequentialAgentSchema config)
    (let [config (if (:agents config)
                   (-> config
                       (assoc :sub-agents (:agents config))
                       (dissoc :agents))
                   config)]
      (props->java SequentialAgent (SequentialAgent/builder) config))
    (throw (ex-info "Invalid SequentialAgent configuration" (m/explain schemas/SequentialAgentSchema config)))))

(defn parallel-agent
  "Creates a ParallelAgent instance."
  [config]
  (if (m/validate schemas/ParallelAgentSchema config)
    (let [config (if (:agents config)
                   (-> config
                       (assoc :sub-agents (:agents config))
                       (dissoc :agents))
                   config)]
      (props->java ParallelAgent (ParallelAgent/builder) config))
    (throw (ex-info "Invalid ParallelAgent configuration" (m/explain schemas/ParallelAgentSchema config)))))

(defn loop-agent
  "Creates a LoopAgent instance."
  [config]
  (if (m/validate schemas/LoopAgentSchema config)
    (let [config (if (:agent config)
                   (-> config
                       (assoc :sub-agents [(:agent config)])
                       (dissoc :agent))
                   config)]
      (props->java LoopAgent (LoopAgent/builder) config))
    (throw (ex-info "Invalid LoopAgent configuration" (m/explain schemas/LoopAgentSchema config)))))
