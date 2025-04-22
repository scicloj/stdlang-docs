(ns
 stdlang-book.walkthrough-generated-test
 (:require
  [std.lang :as l]
  [scicloj.kindly.v4.kind :as kind]
  [scicloj.kindly.v4.api :as kindly]
  [clojure.test :refer [deftest is]]))


(def var3_line18 (l/script :js {:runtime :basic}))


(def var4_line24 (l/script+ [:code :js]))


(def var5_line28 (l/script+ [:code :js]))


(def var7_line32 (!.js (+ 1 2)))


(def var8_line35 (l/! [:code] (+ 1 2 3)))


(deftest test9_line38 (is (= var8_line35 "1 + 2;")))


(def
 var11_line42
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
