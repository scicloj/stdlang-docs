^:kindly/hide-code
(ns index
  (:require [scicloj.kindly.v4.kind :as kind]
            [clojure.string :as str]))

;; # Preface

^:kindly/hide-code
(->> "README.md"
     slurp
     str/split-lines
     (drop 2)
     (str/join "\n")
     kind/md)

;; ## Chapters in this book

^:kindly/hide-code
(defn chapter->title [chapter]
  (or (some->> chapter
               (format "notebooks/stdlang_book/%s.clj")
               slurp
               str/split-lines
               (filter #(re-matches #"^;; # .*" %))
               first
               (#(str/replace % #"^;; # " "")))
      chapter))

^:kindly/hide-code
(->> "notebooks/chapters.edn"
     slurp
     clojure.edn/read-string
     (map (fn [chapter]
            (prn [chapter (chapter->title chapter)])
            (format "\n- [%s](stdlang_book.%s.html)\n"
                    (chapter->title chapter)
                    chapter)))
     (str/join "\n")
     kind/md)
