(ns clj-adk.core-test
  (:require [clojure.test :refer :all]
            [clj-adk.core :refer :all])
  (:import [com.google.adk.agents LlmAgent SequentialAgent ParallelAgent LoopAgent]
           [com.google.adk.models Gemini]
           [com.google.adk.tools FunctionTool]
           [java.util Map]))

(deftest gemini-model-test
  (let [model (gemini-model {:model-name "gemini-pro" :api-key "TEST_KEY"})]
    (is (instance? Gemini model))))

(deftest llm-agent-test
  (let [agent (llm-agent {:name "test-agent"
                          :description "A test agent"
                          :instruction "You are a test agent"
                          :model "gemini-pro"})]
    (is (instance? LlmAgent agent))
    ;; Note: bean and from-java do not work here as LlmAgent does not follow JavaBean conventions (getters).
    ;; We verify properties using the fluent accessors.
    (is (= "test-agent" (.name agent)))
    (is (= "A test agent" (.description agent)))
    ;; instruction() returns an Instruction object (record), so we need to call instruction() on it.
    (is (= "You are a test agent" (.instruction (.instruction agent))))))

(deftest sequential-agent-test
  (let [agent1 (llm-agent {:name "agent1" :model "gemini-pro"})
        agent2 (llm-agent {:name "agent2" :model "gemini-pro"})
        seq-agent (sequential-agent {:name "seq-agent"
                                     :agents [agent1 agent2]})]
    (is (instance? SequentialAgent seq-agent))
    (is (= "seq-agent" (.name seq-agent)))))

(deftest parallel-agent-test
  (let [agent1 (llm-agent {:name "agent1" :model "gemini-pro"})
        agent2 (llm-agent {:name "agent2" :model "gemini-pro"})
        par-agent (parallel-agent {:name "par-agent"
                                   :agents [agent1 agent2]})]
    (is (instance? ParallelAgent par-agent))
    (is (= "par-agent" (.name par-agent)))))

(deftest loop-agent-test
  (let [agent1 (llm-agent {:name "agent1" :model "gemini-pro"})
        loop-agent (loop-agent {:name "loop-agent"
                                :agent agent1})]
    (is (instance? LoopAgent loop-agent))
    (is (= "loop-agent" (.name loop-agent)))))
