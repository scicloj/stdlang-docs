(ns render
  (:require [scicloj.clay.v2.api :as clay]))

(clay/make! {:format [:quarto :html]
             :base-source-path "notebooks"
             :source-path (->> "notebooks/chapters.edn"
                               slurp
                               clojure.edn/read-string
                               (map #(format "stdlang_book/%s.clj" %))
                               (cons "index.clj"))
             :base-target-path "docs"
             :book {:title "std.lang documentation"}
             :clean-up-target-dir true})
