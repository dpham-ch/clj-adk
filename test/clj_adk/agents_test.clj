(ns clj-adk.agents-test
  (:require [clojure.test :refer [deftest is testing]]
            [clj-adk.agents :as agents]
            [clj-adk.models :as models]
            [clj-adk.agents.invocation-context :as invocation-context])
  (:import [com.google.adk.agents LlmAgent SequentialAgent ParallelAgent LoopAgent InvocationContext]
           [com.google.adk.models Gemini]
           [com.google.adk.tools FunctionTool]))

(deftest test-agents-wrappers
  (testing "LlmAgent wrapper"
    (let [agent (agents/create {:type :llm
                                :name "test-llm-agent"
                                :model (models/create {:type :gemini
                                                       :model-name "gemini-1.5-pro"
                                                       :api-key "dummy"})})]
      (is (instance? LlmAgent agent))
      (is (= "test-llm-agent" (.name agent)))
      (is (some? (agents/instruction agent)))
      (is (empty? (agents/tools agent)))))

  (testing "SequentialAgent wrapper"
    (let [sub-agent (agents/create {:type :llm
                                    :name "sub-agent"
                                    :model (models/create {:type :gemini
                                                           :model-name "gemini-1.5-pro"
                                                           :api-key "dummy"})})
          agent (agents/create {:type :sequential
                                :name "test-seq-agent"
                                :agents [sub-agent]})]
      (is (instance? SequentialAgent agent))
      (is (= "test-seq-agent" (.name agent)))
      (is (= 1 (count (agents/sub-agents agent))))))

  (testing "ParallelAgent wrapper"
    (let [sub-agent (agents/create {:type :llm
                                    :name "sub-agent"
                                    :model (models/create {:type :gemini
                                                           :model-name "gemini-1.5-pro"
                                                           :api-key "dummy"})})
          agent (agents/create {:type :parallel
                                :name "test-par-agent"
                                :agents [sub-agent]})]
      (is (instance? ParallelAgent agent))
      (is (= "test-par-agent" (.name agent)))
      (is (= 1 (count (agents/sub-agents agent))))))

  (testing "LoopAgent wrapper"
    (let [sub-agent (agents/create {:type :llm
                                    :name "sub-agent"
                                    :model (models/create {:type :gemini
                                                           :model-name "gemini-1.5-pro"
                                                           :api-key "dummy"})})
          agent (agents/create {:type :loop
                                :name "test-loop-agent"
                                :agent sub-agent})]
      (is (instance? LoopAgent agent))
      (is (= "test-loop-agent" (.name agent)))
      (is (= 1 (count (agents/sub-agents agent)))))))

(deftest test-invocation-context-wrapper
  (testing "InvocationContext wrapper"
    (let [agent (agents/create {:type :llm
                                :name "test-agent"
                                :model (models/create {:type :gemini
                                                       :model-name "gemini-1.5-pro"
                                                       :api-key "dummy"})})
          context (invocation-context/create {:agent agent})]
      (is (instance? InvocationContext context))
      (is (= agent (.agent context))))))
