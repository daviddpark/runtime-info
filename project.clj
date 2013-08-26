(defproject runtime-info "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/data.xml "0.0.7"]]

  ; Necessary to run leiningen plugins
  :eval-in-leiningen true

  ; Try different permutations of initial and maximum.
  ; What is reported by lein runtime-info?
  ;:jvm-opts ["-Xms128m" "-Xmx256m"]
  ;:jvm-opts ["-Xms256m" "-Xmx512m"]
  ;:jvm-opts ["-Xms512m" "-Xmx1g"]
  ;:jvm-opts ["-Xms256m" "-Xmx1g"]
  :jvm-opts ["-Xms128m" "-Xmx1g"]
  )
