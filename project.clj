(require 'cemerick.pomegranate.aether)
(require 'clojure.string)
(cemerick.pomegranate.aether/register-wagon-factory!
 "http" #(org.apache.maven.wagon.providers.http.HttpWagon.))
 
(defproject scicloj/stdlang-docs "0.1.0-SNAPSHOT"
  :description "scicloj documentation and walkthrough for std.lang"
  :url "https://scicloj.github.io/stdlang-docs/"
  :license {:name "MIT License"}
  :dependencies [[org.clojure/clojure "1.11.1"]
                 [org.scicloj/clay "2-beta40"]
                 [xyz.zcaudate/code.test           "4.0.8"]
                 [xyz.zcaudate/code.manage         "4.0.8"]
                 [xyz.zcaudate/code.java           "4.0.8"]
                 [xyz.zcaudate/code.maven          "4.0.8"]
                 [xyz.zcaudate/code.doc            "4.0.8"]
                 [xyz.zcaudate/code.dev            "4.0.8"]
                 [xyz.zcaudate/jvm                 "4.0.8"]
                 
                 [xyz.zcaudate/rt.basic            "4.0.8"]
                 [xyz.zcaudate/rt.redis            "4.0.8"]
                 [xyz.zcaudate/rt.postgres         "4.0.8"]
                 [xyz.zcaudate/rt.shell            "4.0.8"]
                 [xyz.zcaudate/rt.solidity         "4.0.8"]

                 [xyz.zcaudate/std.lib             "4.0.8"]
                 [xyz.zcaudate/std.log             "4.0.8"]
                 [xyz.zcaudate/std.lang            "4.0.8"]
                 [xyz.zcaudate/std.text            "4.0.8"]
                 [xyz.zcaudate/xtalk.db            "4.0.8"]
                 [xyz.zcaudate/xtalk.lang          "4.0.8"]
                 [xyz.zcaudate/xtalk.system        "4.0.8"]]
  :profiles {:dev {:plugins [[lein-ancient "0.6.15"]
                             [lein-exec "0.3.7"]
                             [lein-cljfmt "0.7.0"]
                             [cider/cider-nrepl "0.45.0"]]}
             :repl {:injections ['(try (require 'jvm.tool)
                                      (require '[std.lib :as h])
                                      (create-ns '.)
                                      (catch Throwable t (.printStackTrace t)))]}}
  :jvm-opts
  ["-Xms2048m"
   "-Xmx2048m"
   "-XX:MaxMetaspaceSize=1048m"
   "-XX:-OmitStackTraceInFastThrow"
   
   ;;
   ;; GC FLAGS
   ;;
   "-XX:+UseAdaptiveSizePolicy"
   "-XX:+AggressiveHeap"
   "-XX:+ExplicitGCInvokesConcurrent"
   ;;"-XX:+UseCMSInitiatingOccupancyOnly"
   ;;"-XX:+CMSClassUnloadingEnabled"
   ;;"-XX:+CMSParallelRemarkEnabled"

   ;;
   ;; GC TUNING
   ;;   
   "-XX:MaxNewSize=256m"
   "-XX:NewSize=256m"
   ;;"-XX:CMSInitiatingOccupancyFraction=60"
   "-XX:MaxTenuringThreshold=8"
   "-XX:SurvivorRatio=4"

   ;;
   ;; Truffle
   ;;
   "-Dpolyglot.engine.WarnInterpreterOnly=false"
   
   ;;
   ;; JVM
   ;;
   "-Djdk.tls.client.protocols=\"TLSv1,TLSv1.1,TLSv1.2\""
   "-Djdk.attach.allowAttachSelf=true"
   "--enable-native-access=ALL-UNNAMED"
   "--add-opens" "java.base/java.io=ALL-UNNAMED"
   "--add-opens" "java.base/java.lang=ALL-UNNAMED"
   "--add-opens" "java.base/java.lang.annotation=ALL-UNNAMED"
   "--add-opens" "java.base/java.lang.invoke=ALL-UNNAMED"
   "--add-opens" "java.base/java.lang.module=ALL-UNNAMED"
   "--add-opens" "java.base/java.lang.ref=ALL-UNNAMED"
   "--add-opens" "java.base/java.lang.reflect=ALL-UNNAMED"
   "--add-opens" "java.base/java.math=ALL-UNNAMED"
   "--add-opens" "java.base/java.net=ALL-UNNAMED"
   "--add-opens" "java.base/java.nio=ALL-UNNAMED"
   "--add-opens" "java.base/java.nio.channels=ALL-UNNAMED"
   "--add-opens" "java.base/java.nio.charset=ALL-UNNAMED"
   "--add-opens" "java.base/java.nio.file=ALL-UNNAMED"
   "--add-opens" "java.base/java.nio.file.attribute=ALL-UNNAMED"
   "--add-opens" "java.base/java.nio.file.spi=ALL-UNNAMED"
   "--add-opens" "java.base/java.security=ALL-UNNAMED"
   "--add-opens" "java.base/java.security.cert=ALL-UNNAMED"
   "--add-opens" "java.base/java.security.interfaces=ALL-UNNAMED"
   "--add-opens" "java.base/java.security.spec=ALL-UNNAMED"
   "--add-opens" "java.base/java.text=ALL-UNNAMED"
   "--add-opens" "java.base/java.time=ALL-UNNAMED"
   "--add-opens" "java.base/java.time.chrono=ALL-UNNAMED"
   "--add-opens" "java.base/java.time.format=ALL-UNNAMED"
   "--add-opens" "java.base/java.time.temporal=ALL-UNNAMED"
   "--add-opens" "java.base/java.time.zone=ALL-UNNAMED"
   "--add-opens" "java.base/java.util=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.concurrent=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.concurrent.atomic=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.concurrent.locks=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.function=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.jar=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.regex=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.spi=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.stream=ALL-UNNAMED"
   "--add-opens" "java.base/java.util.zip=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.loader=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.misc=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.module=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.org.objectweb.asm=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.org.xml.sax=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.perf=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.reflect=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.util=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.vm=ALL-UNNAMED"
   "--add-opens" "java.base/jdk.internal.vm.annotation=ALL-UNNAMED"

   "--add-opens" "java.net.http/java.net.http=ALL-UNNAMED"
   "--add-opens" "java.net.http/jdk.internal.net.http=ALL-UNNAMED"
   "--add-opens" "java.management/java.lang.management=ALL-UNNAMED"
   "--add-opens" "java.management/sun.management=ALL-UNNAMED"])
