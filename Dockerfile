FROM java:8

WORKDIR /home/ubuntu/app

COPY target/api-gateway.jar /home/ubuntu/app

# CMD ["java","-Dspring.profiles.active=dev", "-Duser.timezone=Asia/Shanghai","-Dfile.encoding=utf8", "-Xms64m","-Xmx125m","-jar","api-gateway.jar"]

CMD ["java", "-Duser.timezone=Asia/Shanghai","-Denv=DEV","-Dfile.encoding=utf8", "-Xms64m","-Xmx125m","-jar","api-gateway.jar"]