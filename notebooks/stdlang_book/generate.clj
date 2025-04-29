(ns stdlang-book.generate
  (:require [std.lang :as l]
            [clojure.string :as str]
            [scicloj.kindly.v4.kind :as kind]
            [rewrite-clj.parser :as parser]
            [rewrite-clj.node :as node]))

;; Here we define `:code` as a way to use the transpiler
;; to generate Javascript string, but not use it in any runtime.

^:kind/println
(l/script+ [:code :js]
           {:require [[xt.lang.base-lib :as k]
                      [xt.lang.base-iter :as it]]})

;; Here we define `:live` as a way to use the transpiler
;; go generate Javascript string, and run it in a Node.js runtime.

^:kind/println
(l/script+ [:live :js]
           {:runtime :basic
            :require [[xt.lang.base-lib :as k]
                      [xt.lang.base-iter :as it]]})


(->> "notebooks/stdlang_book/examples.clj"
     slurp
     parser/parse-string-all
     node/children
     (mapcat
      (fn [node]
        (let [tag (node/tag node)
              string (node/string node)]
          (prn [tag string])
          (case tag
            :newline nil
            :comment [(->> string
                           str/split-lines
                           (map #(str/replace %  #"^;; " ""))
                           (str/join "\n")
                           kind/md)]
            ;; else
            (let [form (read-string string)]
              (prn [:form form])
              [(kind/md "form:")
               (->> string
                    (format "::: {.sourceClojure}\n```js\n%s\n```\n:::\n")
                    kind/md)
               (kind/md "generated code:")
               (->> form
                    (list 'l/! [:code])
                    eval
                    (format "::: {.generatedCode}\n```js\n%s\n```\n:::\n")
                    kind/md)
               (kind/md "output:")
               (eval (list 'l/! [:live] form))])))))
     kind/fragment)
