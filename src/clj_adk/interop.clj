(ns clj-adk.interop
  (:require [camel-snake-kebab.core :as csk]
            [clojure.java.data.builder :as builder]))

(defn ->camel-case-map [m]
  (update-keys m (comp keyword csk/->camelCaseString name)))

(defn props->java [builder-class builder-instance props]
  (let [java-props (->camel-case-map props)]
    (builder/to-java builder-class builder-instance java-props {})))
