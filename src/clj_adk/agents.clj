(ns clj-adk.agents
  (:require [clj-adk.interop :as interop]
            [clj-adk.schemas :as schemas]
            [malli.core :as m])
  (:import [com.google.adk.agents LlmAgent SequentialAgent ParallelAgent LoopAgent BaseAgent]
           [com.google.adk.agents InvocationContext]))

(defn- llm-agent
  "Creates an LlmAgent instance."
  [config]
  (if (m/validate schemas/LlmAgentSchema config)
    (interop/props->java LlmAgent (LlmAgent/builder) config)
    (throw (ex-info "Invalid LlmAgent configuration" (m/explain schemas/LlmAgentSchema config)))))

(defn- sequential-agent
  "Creates a SequentialAgent instance."
  [config]
  (if (m/validate schemas/SequentialAgentSchema config)
    (let [config (if (:agents config)
                   (-> config
                       (assoc :sub-agents (:agents config))
                       (dissoc :agents))
                   config)]
      (interop/props->java SequentialAgent (SequentialAgent/builder) config))
    (throw (ex-info "Invalid SequentialAgent configuration" (m/explain schemas/SequentialAgentSchema config)))))

(defn- parallel-agent
  "Creates a ParallelAgent instance."
  [config]
  (if (m/validate schemas/ParallelAgentSchema config)
    (let [config (if (:agents config)
                   (-> config
                       (assoc :sub-agents (:agents config))
                       (dissoc :agents))
                   config)]
      (interop/props->java ParallelAgent (ParallelAgent/builder) config))
    (throw (ex-info "Invalid ParallelAgent configuration" (m/explain schemas/ParallelAgentSchema config)))))

(defn- loop-agent
  "Creates a LoopAgent instance."
  [config]
  (if (m/validate schemas/LoopAgentSchema config)
    (let [config (if (:agent config)
                   (-> config
                       (assoc :sub-agents [(:agent config)])
                       (dissoc :agent))
                   config)]
      (interop/props->java LoopAgent (LoopAgent/builder) config))
    (throw (ex-info "Invalid LoopAgent configuration" (m/explain schemas/LoopAgentSchema config)))))

;; Construction

(defmulti create
  "Creates an agent based on the :type key in the config."
  (fn [config]
    (let [type (:type config)]
      (if (m/validate schemas/AgentType type)
        type
        :invalid))))

(defmethod create :llm [config]
  (llm-agent (dissoc config :type)))

(defmethod create :sequential [config]
  (sequential-agent (dissoc config :type)))

(defmethod create :parallel [config]
  (parallel-agent (dissoc config :type)))

(defmethod create :loop [config]
  (loop-agent (dissoc config :type)))

(defmethod create :invalid [config]
  (throw (ex-info "Invalid or missing agent type in config"
                  {:config config
                   :allowed-types (m/form schemas/AgentType)})))

;; Common Execution

(defn run-live
  "Runs the agent synchronously.
   Args:
     invocation-context: The InvocationContext."
  [^BaseAgent agent {:keys [invocation-context]}]
  (.runLive agent invocation-context))

(defn run-async
  "Runs the agent asynchronously.
   Args:
     invocation-context: The InvocationContext."
  [^BaseAgent agent {:keys [invocation-context]}]
  (.runAsync agent invocation-context))

;; Multimethod Accessors

(defmulti instruction
  "Returns the instruction of the agent."
  class)

(defmethod instruction LlmAgent [^LlmAgent agent]
  (.instruction agent))

(defmethod instruction :default [_] nil)


(defmulti tools
  "Returns the tools of the agent."
  class)

(defmethod tools LlmAgent [^LlmAgent agent]
  (.tools agent))

(defmethod tools :default [_] nil)


(defmulti model
  "Returns the model of the agent."
  class)

(defmethod model LlmAgent [^LlmAgent agent]
  (.model agent))

(defmethod model :default [_] nil)


(defmulti sub-agents
  "Returns the list of sub-agents."
  class)

(defmethod sub-agents SequentialAgent [^SequentialAgent agent]
  (.subAgents agent))

(defmethod sub-agents ParallelAgent [^ParallelAgent agent]
  (.subAgents agent))

(defmethod sub-agents LoopAgent [^LoopAgent agent]
  (.subAgents agent))

(defmethod sub-agents :default [_] nil)
