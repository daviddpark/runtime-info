(ns leiningen.process-xml
  (:require [cheshire.core :as json]
            [clj-http.client :as http]
            [clojure.java.io :as io])
  (:use
   [clojure.data.xml]))

(defn process-element [elem]
  (let [content (:content elem)
        is-multiple (and (> (count content) 1)
                         (apply = (map :tag content)))
        has-attrs (> (count (:attrs elem)) 0)]
    (let [result {(:tag elem)
                  (if (and (= (str (type (first content))) "class java.lang.String")
                           (not has-attrs) (not is-multiple))
                    (first content)
                    (if is-multiple
                      (apply conj (map process-element content))
                      (if (not (empty? content))
                        (apply merge (map process-element content))))
                    )}]
      (if has-attrs
        (merge {:attributes (:attrs elem)}
               result)
        result))))

(defn process-drug [elem]
  (let [drug (merge {:attributes (:attrs elem)}
                    (apply merge
                           (map process-element (:content elem))))
        json-drug (json/generate-string drug
                                        {:pretty true})]
    (println "Finished processing drug " (:drugbank-id drug))
    (println "Free Memory: " (/ (/ (.freeMemory (java.lang.Runtime/getRuntime)) 1024.0) 1024.0) "mb")
    (println "")))

(defn process-xml [project & args]
  (let [drugs-file (if (empty? args)
                     "/tmp/drugbank.xml"
                     (first args))]
    (if (empty? args)
      (let [zip-file "/tmp/drugbank.xml.zip"
            data-url "http://www.drugbank.ca/system/downloads/current/drugbank.xml.zip"
            con (-> data-url java.net.URL. .openConnection)
            buffer (make-array Byte/TYPE 4096)
            in (java.util.zip.ZipInputStream. (.getInputStream con))]
        (loop [ze (.getNextEntry in)
               ze-size (.getSize ze)]
          (if-not (nil? ze)
            (let [out (java.io.FileOutputStream. drugs-file)]
              (loop [g (.read in buffer 0 4096)
                     r 0]
                (if-not (= g -1)
                  (do (println r "/" ze-size)
                      (.write out buffer 0 g)
                      (recur (.read in buffer 0 4096) (+ r g)))))
              (.close out))
            ))
        (.close in)
        (.disconnect con)))
    (def data (parse (io/reader drugs-file)))
    (loop [elems (:content data)]
      (let [elem (first elems)]
        (if (= (:tag elem) :drug)
          (process-drug elem)))
      (if (next elems)
        (recur (rest elems))))))