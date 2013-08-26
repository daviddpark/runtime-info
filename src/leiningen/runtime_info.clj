(ns leiningen.runtime-info)

(defn runtime-info [project]
  (let [rt (java.lang.Runtime/getRuntime)
        max (.maxMemory rt)
        total (.totalMemory rt)
        free (.freeMemory rt)]
    (println "Maximum Memory: " max)
    (println "              : " (/ max 1024.0) "kb")
    (println "              : " (/ (/ max 1024.0) 1024.0) "mb")
    (println "  Total Memory: " total)
    (println "              : " (/ total 1024.0) "kb")
    (println "              : " (/ (/ total 1024.0) 1024.0) "mb")
    (println "   Free Memory: " free)
    (println "              : " (/ free 1024.0) "kb")
    (println "              : " (/ (/ free 1024.0) 1024.0) "mb")
    (println "Available Processors: " (.availableProcessors rt))))