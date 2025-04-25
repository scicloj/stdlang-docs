(ns
 stdlang-book.walkthrough-generated-test
 (:require [std.lang :as l] [clojure.test :refer [deftest is]]))


(def
 var3_line20
 (require
  '[scicloj.kindly.v4.kind :as kind]
  '[scicloj.kindly.v4.api :as kindly]))


(def var5_line28 (l/script :js {:runtime :basic}))


(def var6_line31 (!.js ((fn [] (return (Math.pow 2 3))))))


(def var8_line37 (!.js [(+ 1 2 3 4) (Math.pow 4 4) (typeof [1 2 3 4])]))


(deftest test9_line42 (is ([10 256 "object"] var8_line37)))


(def var11_line50 (l/script+ [:code :js]))


(def var13_line56 (l/script+ [:live :js] {:runtime :basic}))


(def var15_line62 [(l/! [:code] (+ 1 2)) (l/! [:live] (+ 1 2))])


(def
 var16_line67
 (defn
  display-output-js-code
  [code]
  (kind/md (format "```js\n%s\n```" code))))


(def
 var17_line72
 (defn
  display-output-clj-code
  [code]
  (kind/md (format "```clj\n%s\n```" code))))


(def
 var18_line77
 (defn
  display-output-format
  [& forms]
  (with-meta
   {:column-names [:form :code :live],
    :row-vectors
    (->>
     forms
     (mapv
      (fn
       [[form code-output rt-output]]
       [(display-output-clj-code (pr-str form))
        (display-output-js-code (pr-str code-output))
        (display-output-clj-code (pr-str rt-output))])))}
   {:kind/table true, :kindly/hide-code true})))


(def
 var19_line91
 (defmacro
  display-output-table
  [& forms]
  (with-meta
   {:column-names [:form :code :live],
    :row-vectors
    (->>
     forms
     (mapv
      (fn
       [form]
       (clojure.core/vec
        (clojure.core/sequence
         (clojure.core/seq
          (clojure.core/concat
           (clojure.core/list
            (clojure.core/sequence
             (clojure.core/seq
              (clojure.core/concat
               (clojure.core/list
                'stdlang-book.walkthrough/display-output-clj-code)
               (clojure.core/list
                (clojure.core/sequence
                 (clojure.core/seq
                  (clojure.core/concat
                   (clojure.core/list 'clojure.core/pr-str)
                   (clojure.core/list
                    (clojure.core/sequence
                     (clojure.core/seq
                      (clojure.core/concat
                       (clojure.core/list 'quote)
                       (clojure.core/list form)))))))))))))
           (clojure.core/list
            (clojure.core/sequence
             (clojure.core/seq
              (clojure.core/concat
               (clojure.core/list
                'stdlang-book.walkthrough/display-output-js-code)
               (clojure.core/list
                (clojure.core/sequence
                 (clojure.core/seq
                  (clojure.core/concat
                   (clojure.core/list 'clojure.core/pr-str)
                   (clojure.core/list
                    (clojure.core/sequence
                     (clojure.core/seq
                      (clojure.core/concat
                       (clojure.core/list 'std.lang/!)
                       (clojure.core/list
                        (clojure.core/vec
                         (clojure.core/sequence
                          (clojure.core/seq
                           (clojure.core/concat
                            (clojure.core/list :code))))))
                       (clojure.core/list form)))))))))))))
           (clojure.core/list
            (clojure.core/sequence
             (clojure.core/seq
              (clojure.core/concat
               (clojure.core/list 'clojure.core/pr-str)
               (clojure.core/list
                (clojure.core/sequence
                 (clojure.core/seq
                  (clojure.core/concat
                   (clojure.core/list 'std.lang/!)
                   (clojure.core/list
                    (clojure.core/vec
                     (clojure.core/sequence
                      (clojure.core/seq
                       (clojure.core/concat
                        (clojure.core/list :live))))))
                   (clojure.core/list form))))))))))))))))}
   {:kind/table true, :kindly/hide-code true})))


(def
 var21_line124
 (display-output-format ['nil (l/! [:code] nil) (l/! [:live] nil)]))


(def
 var23_line131
 (display-output-format
  ['undefined (l/! [:code] undefined) (l/! [:live] undefined)]))


(def
 var25_line138
 (display-output-format
  ['true (l/! [:code] true) (l/! [:live] true)]
  ['false (l/! [:code] false) (l/! [:live] false)]))


(def
 var27_line148
 (display-output-format
  ['"3" (l/! [:code] "3") (l/! [:live] "3")]
  ["Hello World"
   (l/! [:code] "Hello World")
   (l/! [:live] "Hello World")]))


(def
 var29_line159
 (display-output-format
  ['3 (l/! [:code] 3) (l/! [:live] 3)]
  ['1.5 (l/! [:code] 1.5) (l/! [:live] 1.5)]
  ['1.54444444444444
   (l/! [:code] 1.54444444444444)
   (l/! [:live] 1.54444444444444)]))


(def
 var31_line172
 (display-output-format
  ['(. (BigInt "0x1fffffffffffff") (toString))
   (l/! [:code] (. (BigInt "0x1fffffffffffff") (toString)))
   (l/! [:live] (. (BigInt "0x1fffffffffffff") (toString)))]))


(def
 var33_line182
 (display-output-format
  ['(. (Symbol "hello") (toString))
   (l/! [:code] (. (Symbol "hello") (toString)))
   (l/! [:live] (. (Symbol "hello") (toString)))]))


(def
 var35_line192
 (display-output-format
  [#"^[Hh]ello d$"
   (l/! [:code] #"^[Hh]ello d$")
   (l/! [:live] #"^[Hh]ello d$")]))


(def
 var37_line199
 (display-output-table
  (+ 1 1)
  (+ 0.1 0.2)
  (- 8 1)
  (* 10 2)
  (/ 35 5)
  (/ 5 2)))


(def
 var39_line208
 (kind/hiccup
  [:div
   [:script
    (l/!
     [:code]
     (do
      (var m (L.map document.currentScript.parentElement))
      (m.setView [-37.84 144.95] 11)
      (-> (L.tileLayer.provider "OpenStreetMap.Mapnik") (. (addTo m)))
      (->
       [-37.9 144.8]
       L.marker
       (. (addTo m))
       (.
        (bindPopup "<i style='color:purple'>Have you been here?</i>"))
       (. (openPopup)))))]]
  {:html/deps [:leaflet], :style {:height "400px"}}))
