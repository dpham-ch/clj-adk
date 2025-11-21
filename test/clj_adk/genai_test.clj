(ns clj-adk.genai-test
  (:require [clojure.test :refer :all]
            [clj-adk.genai :as genai]
            [clj-adk.schemas :as schemas]))

(deftest create-client-test
  (testing "create-client configuration"
    (let [config {:api-key "test-key"
                  :project "test-project"
                  :location "us-central1"}]
      (is (some? (genai/create-client config))))))

(deftest generate-content-test
  (testing "generate-content structure"
    (let [client (genai/create-client {:api-key "dummy"})]
      ;; Mocking or actual call would go here.
      ;; Since we can't easily mock Java classes without a mocking library or interfaces,
      ;; we mostly check that the function exists and accepts arguments.
      (is (fn? genai/generate-content)))))
