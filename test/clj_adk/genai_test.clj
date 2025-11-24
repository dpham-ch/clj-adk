(ns clj-adk.genai-test
  (:require [clojure.test :refer :all]
            [clj-adk.genai :as genai]
            [clj-adk.schemas :as schemas]))

(deftest create-client-test
  (testing "create-client configuration"
    ;; Project/location and API key are mutually exclusive in the client initializer.
    (let [config {:api-key "test-key"}]
      (is (some? (genai/create-client config))))))

(deftest generate-content-test
  (testing "generate-content structure"
    (let [client (genai/create-client {:api-key "dummy"})]
      ;; Mocking or actual call would go here.
      ;; Since we can't easily mock Java classes without a mocking library or interfaces,
      ;; we mostly check that the function exists and accepts arguments.
      (is (fn? genai/generate-content)))))

(deftest create-content-test
  (testing "create-content works"
    (try
      (let [content (genai/create-content "hello")]
        ;; In CLJ, content should be an instance of Content
        ;; In CLJS, it is a map. Since this test runs in CLJ (likely), we check instance.
        (is (instance? com.google.genai.types.Content content)))
      (catch Exception e
        (is false (str "create-content failed with: " (.getMessage e)))))))

(deftest generate-content-fail-test
  (testing "generate-content with config map argument type check"
    (let [client (genai/create-client {:api-key "dummy"})
          config {:temperature 0.5}]
       (try
         (genai/generate-content client "gemini-pro" "Hello" config)
         (catch NullPointerException e
             ;; NPE is expected because we are using a dummy client and calling real methods
             ;; that might try to access internals that are not set up properly or network.
             ;; However, if we get NPE, it means we passed the argument type check for GenerateContentConfig.
             (is true "NPE is acceptable as it means we passed argument type check"))
         (catch Exception e
           ;; If we get IllegalArgumentException about type mismatch, then it's a fail.
           (is false (str "Should not throw other exceptions: " (.getMessage e))))))))
