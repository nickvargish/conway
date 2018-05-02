(defproject conway "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [criterium "0.4.3"]]
  :main ^:skip-aot conway.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}}
  :plugins [[lein-bin "0.3.5"]
            [lein-codox "0.9.4"]]
  :bin {:name "conway"
        :bin-path "target/bin"
        :bootclasspath true}
  :codox {:metadata {:doc/format :markdown}
          :output-path "target/doc"})
