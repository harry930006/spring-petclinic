FROM amazoncorretto:17-alpine-jdk

# 2. 設定容器內的工作目錄
WORKDIR /app

# 3. 將 Jenkins 剛剛用 Maven 編譯好的 jar 檔，複製到容器內部並改名為 app.jar
COPY target/*.jar app.jar

# 4. 暴露容器的 8080 連接埠（Port）
EXPOSE 8080

# 5. 容器啟動時執行的指令（此處由 Jenkins 啟動時動態注入 Profile 參數）
ENTRYPOINT ["java", "-jar", "app.jar"]
